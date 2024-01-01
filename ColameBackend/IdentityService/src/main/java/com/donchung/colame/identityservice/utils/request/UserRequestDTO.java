package com.donchung.colame.identityservice.utils.request;

import com.donchung.colame.identityservice.POJO.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTO {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String avatar;

    private String cover;

    private Boolean status;

}
