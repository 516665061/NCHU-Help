package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Slides;
import com.example.mapper.SlidesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能
 * 作者：封延民
 * 日期：2024/2/22 22:40
 */
@Service
public class SlidesService {
    @Resource
    private SlidesMapper slidesMapper;

    /**
     * 新增
     */
    public void add(Slides slides) {
        slidesMapper.insert(slides);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        slidesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            slidesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Slides slides) {
        slidesMapper.updateById(slides);
    }

    /**
     * 根据ID查询
     */
    public Slides selectById(Integer id) {
        return slidesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Slides> selectAll(Slides slides) {
        return slidesMapper.selectAll(slides);
    }

    /**
     * 分页查询
     */
    public PageInfo<Slides> selectPage(Slides slides, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Slides> list = slidesMapper.selectAll(slides);
        return PageInfo.of(list);
    }
}
