package com.garry.zboot.common.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.garry.zboot.common.constant.OSSConfigure;
import com.garry.zboot.common.vo.OssItem;
import com.garry.zboot.common.vo.OssPage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * class_name: OssUtil
 * package: com.garry.zboot.common.utils
 * describe: 阿里oss操作类
 * creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
 * creat_date: 2019/7/17
 * creat_time: 10:32
 **/
public class OssUtil {

    // 单例，只需要建立一次链接
    private static OSSClient client = null;
    // 是否使用另外一套本地账户
//    public static final boolean MINE = false;

    //配置参数
    static ClientConfiguration config() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(100);
        conf.setConnectionTimeout(5000);
        conf.setMaxErrorRetry(3);
        conf.setSocketTimeout(2000);
        return conf;
    }


    public static OSSConfigure ossConfigure() {
        OSSConfigure ossConfigure = null;
        try {
            ossConfigure = new OSSConfigure();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ossConfigure;
    }

    //客户端
    public static OSSClient client() {
        if (client == null) {
            ClientConfiguration conf = config();
            client = new OSSClient(ossConfigure().getEndpoint(), ossConfigure().getAccessKeyId(), ossConfigure().getAccessKeySecret(), conf);
            makeBucket(client, ossConfigure().getBucketName());
        }
        return client;
    }


    //创建Bucket
    public static void makeBucket(String bucketName) {
        OSSClient client = client();
        makeBucket(client, bucketName);
    }


    //创建Bucket
    public static void makeBucket(OSSClient client, String bucketName) {
        boolean exist = client.doesBucketExist(bucketName);
        if (exist) {
            p("The bucket exist.");
            return;
        }
        client.createBucket(bucketName);
    }


    //上传一个文件，InputStream
    public static void uploadFile(InputStream is, String key) {
        OSSClient client = client();
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfigure().getBucketName(), key, is);
        client.putObject(putObjectRequest);
    }


    //上传一个文件，File
    public static void uploadFile(File file, String key) {
        OSSClient client = client();
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfigure().getBucketName(), key, file);
        client.putObject(putObjectRequest);
    }


    //下载一个文件到本地
    public static OSSObject downloadFile(String key) {
        OSSClient client = client();
        GetObjectRequest getObjectRequest = new GetObjectRequest(
                ossConfigure().getBucketName(), key);
        OSSObject object = client.getObject(getObjectRequest);
        return object;
    }


    //上传某个文件到某个目录，key是自动生成的
    public static String uploadFile(MultipartFile file, String dir)
            throws IOException {
        if (null != file && !file.isEmpty() && file.getSize() > 0) {
            QiniuUtil qiniuUtil = new QiniuUtil();
            String key = dir + "/" + qiniuUtil.renamePic(file.getOriginalFilename());
            OssUtil.uploadFile(file.getInputStream(), key);
            return ossConfigure().getAccessUrl() + "/" +key;
        }
        return null;
    }


    //删除某个文件
    public static void delete(String key) {
        try {
            client().deleteObject(ossConfigure().getBucketName(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteKeys(List<String> keys){
        try {
            client().deleteObjects(new DeleteObjectsRequest(ossConfigure().getBucketName()).withKeys(keys));
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    //创建目录，不能以斜杠“/”开头
    public static void makeDir(String keySuffixWithSlash) {
        OSSClient client = client();
        /*
         * Create an empty folder without request body, note that the key must
         * be suffixed with a slash
         */
        if (StringUtils.isEmpty(keySuffixWithSlash)) {
            return;
        }
        if (!keySuffixWithSlash.endsWith("/")) {
            keySuffixWithSlash += "/";
        }
        client.putObject(ossConfigure().getBucketName(), keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
    }


    // 实时的分页查询
    public static OssPage listPage(String dir, String nextMarker,
                                   Integer maxKeys) {
        OSSClient client = client();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfigure().getBucketName());
        if (!StringUtils.isEmpty(dir)) {
            listObjectsRequest.setPrefix(dir);
        }
        if (!StringUtils.isEmpty(nextMarker)) {
            listObjectsRequest.setMarker(nextMarker);
        }
        if (maxKeys != null) {
            listObjectsRequest.setMaxKeys(maxKeys);
        }
        ObjectListing objectListing = client.listObjects(listObjectsRequest);


        List<OSSObjectSummary> summrayList = objectListing.getObjectSummaries();
        List<OssItem> itemList = summaryToItem(summrayList);
        OssPage page = new OssPage();


        String newxNextMarker = objectListing.getNextMarker();
        page.setNextMarker(newxNextMarker);
        page.setSummaryList(itemList);
        return page;
    }


    //把OSS的对象，转换成自己的。因为OSS的对象没有实现Serialiable，不能序列化。
    private static List<OssItem> summaryToItem(
            List<OSSObjectSummary> summaryList) {
        List<OssItem> itemList = new ArrayList<OssItem>();
        for (OSSObjectSummary summary : summaryList) {
            OssItem item = new OssItem();
//                BeanUtils.copyProperties(item, summary);
                item.setLastModified(DateUtil.dateToStrLong(summary.getLastModified()));
                item.setKey(summary.getKey());
                item.setSize(summary.getSize());
                item.setBucketName(summary.getBucketName());
                itemList.add(item);
        }
        return itemList;
    }


    //一次迭代，获得某个目录下的所有文件列表
    public static List<OssItem> listAll(String dir) {
        OSSClient client = client();
        List<OssItem> list = new ArrayList<OssItem>();
        // 查询
        ObjectListing objectListing = null;
        String nextMarker = null;
        final int maxKeys = 1000;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfigure().getBucketName()).withPrefix(dir).withMarker(nextMarker)
                    .withMaxKeys(maxKeys);
            objectListing = client.listObjects(listObjectsRequest);


            List<OSSObjectSummary> summrayList = objectListing
                    .getObjectSummaries();
            List<OssItem> itemList = summaryToItem(summrayList);
            list.addAll(itemList);
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        return list;
    }

    public static void p(Object str) {
        System.out.println(str);
    }


    public static void print(OSSException oe) {
        p("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.");
        p("Error Message: " + oe.getErrorCode());
        p("Error Code:       " + oe.getErrorCode());
        p("Request ID:      " + oe.getRequestId());
        p("Host ID:           " + oe.getHostId());
    }
}
