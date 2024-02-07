package com.donchung.colame.postservice.services.impl;

import com.donchung.colame.commonservice.constraints.SystemConstraints;
import com.donchung.colame.commonservice.dto.PageDTO;
import com.donchung.colame.commonservice.dto.SortName;
import com.donchung.colame.commonservice.dto.SorterDTO;
import com.donchung.colame.commonservice.utils.PageUtils;
import com.donchung.colame.commonservice.utils.response.ApiResponse;
import com.donchung.colame.postservice.POJO.Post;
import com.donchung.colame.postservice.POJO.Tag;
import com.donchung.colame.postservice.jwt.JwtService;
import com.donchung.colame.postservice.mapper.PostMapper;
import com.donchung.colame.postservice.mapper.TagMapper;
import com.donchung.colame.postservice.repositories.PostRepository;
import com.donchung.colame.postservice.repositories.TagRepository;
import com.donchung.colame.postservice.services.PostService;
import com.donchung.colame.postservice.utils.request.PostRequestDTO;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import com.donchung.colame.postservice.utils.response.PostResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private JwtService jwtService;


    @Override
    public ResponseEntity<ApiResponse<Object>> findAll() {
        try {
            List<Post> posts = postRepository.findAll();
            List<PostResponseDTO> responseDTOS = posts.stream().map(post -> postMapper.toResponseDto(post)).toList();

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(responseDTOS)
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private Sort getSort(SorterDTO sorterDTO) {
        return sorterDTO.getSortName().equals(SortName.asc)
                ? Sort.by(sorterDTO.getSortBy()).ascending()
                : Sort.by(sorterDTO.getSortBy()).descending();
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getPages(PageDTO pageDTO) {
        try {
            Sort sort = getSort(pageDTO.getSorter());
            Pageable pageable = PageRequest.of(pageDTO.getPageIndex() - 1, pageDTO.getPageSize(), sort);
            List<Post> posts = postRepository.findAll(pageable).stream().toList();

            List<PostResponseDTO> responseDTOS = posts.stream().map(post -> postMapper.toResponseDto(post)).toList();

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(PageUtils.getPage(pageDTO, Arrays.asList(responseDTOS.toArray()), (int) postRepository.count()))
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> create(PostRequestDTO request) {
        log.info("Start create post.");
        try {
            Optional<Post> postExists = postRepository.findByPostCode(request.getPostCode());
            if (postExists.isPresent()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .data(String.format("Post with post code %s is exists.", request.getPostCode()))
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .success(false)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }


            Post post = postMapper.toEntity(request);
            List<Tag> tags = request.getTags().stream().map(tag -> tagRepository.findByCode(tag.getCode()).get()).toList();
            post.setTags(tags);
            post.setId(UUID.randomUUID().toString());
            post.setStatus(true);
            post.setViewer(0);
            post.setUsername(jwtService.getUsername());

            post = postRepository.save(post);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.CREATED.toString())
                            .data(postMapper.toResponseDto(post))
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        } finally {
            log.info("End create post.");
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> update(PostRequestDTO request) {
        log.info("Start create post.");
        try {
            Optional<Post> postExists = postRepository.findByPostCode(request.getPostCode());

            if (postExists.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .data(String.format("Post with post code %s is not exists.", request.getPostCode()))
                                .code(HttpStatus.BAD_REQUEST.toString())
                                .success(false)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }


            Post post = postExists.get();
            if (!jwtService.idAdmin()) {
                if (!jwtService.getUsername().equalsIgnoreCase(post.getUsername())) {
                    return new ResponseEntity<>(
                            ApiResponse.builder()
                                    .code(HttpStatus.UNAUTHORIZED.toString())
                                    .data(SystemConstraints.ACCESS_DENIED)
                                    .success(false)
                                    .build(),
                            HttpStatus.UNAUTHORIZED
                    );
                }
            }


            post = postMapper.toEntity(post, request);
            List<Tag> tags = request.getTags().stream().map(tag -> tagRepository.findByCode(tag.getCode()).get()).collect(Collectors.toList());
            post.setTags(tags);

            post = postRepository.save(post);

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .code(HttpStatus.CREATED.toString())
                            .data(postMapper.toResponseDto(post))
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Post error: {}", e.getMessage());
        } finally {
            log.info("End create post.");
        }

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> enable(String postCode) {
        log.info("Start enable post by code {}", postCode);
        try {
            Optional<Post> post = postRepository.findByPostCode(postCode);
            if (post.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Post with code %s not found", postCode))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }

            Post postNew = post.get();
            postNew.setStatus(true);

            postNew = postRepository.save(postNew);
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .code(HttpStatus.OK.toString())
                            .data(postMapper.toResponseDto(postNew))
                            .success(true)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        } finally {
            log.info("End enable post by code {}", postCode);
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> disable(String postCode) {
        log.info("Start disable post by code {}", postCode);
        try {
            Optional<Post> post = postRepository.findByPostCode(postCode);
            if (post.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Post with code %s not found", postCode))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }

            Post postNew = post.get();
            postNew.setStatus(false);

            postNew = postRepository.save(postNew);
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .code(HttpStatus.OK.toString())
                            .data(postMapper.toResponseDto(postNew))
                            .success(true)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        } finally {
            log.info("End disable post by code {}", postCode);
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> delete(String postCode) {
        log.info("Start delete post by code {}", postCode);
        try {
            Optional<Post> post = postRepository.findByPostCode(postCode);

            if (post.isEmpty()) {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .success(false)
                                .data(String.format("Post with code %s not found", postCode))
                                .code(HttpStatus.NOT_FOUND.toString())
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }
            Post postNew = post.get();


            if (!jwtService.idAdmin()) {
                if (!jwtService.getUsername().equalsIgnoreCase(postNew.getUsername())) {
                    return new ResponseEntity<>(
                            ApiResponse.builder()
                                    .code(HttpStatus.UNAUTHORIZED.toString())
                                    .data(SystemConstraints.ACCESS_DENIED)
                                    .success(false)
                                    .build(),
                            HttpStatus.UNAUTHORIZED
                    );
                }
            }

            postNew.setStatus(false);

            postNew = postRepository.save(postNew);
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .code(HttpStatus.OK.toString())
                            .data(postMapper.toResponseDto(postNew))
                            .success(true)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        } finally {
            log.info("End delete post by code {}", postCode);
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> findByTagCode(String tagCode) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getPagesActive(PageDTO pageDTO) {
        try {
            Sort sort = getSort(pageDTO.getSorter());
            Pageable pageable = PageRequest.of(pageDTO.getPageIndex() - 1, pageDTO.getPageSize(), sort);
            List<Post> posts = postRepository.findByStatus(true, pageable).stream().toList();

            List<PostResponseDTO> responseDTOS = posts.stream().map(post -> postMapper.toResponseDto(post)).toList();

            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .success(true)
                            .data(PageUtils.getPage(pageDTO, Arrays.asList(responseDTOS.toArray()), postRepository.countPostByStatusIs(true)))
                            .code(HttpStatus.OK.toString())
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.info("Post error: {}", e.getMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .data(SystemConstraints.SOMETHING_WENT_WRONG)
                        .success(false)
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
