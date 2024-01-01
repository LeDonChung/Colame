package com.donchung.colame.userservice.utils;

import com.donchung.colame.userservice.POJO.User;
import com.donchung.colame.userservice.utils.request.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ConverterUtils {
    public static User toUser(User user, UserRequestDTO request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }
}
