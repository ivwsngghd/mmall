package com.mmall.pojo;

public class ImageServerInfo {
    private int id;
    private String url;         //前缀
    private String ftpIp;       //ftp传输用的ip地址
    private String ftpUserName; //ftp传输用的权限账户
    private String ftpPass;     //ftp传输用的权限账户的密码

    private Long maxPicAmount;
    private Long curPicAmount;
    private short usableFlag; // 1:可用 0:不可用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Long getMaxPicAmount() {
        return maxPicAmount;
    }

    public void setMaxPicAmount(Long maxPicAmount) {
        this.maxPicAmount = maxPicAmount;
    }

    public Long getCurPicAmount() {
        return curPicAmount;
    }

    public void setCurPicAmount(Long curPicAmount) {
        this.curPicAmount = curPicAmount;
    }

    public short getUsableFlag() {
        return usableFlag;
    }

    public void setUsableFlag(short usableFlag) {
        this.usableFlag = usableFlag;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public void setFtpPass(String ftpPass) {
        this.ftpPass = ftpPass;
    }
}
