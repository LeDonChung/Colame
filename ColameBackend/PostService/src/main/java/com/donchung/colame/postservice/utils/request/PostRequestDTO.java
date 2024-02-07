package com.donchung.colame.postservice.utils.request;

import com.donchung.colame.postservice.POJO.Auditable;
import com.donchung.colame.postservice.POJO.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO extends Auditable<String> {
    private String id;

    private String postCode;

    private String title;

    private String summary;

    private String content;

    private Integer viewer;

    private Collection<TagRequestDTO> tags;

    private boolean status;

    private String username;
}
