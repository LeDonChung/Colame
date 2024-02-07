package com.donchung.colame.userservice.utils;

import com.donchung.colame.commonservice.utils.ValidateUtils;
import com.donchung.colame.userservice.utils.request.UserRequestDTO;

import java.util.HashMap;
import java.util.Map;

public class ValidateObject {
    public static Map<String, String> validateUserRequestDto(UserRequestDTO user) {
        Map<String, String> errors = new HashMap<>();
        errors.putAll(ValidateUtils.builder()
                .fieldName("firstName")
                .value(user.getFirstName())
                .required(true)
                .build().validate());

        errors.putAll(ValidateUtils.builder()
                .fieldName("lastName")
                .value(user.getLastName())
                .required(true)
                .build().validate());

        return errors;
    }
}
