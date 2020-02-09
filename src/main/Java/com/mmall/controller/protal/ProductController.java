package com.mmall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailRESTful(@PathVariable Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /**
     * @param keyword    模糊查询的关键字 非必须
     * @param categoryId 分类ID 非必须
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {


        return iProductService.getProductByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

    /**
     *
     * 配置的required属性false表示非必须属性；默认的占位符不能为空
     * 此处keyword和categoryId不能同时为空
     * @return
     */
    @RequestMapping(value = {"/list/{pageNum}/{pageSize}/{orderBy}/categoryId/{categoryId}/keyword/{keyword}","/list/{pageNum}/{pageSize}/{orderBy}/keyword/{keyword}","/list/{pageNum}/{pageSize}/{orderBy}/categoryId/{categoryId}"}, method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "keyword", required = false) String keyword,
                                                @PathVariable(value = "categoryId", required = false) Integer categoryId,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize,
                                                @PathVariable(value = "orderBy") String orderBy) {

        if (pageNum == null || pageNum < 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_asc";
        }
            return iProductService.getProductByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
