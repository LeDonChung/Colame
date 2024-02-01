package com.donchung.colame.postservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    @GetMapping("/all")
    public String getAll() {
        return "hello";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdmin() {
        return "admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String getUser() {
        return "user";
    }

}
