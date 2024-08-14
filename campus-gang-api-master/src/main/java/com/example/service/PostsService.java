package com.example.service;

/**
 * 功能：帖子业务处理
 * 作者：荒
 * 日期：2024/3/31 22:29
 */

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.GoodsStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Lost;
import com.example.entity.Posts;
import com.example.entity.User;
import com.example.mapper.PostsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 帖子业务处理
 **/
@Service
public class PostsService {

    @Resource
    private PostsMapper postsMapper;

    /**
     * 新增
     */
    public void add(Posts posts) {
        posts.setTime(DateUtil.now());  //当前的最新的时间
        Account currentUser = TokenUtils.getCurrentUser();
        posts.setUserId(currentUser.getId());
        posts.setStatus(GoodsStatusEnum.NOT_AUDIT.value);
        postsMapper.insert(posts);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        postsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            postsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Posts posts) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            posts.setStatus(GoodsStatusEnum.NOT_AUDIT.value);
        }
        postsMapper.updateById(posts);
    }

    /**
     * 根据ID查询
     */
    public Posts selectById(Integer id) {
        return postsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Posts> selectAll(Posts posts) {
        return postsMapper.selectAll(posts);
    }

    /**
     * 分页查询
     */
    public PageInfo<Posts> selectPage(Posts posts, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectAll(posts);
        return PageInfo.of(list);
    }

    /**
     * 前台分页查询
     */
    public PageInfo<Posts> selectFrontPage(Posts posts, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectFrontAll(posts);
        return PageInfo.of(list);
    }

    public void updateCount(Integer id) {
        postsMapper.updateCount(id);
    }

    public List<Posts> selectNewFive(Posts posts) {
        List<Posts> lists = postsMapper.selectNewFive(posts);
        return lists;
    }

    public List<Posts> selectHotFive(Posts posts) {
        List<Posts> lists = postsMapper.selectHotFive(posts);
        return lists;
    }
}
