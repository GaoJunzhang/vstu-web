package com.garry.zboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * class_name: IgnoredUrlsProperties
 * package: com.garry.zboot.config
 * describe: 忽略的url配置
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2019/7/9
 * creat_time: 10:50
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "ignored")
public class IgnoredUrlsProperties {

    private List<String> urls = new ArrayList<>();
}
