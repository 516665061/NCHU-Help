package com.example.mapper;

import com.example.entity.Lost;

import java.util.List;

/**
 * 操作notice相关数据接口
*/
public interface LostMapper {

    /**
      * 新增
    */
    int insert(Lost lost);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Lost lost);

    /**
      * 根据ID查询
    */
    Lost selectById(Integer id);

    /**
      * 查询所有
    */
    List<Lost> selectAll(Lost lost);

    List<Lost> selectNewFour(Lost lost);
}