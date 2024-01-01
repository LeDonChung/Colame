package com.donchung.colame.userservice.utils.request;

import com.donchung.colame.userservice.POJO.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserRequestDTO {
    private String userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String avatar;

    private String cover;

    private Boolean status;
}
