package com.personal.mall.thirty.controller;

import com.personal.common.utils.R;
import com.personal.mall.thirty.service.FileStorageService;
import com.personal.mall.thirty.service.UploadFileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/thirty")
public class uploadController {
    private final UploadFileService uploadFileService;
    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public R uploadFile(@RequestParam("file") MultipartFile file) { // 返回统一响应对象
        try {
            String fileUrl = storageService.uploadFile(file);
            return R.ok().put("data", fileUrl); // 封装成功响应
        } catch (Exception e) {
            return R.error("上传失败: " + e.getMessage()); // 封装错误响应
        }
    }
}
