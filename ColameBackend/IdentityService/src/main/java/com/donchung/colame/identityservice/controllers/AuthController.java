package com.donchung.colame.identityservice.controllers;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.identityservice.service.UserService;
import com.donchung.colame.identityservice.utils.ValidateObject;
import com.donchung.colame.identityservice.utils.request.ChangePasswordRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserSignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<Object>> signUp(@RequestBody UserSignUpDTO user) {
        try {
            // Kiểm tra hợp lệ
            Map<String, String> errors = ValidateObject.validateUserSignUpDTO(user);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .data(errors)
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra nhập lại mật khẩu
            if (!user.getRePassword().equals(user.getPassword())) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .data(SystemConstraints.PASSWORD_NOT_MATCHES)
                                .build(),
                        HttpStatus.BAD_REQUEST);
            }

            // Đăng ký tài khoản
            return userService.register(user);
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
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@RequestBody ChangePasswordRequestDTO passwordDTO) {
        try {
            return userService.changePassword(passwordDTO);
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
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Object>> validate(@RequestParam("token") String token) {

        try {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.OK.toString())
                            .data(userService.validateToken(token))
                            .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(false)
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .data(e.getLocalizedMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
