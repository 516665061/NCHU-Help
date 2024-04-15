package com.example.mapper;

import com.example.entity.Evaluate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作evaluate相关数据接口
 */
public interface EvaluateMapper {

    /**
     * 新增
     */
    int insert(Evaluate evaluate);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Evaluate evaluate);

    /**
     * 根据ID查询
     */
    Evaluate selectById(Integer id);

    /**
     * 查询所有
     */
    List<Evaluate> selectAll(Evaluate evaluate);

    List<Evaluate> selectRoot(@Param("fid") Integer fid, @Param("module") String module);

    List<Evaluate> selectByRootId(Integer rootId);

    Integer selectCount(@Param("fid") Integer fid, @Param("module") String module);


    List<Evaluate> selectByPid(Integer pid);
}