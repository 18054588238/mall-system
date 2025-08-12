package com.personal.mall.thirty.service;

import com.personal.common.utils.R;

public interface UploadFileService {
    R uploadFileByMinio(byte[] bytes, String fileName, String contentType);
}
