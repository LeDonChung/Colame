package com.donchung.colame.userservice.service.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    @Override
    public ResponseEntity<ApiResponse<Object>> enable(String userId) {
        try {
            Optional<User> userIs = userRepository.findById(userId);
            if (userIs.isPresent()) {
                User user = userIs.get();
                user.setStatus(true);
                user = userRepository.save(user);
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
            Optional<User> userIs = userRepository.findById(userId);
            if (userIs.isPresent()) {
                User user = userIs.get();
                user.setStatus(false);
                user = userRepository.save(user);
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
            if(isExists != null) {

                String url = firebaseService.uploadFile(avatar, UserConstraints.LOCATION_AVATAR, username).toString();

                isExists.setAvatar(url);
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

    @Override
    public ResponseEntity<ApiResponse<Object>> updateCover(String username, MultipartFile cover) {
        try {
            User isExists = userRepository.findByUsername(username);
            if(isExists != null) {

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
