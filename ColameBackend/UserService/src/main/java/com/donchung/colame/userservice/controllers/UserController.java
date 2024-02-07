package com.donchung.colame.userservice.controllers;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.userservice.service.UserService;
import com.donchung.colame.userservice.utils.ValidateObject;
import com.donchung.colame.userservice.utils.request.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> enable(@RequestParam("userId") String userId) {
        try {
            return userService.enable(userId);
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

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> disable(@RequestParam("userId") String userId) {
        try {
            return userService.disable(userId);
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

    @PutMapping("/updateProfile")
    @PreAuthorize("hasAuthority('USER') and authentication.principal.equals(#request.username)")
    public ResponseEntity<ApiResponse<Object>> updateProfile(@RequestBody UserRequestDTO request) {
        try {
            // Kiểm tra hợp lệ
            Map<String, String> errors = ValidateObject.validateUserRequestDto(request);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .data(errors)
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }


            return userService.updateProfile(request);
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

    @PostMapping("/uploadAvatar")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.equals(#username)")
    public ResponseEntity<ApiResponse<Object>> updateAvatar(@RequestParam("avatar") MultipartFile avatar, @RequestParam String username) {
        try {
            return userService.updateAvatar(username, avatar);
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

    @PostMapping("/uploadCover")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN') and authentication.principal.equals(#username)")
    public ResponseEntity<ApiResponse<Object>> updateCover(@RequestParam("cover") MultipartFile cover, @RequestParam String username) {
        try {
            return userService.updateCover(username, cover);
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


}
