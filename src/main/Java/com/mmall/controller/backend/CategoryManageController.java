package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    /**
     *
     * @param session 用于判断身份
     * @param categoryName 需要添加的分类的名字
     * @param parenId 是否根节点
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0")int parenId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }


        ServerResponse resp = iUserService.checkAdminRole(user);
        //检查一下是否为管理员
        if (resp.isSuccess()){
            return iCategoryService.addCategory(categoryName,parenId);

        }else {
            return resp;
        }
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }

        ServerResponse resp = iUserService.checkAdminRole(user);
        //检查一下是否为管理员
        if (resp.isSuccess()){
            //更新categorey
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return resp;
        }

    }


    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }

        ServerResponse resp = iUserService.checkAdminRole(user);
        if (resp.isSuccess()){
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return resp;
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }

        ServerResponse resp = iUserService.checkAdminRole(user);
        if (resp.isSuccess()){
            //查询当前节点的ID和递归子节点的id
            //父节点-儿子节点->孙子
        return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else {
            return resp;
        }
    }


}

