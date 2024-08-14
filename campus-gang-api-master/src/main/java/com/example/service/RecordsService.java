package com.example.service;

/**
 * 功能
 * 作者：荒
 * 日期：2023/12/10 13:04
 */

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Records;
import com.example.mapper.RecordsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 收支明细业务处理
 **/
@Service
public class RecordsService implements InitializingBean {

    @Resource
    private RecordsMapper recordsMapper;

    private static RecordsMapper staticRecordMapper;

    /**
     * 收支明细存储
     */
    public static void addRecord(Account currentUser,String content, BigDecimal money, String type) {
        Records records = new Records();
        if (currentUser == null){
            currentUser = TokenUtils.getCurrentUser();
        }
        records.setUserId(currentUser.getId());
        records.setTime(DateUtil.now());
        records.setContent(content);
        records.setMoney(money);
        records.setType(type);
        staticRecordMapper.insert(records);  // 插入数据库
    }

    /**
     * 新增
     */
    public void add(Records records) {
        recordsMapper.insert(records);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        recordsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            recordsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Records records) {
        recordsMapper.updateById(records);
    }

    /**
     * 根据ID查询
     */
    public Records selectById(Integer id) {
        return recordsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Records> selectAll(Records records) {
        return recordsMapper.selectAll(records);
    }

    /**
     * 分页查询
     */
    public PageInfo<Records> selectPage(Records records, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Records> list = recordsMapper.selectAll(records);
        return PageInfo.of(list);
    }

    //复制静态变量
    @Override
    public void afterPropertiesSet() throws Exception {
        staticRecordMapper = recordsMapper;
    }
}