package com.donchung.colame.userservice.utils.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse<T> {
    private T data;
    private Boolean success;
    private String code;
}
