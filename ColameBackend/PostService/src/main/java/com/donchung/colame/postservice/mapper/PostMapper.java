package com.donchung.colame.postservice.mapper;

import com.donchung.colame.postservice.POJO.Post;
import com.donchung.colame.postservice.POJO.Tag;
import com.donchung.colame.postservice.utils.request.PostRequestDTO;
import com.donchung.colame.postservice.utils.response.PostResponseDTO;
import com.donchung.colame.postservice.utils.response.TagResponseDTO;
import com.netflix.discovery.converters.Auto;
import org.apache.coyote.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {
    @Autowired
    private TagMapper tagMapper;

    public PostResponseDTO toResponseDto(Post post) {
        PostResponseDTO responseDTO = new PostResponseDTO();
        BeanUtils.copyProperties(post, responseDTO);
        List<TagResponseDTO> tags = post.getTags().stream().map(tag -> tagMapper.toResponseDto(tag)).toList();
        responseDTO.setTags(tags);
        return responseDTO;
    }

    public Post toEntity(PostRequestDTO requestDTO) {
        Post post = new Post();
        post.setContent(requestDTO.getContent());
        post.setTitle(requestDTO.getTitle());
        post.setPostCode(requestDTO.getPostCode());
        post.setSummary(requestDTO.getSummary());
        return post;
    }

    public Post toEntity(Post postOld, PostRequestDTO requestDTO) {
        postOld.setContent(requestDTO.getContent());
        postOld.setTitle(requestDTO.getTitle());
        postOld.setSummary(requestDTO.getSummary());
        postOld.setPostCode(requestDTO.getPostCode());
        return postOld;
    }

}
