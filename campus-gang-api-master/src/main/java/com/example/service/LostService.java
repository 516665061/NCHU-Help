package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Lost;
import com.example.entity.User;
import com.example.mapper.LostMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class LostService {

    @Resource
    private LostMapper lostMapper;

    @Autowired
    private UserMapper userMapper;
    /**
     * 新增
     */
    public void add(Lost lost) {
        lost.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        lost.setUserId(currentUser.getId());
        lostMapper.insert(lost);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        lostMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            lostMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Lost lost) {
        lostMapper.updateById(lost);
    }

    /**
     * 根据ID查询
     */
    public Lost selectById(Integer id) {
        return lostMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Lost> selectAll(Lost lost) {
        return lostMapper.selectAll(lost);
    }

    /**
     * 分页查询
     */
    public PageInfo<Lost> selectPage(Lost lost, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Lost> list = lostMapper.selectAll(lost);
        return PageInfo.of(list);
    }

    public List<Lost> selectNewFour(Lost lost) {
        List<Lost> lists = lostMapper.selectNewFour(lost);
        for (Lost list : lists) {
            User user = userMapper.selectById(lost.getUserId());
            if (ObjectUtil.isNotEmpty(user)){
                lost.setUserName(user.getName());
            }
        }
        return lists;
    }
}