package com.example.mapper;

import com.example.entity.Slides;

import java.util.List;

/**
 * 功能
 * 作者：封延民
 * 日期：2024/2/22 22:44
 */
public interface SlidesMapper {
    /**
     * 新增
     */
    int insert(Slides slides);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Slides slides);

    /**
     * 根据ID查询
     */
    Slides selectById(Integer id);

    /**
     * 查询所有
     */
    List<Slides> selectAll(Slides slides);
}
