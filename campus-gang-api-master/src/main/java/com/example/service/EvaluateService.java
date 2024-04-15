package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Evaluate;
import com.example.mapper.EvaluateMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 评论表业务处理
 **/
@Service
public class EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 新增
     */
    public void add(Evaluate evaluate) {
        evaluate.setTime(DateUtil.now());
        evaluateMapper.insert(evaluate);

        Integer id = evaluate.getId();
        // 更新root_id
        if (evaluate.getPid() == null) {
            evaluate.setRootId(id);
        } else {
            Evaluate parentEvaluate = evaluateMapper.selectById(evaluate.getPid());
            evaluate.setRootId(parentEvaluate.getRootId());
        }
        this.updateById(evaluate);
    }

    public void deleteById(Integer id) {
        this.deepDelete(id);
    }

    /**
     * 递归删除
     */
    public void deleteDeep(Integer id) {
        this.deepDelete(id);
    }

    private void deepDelete(Integer pid) {
        List<Evaluate> children = evaluateMapper.selectByPid(pid);
        evaluateMapper.deleteById(pid);
        for (Evaluate child : children) {
            this.deepDelete(child.getId());
        }
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            evaluateMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Evaluate evaluate) {
        evaluateMapper.updateById(evaluate);
    }

    /**
     * 根据ID查询
     */
    public Evaluate selectById(Integer id) {
        return evaluateMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Evaluate> selectAll(Evaluate evaluate) {
        return evaluateMapper.selectAll(evaluate);
    }

    /**
     * 分页查询
     */
    public PageInfo<Evaluate> selectPage(Evaluate evaluate, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Evaluate> list = evaluateMapper.selectAll(evaluate);
        return PageInfo.of(list);
    }

    public List<Evaluate> selectTree(Integer fid, String module) {
        List<Evaluate> rootList = evaluateMapper.selectRoot(fid, module);
        for (Evaluate root : rootList) {
            Integer rootId = root.getRootId();
            List<Evaluate> children = evaluateMapper.selectByRootId(rootId);
            root.setChildren(children);
        }
        return rootList;
    }

    public Integer selectCount(Integer fid, String module) {
        return evaluateMapper.selectCount(fid, module);
    }
}