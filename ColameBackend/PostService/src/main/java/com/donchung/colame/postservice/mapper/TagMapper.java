package com.donchung.colame.postservice.mapper;

import com.donchung.colame.postservice.POJO.Tag;
import com.donchung.colame.postservice.utils.request.TagRequestDTO;
import com.donchung.colame.postservice.utils.response.TagResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagMapper {
    public TagResponseDTO toResponseDto(Tag tag) {
        TagResponseDTO response = new TagResponseDTO();
        BeanUtils.copyProperties(tag, response);
        return response;
    }

    public Tag toEntity(TagRequestDTO request) {
        Tag tag = new Tag();
        tag.setCode(request.getCode());
        tag.setBackground(request.getBackground());
        tag.setName(request.getName());
        return tag;
    }

    public Tag toEntity(Tag tagOld, TagRequestDTO request) {
        tagOld.setName(request.getName());
        tagOld.setCode(request.getCode());
        tagOld.setBackground(request.getBackground());
        return tagOld;
    }
}
