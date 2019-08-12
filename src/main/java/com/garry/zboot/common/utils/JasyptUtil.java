package com.garry.zboot.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
* class_name: JasyptUtil
* package: com.garry.zboot.common.utils
* describe: Jasypt生成加密工具类
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 11:07
**/
@Slf4j
public class JasyptUtil {

    /**
     * Jasypt生成加密结果
     * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
     * @param value 待加密值
     * @return
     */
    public static String encyptPwd(String password,String value){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.encrypt(value);
        return result;
    }

    /**
     * 解密
     * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
     * @param value 待解密密文
     * @return
     */
    public static String decyptPwd(String password,String value){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(password));
        String result = encryptor.decrypt(value);
        return result;
    }

    public static SimpleStringPBEConfig cryptor(String password){
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args){

        //加密
/*        System.out.println(encyptPwd("zboot","123456"));
        //解密
        System.out.println(decyptPwd("zboot","kF21ndCjR/PpKHQCXZTdpw=="));*/
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
        String encryptPass = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encryptPass);
    }
}
