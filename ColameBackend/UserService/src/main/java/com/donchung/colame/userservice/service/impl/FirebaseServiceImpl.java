package com.donchung.colame.userservice.service.impl;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.userservice.UserServiceApplication;
import com.donchung.colame.userservice.constaints.UserConstraints;
import com.donchung.colame.userservice.service.FirebaseService;
import com.google.cloud.storage.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class FirebaseServiceImpl implements FirebaseService {
    private final String bucket = "colame-bccd3.appspot.com";
    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassLoader classLoader = UserServiceApplication.class.getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
            FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build().getService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object uploadFile(MultipartFile file, String location, String fileName) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("firebaseStorageDownloadTokens", fileName);
            BlobId blobId = BlobId.of(bucket, location + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setMetadata(map)
                    .setContentType(file.getContentType())
                    .build();
            Blob blob = storage.create(blobInfo, file.getInputStream());
            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s%s?alt=media", bucket, location.replace("/", "%2F"), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean isExists(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucket, fileName);
            Blob blob = storage.get(blobId);
            if (blob != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String generateFileName(String originalFileName) {
        return String.format("%s", originalFileName);
    }
}
