package com.donchung.colame.postservice.services.impl;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.POJO.Tag;
import com.donchung.colame.postservice.mapper.TagMapper;
import com.donchung.colame.postservice.repositories.TagRepository;
import com.donchung.colame.postservice.services.TagService;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import com.donchung.colame.postservice.utils.response.TagResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseEntity<ApiResponse<Object>> findAll() {
        try {

            List<Tag> tags = tagRepository.findAll();
            List<TagResponseDTO> tagResponseDTOS = tags.stream().map(tag -> tagMapper.toResponseDto(tag)).toList();
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagResponseDTOS)
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> create(TagRequestDTO request) {

        log.info("Start create tag.");

        try {
            Optional<Tag> tagExists = tagRepository.findByCode(request.getCode());
            if (tagExists.isPresent()) {
                new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Tag with code %s is exists.", request.getCode()))
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }


            Tag tagNew = tagMapper.toEntity(request);
            tagNew.setId(UUID.randomUUID().toString());
            tagNew.setStatus(true);

            tagNew = tagRepository.save(tagNew);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagMapper.toResponseDto(tagNew))
                            .code(HttpStatus.CREATED.toString())
                            .build(),
                    HttpStatus.CREATED
            );

        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        } finally {
            log.info("End create tag.");
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> update(TagRequestDTO request) {
        log.info("Start update tag.");

        try {
            Optional<Tag> tagOld = tagRepository.findById(request.getId());
            if (tagOld.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Tag with id %s not found", request.getId()))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }

            if (!tagOld.get().getCode().equals(request.getCode())) {

                Optional<Tag> tagExists = tagRepository.findByCode(request.getCode());
                if (tagExists.isPresent()) {
                    return new ResponseEntity<>(
                            ApiResponse.builder()
                                    .success(false)
                                    .data(String.format("Tag with code %s is exists.", request.getCode()))
                                    .code(HttpStatus.BAD_REQUEST.toString())
                                    .build(),
                            HttpStatus.BAD_REQUEST
                    );
                }

            }

            Tag tagNew = tagMapper.toEntity(tagOld.get(), request);

            tagNew = tagRepository.save(tagNew);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagMapper.toResponseDto(tagNew))
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        } finally {
            log.info("End update tag.");
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> enable(String tagCode) {
        log.info("Start enable tag code {}.", tagCode);

        try {
            Optional<Tag> tag = tagRepository.findByCode(tagCode);
            if (tag.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Tag with code %s not found", tagCode))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }

            Tag tagNew = tag.get();
            tagNew.setStatus(true);
            tagNew = tagRepository.save(tagNew);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagMapper.toResponseDto(tagNew))
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        } finally {
            log.info("End enable tag code {}.", tagCode);
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> disable(String tagCode) {
        log.info("Start disable code id {}.", tagCode);

        try {
            Optional<Tag> tag = tagRepository.findByCode(tagCode);
            if (tag.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Tag with code %s not found", tagCode))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }

            Tag tagNew = tag.get();
            tagNew.setStatus(false);
            tagNew = tagRepository.save(tagNew);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagMapper.toResponseDto(tagNew))
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        } finally {
            log.info("End disable tag code {}.", tagCode);
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> findAllByActive() {
        try {

            List<Tag> tags = tagRepository.findByStatus(true);
            List<TagResponseDTO> tagResponseDTOS = tags.stream().map(tag -> tagMapper.toResponseDto(tag)).toList();
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(tagResponseDTOS)
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Tag error: {}", e.getMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .success(false)
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
