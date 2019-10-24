package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping); //在MyBatis添加了 useGenerateKey和keyProperty后，插入完成后会把主键赋值给shipping实例对象
        if (rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return  ServerResponse.createByErrorByMessage("新建地址失败");
    }

    public ServerResponse<String> del(Integer userId,Integer shippingId){

        int rowCount = shippingMapper.deleteByPrimaryKeyUserId(userId, shippingId);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return  ServerResponse.createByErrorByMessage("删除地址失败");

    }

    public ServerResponse<String> update(Integer userId, Shipping shipping){
        shipping.setUserId(userId); //当前登录的userId
        int rowCount = shippingMapper.updateByPrimaryKeyUserId(shipping);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return  ServerResponse.createByErrorByMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByPrimaryKeyUserId(userId,shippingId);
        if (shipping == null){
            return ServerResponse.createByErrorByMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("更新地址成功",shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);


    }

}