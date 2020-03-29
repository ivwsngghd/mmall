package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 用于处理FTP文件上传的问题
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;

    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 唯一对外开放的用于上传文件的方法upload
     *
     * @param fileList 待上传的文件列表
     * @return 文件是否上传成功了；
     * @throws IOException 上传文件失败后的异常处理交给上层进行处理；
     *                     目前存放于Tomcat目录中
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        //TODO 独立上传的路径目录
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器，结束上传，上传结果:{}", result);

        return result;
    }

    /**
     * 分布式图片存储改良的上传方法：
     * 即上层指定了ftp的IP，用户，服务器，密码，和存储的相对路径
     */
    public static boolean uploadFile(List<File> fileList, String ftpIp, String ftpUser, String ftpPass, String remotePath) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        //根目录相对路径
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器，结束上传，上传结果:{}", result);
        return result;
    }

    /**
     * 上传文件的路径
     *
     * @param remotePath
     * @param fileList
     * @return
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fis = null;
        //连接FTP服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                System.out.println("开始上传文件");
                ftpClient.changeWorkingDirectory(remotePath);   //创建ftp服务器的路径
                ftpClient.setBufferSize(1024);                  //
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//                ftpClient.enterLocalActiveMode(); //外部链接
                ftpClient.enterLocalPassiveMode();  //内部链接  大多数允许
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
                uploaded = true;
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


    /**
     * 用于检测是否连接FTP服务器成功
     *
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean success = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port); //port 可不写，不写默认21
            ftpClient.login(user, pwd);
            success = true;
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }
        return success;
    }

    /**
     * 用于查询有的文件数
     *
     * @param remotePath 要查询的远程目录文件夹
     * @return
     */
    public static String[] imgPathSelect(String remotePath) {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);

        try {
            return ftpUtil.imgSelect(remotePath);
        } catch (IOException e) {
            logger.info("ftp连接异常:{}", e);
        }
        return null;

    }

    private String[] imgSelect(String remotePath) throws IOException {
        logger.info("开始连接ftp服务器");
        if (connectServer(ftpIp, this.port, ftpUser, ftpPass)) {
            try {
                logger.info("开始查询img路径下的文件");
                ftpClient.enterLocalPassiveMode();
                return ftpClient.listNames(remotePath);
            } catch (IOException e) {
                logger.info("图片文件查询异常");
            } finally {
                ftpClient.disconnect();
            }
        }
        return null;
    }

    public static void deleteImgFiles(String[] imgNames) {
        deleteImgFiles(imgNames,"img");
    }

    public static void deleteImgFiles(String[] imgNames,String remotePath) {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        ftpUtil.connectServer(ftpUtil.getIp(), ftpUtil.getPort(), ftpUtil.getUser(), ftpUtil.getPwd());
        FTPClient ftpClient = ftpUtil.getFtpClient();
        try {
            logger.info("开始连接ftp服务器，进行图片清除操作");
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(remotePath);
            for (String pathname : imgNames) {
                ftpClient.deleteFile(pathname);
            }
            logger.info("图片清理工作完成");
        } catch (IOException e) {
            logger.info("图片文件查询异常");
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.info("ftpClient关闭时异常:{}", e);
            }
        }
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
