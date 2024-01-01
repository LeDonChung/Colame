package com.donchung.colame.userservice.service;


import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.userservice.utils.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<ApiResponse<Object>> enable(String userId);
    ResponseEntity<ApiResponse<Object>> disable(String userId);
    ResponseEntity<ApiResponse<Object>> updateProfile(UserRequestDTO request);
    ResponseEntity<ApiResponse<Object>> updateAvatar(String username, MultipartFile avatar);
    ResponseEntity<ApiResponse<Object>> updateCover(String username, MultipartFile cover);
}
