package com.donchung.colame.postservice.utils;

import com.donchung.colame.commonservice.utils.ValidateUtils;
import com.donchung.colame.postservice.utils.request.PostRequestDTO;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;

import java.util.HashMap;
import java.util.Map;


public class ValidateObject {
    public static Map<String, String> validate(TagRequestDTO tag) {
        Map<String, String> errors = new HashMap<>();
        errors.putAll(ValidateUtils.builder()
                .fieldName("name")
                .value(tag.getName())
                .required(true)
                .build().validate());

        errors.putAll(ValidateUtils.builder()
                .fieldName("code")
                .value(tag.getCode())
                .required(true)
                .build().validate());

        errors.putAll(ValidateUtils.builder()
                .fieldName("background")
                .value(tag.getBackground())
                .required(true)
                .build().validate());
        return errors;
    }

    public static Map<String, String> validate(PostRequestDTO post) {
        Map<String, String> errors = new HashMap<>();
        errors.putAll(ValidateUtils.builder()
                .fieldName("postCode")
                .value(post.getPostCode())
                .required(true)
                .build().validate());

        errors.putAll(ValidateUtils.builder()
                .fieldName("title")
                .value(post.getTitle())
                .required(true)
                .min(15L)
                .build().validate());

        return errors;
    }
}
