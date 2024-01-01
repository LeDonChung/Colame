package com.donchung.colame.identityservice.utils.request;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String username;
    private String passwordOld;
    private String passwordNew;
}
