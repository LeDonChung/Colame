package com.donchung.colame.userservice.controllers;

import com.donchung.colame.userservice.POJO.User;
import com.donchung.colame.userservice.repositories.UserRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/home")
    public void home() {
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setStatus(true);
        userRepository.save(user);
    }
}
