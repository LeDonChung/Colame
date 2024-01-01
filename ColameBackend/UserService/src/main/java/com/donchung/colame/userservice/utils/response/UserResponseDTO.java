package com.donchung.colame.userservice.utils.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    private String avatar;

    private String cover;

    private Boolean status;
}
