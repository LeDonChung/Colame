package com.donchung.colame.postservice.utils.response;

import com.donchung.colame.postservice.POJO.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO extends Auditable<String> {
    private String id;

    private String postCode;

    private String title;

    private String summary;

    private String content;

    private Integer viewer;

    private Collection<TagResponseDTO> tags;

    private boolean status;

    private String username;
}
