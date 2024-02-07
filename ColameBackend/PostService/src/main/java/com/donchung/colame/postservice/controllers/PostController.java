package com.donchung.colame.postservice.controllers;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.dto.PageDTO;
import com.donchung.colame.commonservice.utils.ValidateUtils;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.services.PostService;
import com.donchung.colame.postservice.utils.ValidateObject;
import com.donchung.colame.postservice.utils.request.PostRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> getAll() {
        try {
            return postService.findAll();
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @GetMapping("/getByTagCode")
    public ResponseEntity<ApiResponse<Object>> getByTagCode(@RequestParam String tagCode) {
        try {
            return postService.findByTagCode(tagCode);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getPagesActive")
    public ResponseEntity<ApiResponse<Object>> getPagesActive(@RequestBody PageDTO pageDTO) {
        try {
            return postService.getPagesActive(pageDTO);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody PostRequestDTO request) {
        try {
            Map<String, String> errors = ValidateObject.validate(request);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.builder().data(errors).code(HttpStatus.BAD_REQUEST.name()).success(false).build(), HttpStatus.BAD_REQUEST);
            }

            return postService.create(request);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or authentication.principal.equals(#request.username)")
    public ResponseEntity<ApiResponse<Object>> update(@RequestBody PostRequestDTO request) {
        try {
            Map<String, String> errors = ValidateObject.validate(request);
            if (!errors.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.builder().data(errors).code(HttpStatus.BAD_REQUEST.name()).success(false).build(), HttpStatus.BAD_REQUEST);
            }


            return postService.update(request);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> enable(@RequestParam String postCode) {
        try {
            return postService.enable(postCode);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> disable(@RequestParam String postCode) {
        try {
            return postService.disable(postCode);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Object>> delete(@RequestParam String postCode) {
        try {
            return postService.delete(postCode);
        } catch (Exception e) {
            log.info("Post Controller error: {}", e.getMessage());
        }
        return new ResponseEntity<>(ApiResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).data(SystemConstraints.SOMETHING_WENT_WRONG).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
