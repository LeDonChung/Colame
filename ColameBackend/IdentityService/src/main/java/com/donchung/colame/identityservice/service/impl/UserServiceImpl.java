package com.donchung.colame.identityservice.service.impl;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.identityservice.POJO.User;
import com.donchung.colame.identityservice.jwt.JwtService;
import com.donchung.colame.identityservice.repositories.RoleRepository;
import com.donchung.colame.identityservice.repositories.UserRepository;
import com.donchung.colame.identityservice.service.UserService;
import com.donchung.colame.identityservice.utils.request.ChangePasswordRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserSignUpDTO;
import com.donchung.colame.identityservice.utils.response.UserResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<ApiResponse<Object>> register(UserSignUpDTO request) {
        try {
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
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.CREATED.toString())
                            .data(SystemConstraints.REGISTER_ACCOUNT_SUCCESSFULLY)
                            .build(),
                    HttpStatus.CREATED);
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

    @Override
    public boolean validateToken(String token) {
        return jwtService.isValidToken(token);
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> changePassword(ChangePasswordRequestDTO passwordDTO) {
        try {
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
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(message)
                                .build(),
                        HttpStatus.OK);
            } else {
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
