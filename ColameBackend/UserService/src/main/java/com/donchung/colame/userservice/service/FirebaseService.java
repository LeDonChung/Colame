package com.donchung.colame.userservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseService {
    Object uploadFile(MultipartFile file, String location, String fileName);
    boolean isExists(String fileName);
}
