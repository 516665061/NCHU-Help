package com.example.mapper;

import com.example.entity.Orders;

import java.util.List;

/**
 * 功能
 * 作者：封延民
 * 日期：2023/12/7 18:03
 */
public interface OrdersMapper {
    /**
     * 新增
     */
    int insert(Orders orders);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Orders orders);

    /**
     * 根据ID查询
     */
    Orders selectById(Integer id);

    /**
     * 查询所有
     */
    List<Orders> selectAll(Orders orders);

    List<Orders> list(String startTime, String endTime);
}
