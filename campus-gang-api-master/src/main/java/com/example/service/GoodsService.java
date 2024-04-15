package com.example.service;

/**
 * 功能
 * 作者：封延民
 * 日期：2024/3/22 8:14
 */

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.GoodsStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Goods;
import com.example.entity.Likes;
import com.example.mapper.CollectMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 二手商品业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private CollectMapper collectMapper;
    /**
     * 新增
     */
    public void add(Goods goods) {
        goods.setDate(DateUtil.today());
        Account currentUser = TokenUtils.getCurrentUser();
        goods.setUserId(currentUser.getId());
        goods.setStatus(GoodsStatusEnum.NOT_AUDIT.value);
        goods.setReadCount(0);
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            goods.setStatus(GoodsStatusEnum.NOT_AUDIT.value);
        }
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        Account currentUser = TokenUtils.getCurrentUser();
        Likes likes = likesMapper.selectByUserIdAndFid(currentUser.getId(),id);
        goods.setUserLikes(likes != null);
        int likesCount = likesMapper.selectCountByFid(id);
        goods.setLikesCount(likesCount);

        Collect collect = collectMapper.selectByUserIdAndFid(currentUser.getId(),id);
        goods.setUserCollect(collect != null);
        int collectCount = collectMapper.selectCountByFid(id);
        goods.setCollectCount(collectCount);
        return goods;
    }

    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            goods.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    /**
     * 前台分页查询
     */
    public PageInfo<Goods> selectFrontPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectFrontAll(goods);
        return PageInfo.of(list);
    }

    public void updateReadCount(Integer id) {
        goodsMapper.updateReadCount(id);
    }
}