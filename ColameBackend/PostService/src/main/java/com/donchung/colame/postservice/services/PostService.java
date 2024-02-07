package com.donchung.colame.postservice.services;

import com.donchung.colame.commonservice.dto.PageDTO;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.utils.request.PostRequestDTO;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import org.springframework.http.ResponseEntity;

public interface PostService {
    /* ADMIN */
    ResponseEntity<ApiResponse<Object>> findAll();

    ResponseEntity<ApiResponse<Object>> getPages(PageDTO pageDTO);

    ResponseEntity<ApiResponse<Object>> enable(String postCode);

    /* USER */
    ResponseEntity<ApiResponse<Object>> findByTagCode(String tagCode);
    ResponseEntity<ApiResponse<Object>> getPagesActive(PageDTO pageDTO);

    /* ADMIN AND USER */
    ResponseEntity<ApiResponse<Object>> create(PostRequestDTO request);

    ResponseEntity<ApiResponse<Object>> update(PostRequestDTO request);

    ResponseEntity<ApiResponse<Object>> disable(String postCode);

    ResponseEntity<ApiResponse<Object>> delete(String postCode);
}
