package com.donchung.colame.gateway.external.services;

import com.donchung.colame.commonservice.utils.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "IDENTITY-SERVICE")
@Service
public interface AuthService {
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Object>> validate(@RequestParam("token") String token);
}
