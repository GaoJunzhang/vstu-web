package com.garry.zboot.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class OssPage {
    private List<OssItem> summaryList;
    private String nextMarker;
    private Integer maxKeys;
}
