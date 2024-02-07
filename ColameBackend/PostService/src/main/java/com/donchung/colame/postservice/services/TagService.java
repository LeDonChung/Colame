package com.donchung.colame.postservice.services;

import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import org.springframework.http.ResponseEntity;

public interface TagService {
    /* ADMIN */
    ResponseEntity<ApiResponse<Object>> findAll();

    ResponseEntity<ApiResponse<Object>> create(TagRequestDTO request);

    ResponseEntity<ApiResponse<Object>> update(TagRequestDTO request);

    ResponseEntity<ApiResponse<Object>> enable(String tagCode);

    ResponseEntity<ApiResponse<Object>> disable(String tagCode);

    /* USER */
    ResponseEntity<ApiResponse<Object>> findAllByActive();
}
