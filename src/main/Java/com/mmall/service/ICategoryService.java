package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    public ServerResponse addCategory(String categoryName, Integer parentId);
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    /**
     * 用于获取平行分类
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);


    /**
     * 递归查询本节点的ID以及孩子节点的ID
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
