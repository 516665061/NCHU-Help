package com.example.service;

import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CollectService {

    @Resource
    CollectMapper collectMapper;

    public void add(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        Integer fid = collect.getFid();
        Collect dbCollect = collectMapper.selectByUserIdAndFid(userId, fid);
        if (dbCollect != null) {
            collectMapper.deleteById(dbCollect.getId());
        } else {
            collect.setUserId(userId);
            collectMapper.insert(collect);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    /**
     * 分页查询
     */
    public PageInfo<Collect> selectPage(Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(userId);
        return PageInfo.of(list);
    }
}