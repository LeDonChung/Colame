package com.donchung.colame.userservice.service.impl;

import com.donchung.colame.commonservice.constraints.KafkaTopic;
import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.userservice.POJO.User;
import com.donchung.colame.userservice.constaints.UserConstraints;
import com.donchung.colame.userservice.repositories.UserRepository;
import com.donchung.colame.userservice.service.FirebaseService;
import com.donchung.colame.userservice.service.UserService;
import com.donchung.colame.userservice.utils.ConverterUtils;
import com.donchung.colame.userservice.utils.request.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public ResponseEntity<ApiResponse<Object>> enable(String userId) {
        try {
            log.info("Start enable account for user {}", userId);
            Optional<User> userIs = userRepository.findById(userId);
            if (userIs.isPresent()) {
                User user = userIs.get();
                user.setStatus(true);
                user = userRepository.save(user);
                log.info("Enable account for user {} successfully.", userId);
                kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("Enable account for user %s successfully.", user.getUsername()));
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(user.toUserResponseDto())
                                .build(),
                        HttpStatus.OK
                );
            }
            log.info("Enable fail account for user {} because {}", userId, "not found.");
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.NOT_FOUND.toString())
                            .data(String.format("User with id %s is not found", userId))
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("End enable account for user {}", userId);
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> disable(String userId) {
        try {
            log.info("Start disable account for user {}", userId);

            Optional<User> userIs = userRepository.findById(userId);
            if (userIs.isPresent()) {
                User user = userIs.get();
                user.setStatus(false);
                user = userRepository.save(user);
                kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("Disable account for user %s successfully.", user.getUsername()));
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(user.toUserResponseDto())
                                .build(),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.NOT_FOUND.toString())
                            .data(String.format("User with id %s is not found", userId))
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();

        }
        log.info("End disable account for user {}", userId);
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateProfile(UserRequestDTO request) {
        try {
            Optional<User> isExists = userRepository.findById(request.getUserId());
            if (isExists.isPresent()) {
                // update
                User user = isExists.get();

                user = ConverterUtils.toUser(user, request);

                kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("Update profile for user %s successfully.", user.getUsername()));
                user = userRepository.save(user);
                return new ResponseEntity<>(
                        ApiResponse
                                .builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(user.toUserResponseDto())
                                .build(),
                        HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.NOT_FOUND.toString())
                            .data(String.format("User with id %s is not found", request.getUserId()))
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateAvatar(String username, MultipartFile avatar) {
        try {
            User isExists = userRepository.findByUsername(username);
            if (isExists != null) {

                String url = firebaseService.uploadFile(avatar, UserConstraints.LOCATION_AVATAR, username).toString();

                isExists.setAvatar(url);
                isExists = userRepository.save(isExists);

                kafkaTemplate.send(KafkaTopic.USER_TOPIC, String.format("Update avatar for user %s successfully.", isExists.getUsername()));

                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(isExists.toUserResponseDto())
                                .build(),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.NOT_FOUND.toString())
                            .data(String.format("User with username %s is not found", username))
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> updateCover(String username, MultipartFile cover) {
        try {
            User isExists = userRepository.findByUsername(username);
            if (isExists != null) {

                String url = firebaseService.uploadFile(cover, UserConstraints.LOCATION_COVER, username).toString();

                isExists.setCover(url);
                isExists = userRepository.save(isExists);

                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(true)
                                .code(HttpStatus.OK.toString())
                                .data(isExists.toUserResponseDto())
                                .build(),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.NOT_FOUND.toString())
                            .data(String.format("User with username %s is not found", username))
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
