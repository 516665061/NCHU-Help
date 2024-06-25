package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Auditor;
import com.example.exception.CustomException;
import com.example.mapper.AuditorMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class AuditorService {

    @Resource
    private AuditorMapper auditorMapper;

    /**
     * 新增
     */
    public void add(Auditor auditor) {
        Auditor dbAuditor = auditorMapper.selectByUsername(auditor.getUsername());
        if (ObjectUtil.isNotNull(dbAuditor)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(auditor.getPassword())) {
            auditor.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(auditor.getName())) {
            auditor.setName(auditor.getUsername());
        }
        auditor.setRole(RoleEnum.AUDITOR.name());
        auditorMapper.insert(auditor);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        auditorMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            auditorMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Auditor auditor) {
        auditorMapper.updateById(auditor);
    }

    /**
     * 根据ID查询
     */
    public Auditor selectById(Integer id) {
        return auditorMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Auditor> selectAll(Auditor auditor) {
        return auditorMapper.selectAll(auditor);
    }

    /**
     * 分页查询
     */
    public PageInfo<Auditor> selectPage(Auditor auditor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Auditor> list = auditorMapper.selectAll(auditor);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Auditor login(Account account) {
        Auditor dbAuditor = auditorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAuditor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAuditor.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbAuditor.getId() + "-" + RoleEnum.AUDITOR.name();
        String token = TokenUtils.createToken(tokenData, dbAuditor.getPassword());
        dbAuditor.setToken(token);
        return dbAuditor;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Auditor auditor = new Auditor();
        BeanUtils.copyProperties(account, auditor);
        add(auditor);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Auditor dbAuditor = auditorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAuditor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbAuditor.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbAuditor.setPassword(account.getNewPassword());
        auditorMapper.updateById(dbAuditor);
    }

}