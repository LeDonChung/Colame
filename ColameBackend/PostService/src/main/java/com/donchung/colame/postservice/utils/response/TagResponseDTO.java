package com.donchung.colame.postservice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
    private String id;

    private String name;

    private String code;

    private String background;

    private Boolean status;
}
