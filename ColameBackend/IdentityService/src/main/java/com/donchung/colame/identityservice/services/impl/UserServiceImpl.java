package com.donchung.colame.identityservice.services.impl;

import com.donchung.colame.commonservice.constraints.KafkaTopic;
import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.identityservice.POJO.User;
import com.donchung.colame.identityservice.jwt.JwtService;
import com.donchung.colame.identityservice.repositories.RoleRepository;
import com.donchung.colame.identityservice.repositories.UserRepository;
import com.donchung.colame.identityservice.services.UserService;
import com.donchung.colame.identityservice.utils.request.ChangePasswordRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserSignUpDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public ResponseEntity<ApiResponse<Object>> register(UserSignUpDTO request) {
        try {
            log.info("Start register for user {}.", request.getUsername());
            // Kiểm tra username tồn tại
            User isExists = userRepository.findByUsername(request.getUsername());
            if (isExists != null) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .data(SystemConstraints.ACCOUNT_ALREADY_EXISTS)
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            BeanUtils.copyProperties(request, user);
            user.setUserId(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList(roleRepository.findByCode("USER")));

            // User must be verify account
            user.setStatus(false);

            user = userRepository.save(user);

            kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("User %s register account successfully.", request.getUsername()));
            log.info("End register for user {}.", request.getUsername());

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.CREATED.toString())
                            .data(SystemConstraints.REGISTER_ACCOUNT_SUCCESSFULLY)
                            .build(),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Register fail for user {} because {}.", request.getUsername(), e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.isValidToken(token);
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> changePassword(ChangePasswordRequestDTO passwordDTO) {
        try {
            log.info("Start change password for user {}.", passwordDTO.getUsername());
            User userCurrent = userRepository.findByUsername(passwordDTO.getUsername());
            String message = "";
            if (userCurrent != null) {
                boolean match = passwordEncoder.matches(passwordDTO.getPasswordOld(), userCurrent.getPassword());
                if (!match) {
                    message = "Password old is not matches";
                } else {
                    userCurrent.setPassword(passwordEncoder.encode(passwordDTO.getPasswordNew()));
                    message = "Change password successfully";
                    userRepository.save(userCurrent);
                }

                log.info("Change password for user {} successfully", passwordDTO.getUsername());
                kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("Change password for user %s successfully.", passwordDTO.getUsername()));
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(message)
                                .build(),
                        HttpStatus.OK);
            } else {
                log.info("Change password for user {} fail because {}.", passwordDTO.getUsername(), SystemConstraints.ACCESS_DENIED);
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .code(HttpStatus.UNAUTHORIZED.toString())
                                .data(SystemConstraints.ACCESS_DENIED)
                                .build(),
                        HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .success(false)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
