package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * 旅游分类
 */
public interface CategoryService {

    /**
     * 查询所有旅游分类信息
     * @return
     */
    List<Category> findAll();
}
