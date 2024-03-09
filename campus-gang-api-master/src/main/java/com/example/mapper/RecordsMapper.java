package com.example.mapper;

import com.example.entity.Records;
import java.util.List;

/**
 * 操作records相关数据接口
 */
public interface RecordsMapper {

    /**
     * 新增
     */
    int insert(Records records);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Records records);

    /**
     * 根据ID查询
     */
    Records selectById(Integer id);

    /**
     * 查询所有
     */
    List<Records> selectAll(Records records);

}
