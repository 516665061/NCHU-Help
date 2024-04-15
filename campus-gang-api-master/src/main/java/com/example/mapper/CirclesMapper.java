package com.example.mapper;

import com.example.entity.Circles;
import java.util.List;
/**
 * 功能: 操作circles相关数据接口
 * 作者：封延民
 * 日期：2024/3/22 11:58
 */

public interface CirclesMapper {

    /**
     * 新增
     */
    int insert(Circles circles);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Circles circles);

    /**
     * 根据ID查询
     */
    Circles selectById(Integer id);

    /**
     * 查询所有
     */
    List<Circles> selectAll(Circles circles);

}