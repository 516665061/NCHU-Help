package com.example.mapper;
import com.example.entity.Goods;
import org.apache.ibatis.annotations.Update;

import java.util.List;
/**
 * 功能: 操作goods相关数据接口
 * 作者：荒
 * 日期：2024/3/22 8:15
 */
public interface GoodsMapper {

    /**
     * 新增
     */
    int insert(Goods goods);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Goods goods);

    /**
     * 根据ID查询
     */
    Goods selectById(Integer id);

    /**
     * 查询所有
     */
    List<Goods> selectAll(Goods goods);

    /**
     * 查询所有
     */
    List<Goods> selectFrontAll(Goods goods);

    void updateReadCount(Integer id);
}