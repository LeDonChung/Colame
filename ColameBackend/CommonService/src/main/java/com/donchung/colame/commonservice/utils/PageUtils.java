package com.donchung.colame.commonservice.utils;

import com.donchung.colame.commonservice.dto.PageDTO;
import com.donchung.colame.commonservice.dto.SortName;
import com.donchung.colame.commonservice.dto.SorterDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class PageUtils {
    private int length;

    private int pageIndex;

    private int pageSize;

    private List<Object> dataSource;

    private SorterDTO sorter;

    public static PageUtils getPage(PageDTO pageDTO, List<Object> results, int count) {
        return PageUtils
                .builder()
                .length(count)
                .sorter(pageDTO.getSorter())
                .pageSize(pageDTO.getPageSize())
                .pageIndex(pageDTO.getPageIndex())
                .dataSource(Arrays.asList(results.toArray()))
                .build();
    }
}
