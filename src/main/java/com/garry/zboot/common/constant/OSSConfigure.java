package com.garry.zboot.common.constant;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 张高俊 on 2017/10/26.
 */


public class OSSConfigure {

    //阿里云API的内或外网域名
//    @Value("${aliyun.oos.endpoint}")
    private static String endpoint;
    //阿里云API的密钥Access Key ID
//    @Value("${aliyun.oos.accessKeyId}")
    private static String accessKeyId;
    //阿里云API的密钥Access Key Secret
//    @Value("${aliyun.oos.accessKeySecret}")
    private static String accessKeySecret;
    //阿里云API的bucket名称
//    @Value("${aliyun.oos.bucketName}")
    private static String bucketName;
    //阿里云API的文件夹名称
//    @Value("${aliyun.oos.file}")
    private static String file;
//    @Value("${aliyun.oos.accessUrl}")
    private static String accessUrl;
    public OSSConfigure() throws IOException {

        Properties prop = new Properties();
        prop.load(this.getClass().getClassLoader()
                .getResourceAsStream("oss.properties"));

        endpoint = prop.getProperty("endpoint").trim();
        accessKeyId = prop.getProperty("accessKeyId").trim();
        accessKeySecret = prop.getProperty("accessKeySecret").trim();
        bucketName = prop.getProperty("bucketName").trim();
        accessUrl = prop.getProperty("accessUrl").trim();
        file = prop.getProperty("aliyun.oos.file").trim();

    }

    public OSSConfigure(String endpoint, String accessKeyId,
                        String accessKeySecret, String bucketName, String accessUrl) {

        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.accessUrl = accessUrl;
    }
    public static String getEndpoint() {
        return endpoint;
    }

    public static void setEndpoint(String endpoint) {
        OSSConfigure.endpoint = endpoint;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static void setAccessKeyId(String accessKeyId) {
        OSSConfigure.accessKeyId = accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        OSSConfigure.accessKeySecret = accessKeySecret;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static void setBucketName(String bucketName) {
        OSSConfigure.bucketName = bucketName;
    }

    public static String getFile() {
        return file;
    }

    public static void setFile(String file) {
        OSSConfigure.file = file;
    }

    public static String getAccessUrl() {
        return accessUrl;
    }

    public static void setAccessUrl(String accessUrl) {
        OSSConfigure.accessUrl = accessUrl;
    }
}
