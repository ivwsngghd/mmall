package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 新增或者更新商品 (保存)
     *
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest httpServletRequest, Product product) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //增加产品业务逻辑
//            return iProductService.saveOrUpdateProduct(product);
//
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
        return iProductService.saveOrUpdateProduct(product);

    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse set_sales_status(HttpServletRequest httpServletRequest, Integer productId, Integer status) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //设置商品状态
//            return iProductService.setSaleStatus(productId, status);
//
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
        return iProductService.setSaleStatus(productId, status);

    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(HttpServletRequest httpServletRequest, Integer productId) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //设置商品状态
//            return iProductService.manageProductDetail(productId);
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
        return iProductService.manageProductDetail(productId);

    }


    /**
     * 获取
     *
     * @param pageNum  页号
     * @param pageSize 页面条数
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //设置商品状态
//            return iProductService.getProductList(pageNum, pageSize);
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
        return iProductService.getProductList(pageNum, pageSize);

    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest httpServletRequest, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //设置商品状态
//            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    /**
     * 上传文件
     * @param file 注意value = upload_file 要和前台的 name 属性一致，否则无法上传；
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
            String path = request.getSession().getServletContext().getRealPath("upload");   //获取要上传的路径
            String targetFileName = iFileService.upload(file, path);                        //文件流和上传的路径
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName; //把访问路径返回给前端；

            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
//        } else {
//            return ServerResponse.createByErrorByMessage("无权限操作");
//        }
    }

    /**
     * 该接口用于富文本(文字图片的集合)的上传
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpServletRequest httpServletRequest, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        if (StringUtils.isEmpty(loginToken) || StringUtils.isEmpty(userJsonStr)) {
            resultMap.put("success", false);
            resultMap.put("msg", "请先登录管理员！");
            return resultMap;
        }
        /*
            使用过simditor富文本传输格式要求：
            {
                "success"   : true/false,
                "msg"       :  "error message"
                "file_path" : "[real file path]"
            }
         */
        User user = JsonUtil.stringToObj(userJsonStr, User.class);
        if (user != null && iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");   //获取路径
            String targetFileName = iFileService.upload(file, path);                        //上传
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName; //返回访问名字
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");   //前端插件对后台的返回报文有一定的要求；
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作!");
            return resultMap;
        }
    }
}
