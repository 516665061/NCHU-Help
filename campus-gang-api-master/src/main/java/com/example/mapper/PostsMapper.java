package com.example.mapper;

import com.example.entity.Lost;
import com.example.entity.Posts;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作posts相关数据接口
 */
public interface PostsMapper {

    /**
     * 新增
     */
    int insert(Posts posts);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Posts posts);

    /**
     * 根据ID查询
     */
    Posts selectById(Integer id);

    /**
     * 查询所有
     */
    List<Posts> selectAll(Posts posts);

    List<Posts> selectFrontAll(Posts posts);

    @Update("update posts set read_count = read_count + 1 where id = #{id}")
    void updateCount(Integer id);

    List<Posts> selectNewFive(Posts posts);
    List<Posts> selectHotFive(Posts posts);
}