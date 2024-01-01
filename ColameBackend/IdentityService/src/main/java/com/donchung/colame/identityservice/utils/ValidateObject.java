package com.donchung.colame.identityservice.utils;

import com.donchung.colame.identityservice.POJO.UsernameType;
import com.donchung.colame.identityservice.utils.request.UserSignUpDTO;

import java.util.HashMap;
import java.util.Map;

public class ValidateObject {
    public static Map<String, String> validateUserSignUpDTO(UserSignUpDTO user) {
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

        UsernameType type = user.getUsernameType();
        if (type == UsernameType.EMAIL) {
            errors.putAll(ValidateUtils.builder()
                    .fieldName("username")
                    .value(user.getUsername())
                    .required(true)
                    .isEmail(true)
                    .build().validate());
            return errors;
        } else if (type == UsernameType.PHONE) {
            errors.putAll(ValidateUtils.builder()
                    .fieldName("username")
                    .value(user.getUsername())
                    .required(true)
                    .isPhone(true)
                    .build().validate());
        }

        return errors;
    }
}
