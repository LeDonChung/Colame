package com.donchung.colame.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private int length;

    private int pageIndex;

    private int pageSize;

    private SorterDTO sorter;

}
