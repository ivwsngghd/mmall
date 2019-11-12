package com.mmall.service;

import com.mmall.BaseTest;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

public class ServiceTestCategory extends BaseTest {

    @Autowired
    private CategoryMapper categoryMapper;


    @Test
    public void test(){

        List<Category> categoryList = categoryMapper.selectCategoryChildByParentId(100001);

        Iterator<Category> categoryIterator = categoryList.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            System.out.println(category.getId());
            System.out.println(category.getName());
            System.out.println(category.getStatus());
            System.out.println(category.getParentId());
            System.out.println();

        }

    }

}
