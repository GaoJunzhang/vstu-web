package com.garry.zboot.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* class_name: Captcha
* package: com.garry.zboot.common.vo
* describe: 验证码对象
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 10:03
**/
@Data
public class Captcha implements Serializable{

    @ApiModelProperty(value = "验证码id")
    private String captchaId;

    @ApiModelProperty(value = "验证码")
    private String code;
}
