package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    String upload(MultipartFile file, String path);

    void delFile(String []fileNames);
    void delFile(String fileNames);
    void delFile(String []fileNames,String remotePath);

}
