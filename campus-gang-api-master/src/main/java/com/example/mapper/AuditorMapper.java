package com.example.mapper;

import com.example.entity.Auditor;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作auditor相关数据接口
*/
public interface AuditorMapper {

    /**
      * 新增
    */
    int insert(Auditor auditor);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Auditor auditor);

    /**
      * 根据ID查询
    */
    Auditor selectById(Integer id);

    /**
      * 查询所有
    */
    List<Auditor> selectAll(Auditor auditor);

    @Select("select * from auditor where username = #{username}")
    Auditor selectByUsername(String username);
}