package com.gzu.lionsoj.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.gzu.lionsoj.config.CosClientConfig;
import java.io.File;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Classname: CosManager
 * @Description: Cos 对象存储操作
 * @Author: lions
 * @Datetime: 12/29/2023 12:27 AM
 */
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * @Description: 上传对象
     * @param key 唯一键
     * @param localFilePath 本地文件路径
     * @Return: 上传结果
     * @Author: lions
     * @Datetime: 12/29/2023 12:27 AM
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                new File(localFilePath));
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * @Description: 上传对象
     * @param key 唯一键
     * @param file 文件
     * @Return: 上传结果
     * @Author: lions
     * @Datetime: 12/29/2023 12:28 AM
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        return cosClient.putObject(putObjectRequest);
    }
}
