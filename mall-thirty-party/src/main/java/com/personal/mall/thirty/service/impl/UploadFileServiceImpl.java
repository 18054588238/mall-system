package com.personal.mall.thirty.service.impl;

import com.personal.common.utils.R;
import com.personal.mall.thirty.service.UploadFileService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileServiceImpl implements UploadFileService {
    @Value("${minio.bucketName}")
    private String bucketName;
    private final MinioClient minioClient;
    @Override
    public R uploadFileByMinio(byte[] bytes, String fileName, String contentType) {
        if (bytes == null || bytes.length == 0 || StringUtils.isBlank(fileName)) {
            System.out.println("文件流和文件名不能为null");
        }
        if (contentType == null) {
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        fileName = UUID.randomUUID()+"_"+fileName;
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(byteArrayInputStream,bytes.length,-1)
                            .contentType(contentType)
                            .build()
            );
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(10*60)
                            .build()
            );
            return R.ok().put("url",url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
