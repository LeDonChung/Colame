package com.donchung.colame.postservice.controllers;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.services.TagService;
import com.donchung.colame.postservice.utils.ValidateObject;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tag")
@AllArgsConstructor
@Slf4j
public class TagController {
    private final TagService tagService;

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> getAll() {
        try {
            return tagService.findAll();
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody TagRequestDTO requestDTO) {
        try {
            Map<String, String> errors = ValidateObject.validate(requestDTO);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.BAD_REQUEST.toString()).data(errors).success(false).build(), HttpStatus.BAD_REQUEST);
            }
            return tagService.create(requestDTO);
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody TagRequestDTO requestDTO) {
        try {
            Map<String, String> errors = ValidateObject.validate(requestDTO);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.BAD_REQUEST.toString()).data(errors).success(false).build(), HttpStatus.BAD_REQUEST);
            }
            return tagService.update(requestDTO);
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> enable(@RequestParam(value = "tagCode", required = true) String tagCode) {
        try {
            return tagService.enable(tagCode);
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> disable(@RequestParam(value = "tagCode", required = true) String tagCode) {
        try {
            return tagService.disable(tagCode);
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getAllByActive")
    public ResponseEntity<ApiResponse<Object>> getAllByActive() {
        try {
            return tagService.findAllByActive();
        } catch (Exception e) {
            log.info("Tag Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
