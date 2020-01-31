package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     *
     * @param file 待传输文件流
     * @param path ftp服务器的根下的相对路径
     * @return
     */
    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf('.')+1);   //获取扩展名 substring 算左 不算右，加一可以去掉'.'
        // TODO 这里要进行处理信息，上传至哪一个服务器？然后后面上传成功后会在MySQL进行记录；
        String uploadFileName = UUID.randomUUID().toString() + '.' + fileExtensionName; //生成UUIDName;
        logger.info("开始上传文件，上传文件的文件名:{},上传路径:{},新文件名:{}",fileName,path,uploadFileName); //写入日志文件

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);  //赋予可写权限，譬如Tomcat部署在Linux需要相应权限
            fileDir.mkdirs();       //全路径生成
//            fileDir.mkdir();    //不会生成中间的路径
        }
        File targetFile = new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件上传成功

            //文件上传至FTP服务器  (需要另外处理)
            // TODO 在Util中设置对应的服务器IP和信息，在此处进行上传的时候，利用数据库表进行图片信息的记录，IP和对应的访问域名；
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到FTP服务器里
            //传完之后，删除upload下面的文件  (位于tomcat目录下)
            targetFile.delete();


        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }

        return targetFile.getName();
    }

    public static void main(String[] args) {
        String fileName = "abc.jpg";
        System.out.println(fileName.substring(fileName.lastIndexOf('.')));
    }

}
