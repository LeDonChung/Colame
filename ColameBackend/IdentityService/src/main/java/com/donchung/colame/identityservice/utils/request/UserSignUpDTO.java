package com.donchung.colame.identityservice.utils.request;

import com.donchung.colame.identityservice.POJO.UsernameType;
import lombok.Data;

@Data
public class UserSignUpDTO {
    private String username;
    private String password;
    private String rePassword;
    private String firstName;
    private String lastName;
    private UsernameType usernameType;
}
