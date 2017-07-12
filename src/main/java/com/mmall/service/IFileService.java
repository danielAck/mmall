package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 张柏桦 on 2017/7/11.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
