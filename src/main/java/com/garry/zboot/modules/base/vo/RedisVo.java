package com.garry.zboot.modules.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisVo {
    private String key;

    private String value;
}
