package com.garry.zboot.common.exception;

import lombok.Data;

/**
* class_name: ZbootException
* package: com.garry.zboot.common.exception
* describe: 全局异常
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 9:37
**/
@Data
public class ZbootException extends RuntimeException {
    private String msg;

    public ZbootException(String msg){
        super(msg);
        this.msg = msg;
    }
}
