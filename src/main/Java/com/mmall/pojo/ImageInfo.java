package com.mmall.pojo;

public class ImageInfo {
    private Long id;
    private String imgUUID;    //访问的UUID
    private int serverId;
    private Long productId; //所属商品ID，用于删除商品的时候顺便进行的数据删除

}
