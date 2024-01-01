package com.donchung.colame.identityservice.service;

import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.identityservice.utils.request.ChangePasswordRequestDTO;
import com.donchung.colame.identityservice.utils.request.UserSignUpDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<ApiResponse<Object>> register(UserSignUpDTO request);

    boolean validateToken(String token);

    ResponseEntity<ApiResponse<Object>> changePassword(ChangePasswordRequestDTO passwordDTO);
}
