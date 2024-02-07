package com.donchung.colame.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SorterDTO {
    private SortName sortName;
    private String sortBy;
}
