package com.example.mapper;

import com.example.entity.GoodsOrders;
import java.util.List;

/**
 * 操作orders相关数据接口
 */
public interface GoodsOrdersMapper {

    /**
     * 新增
     */
    int insert(GoodsOrders goodsOrders);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(GoodsOrders goodsOrders);

    /**
     * 根据ID查询
     */
    GoodsOrders selectById(Integer id);

    /**
     * 查询所有
     */
    List<GoodsOrders> selectAll(GoodsOrders goodsOrders);

    List<GoodsOrders> list(String startTime, String endTime);
}