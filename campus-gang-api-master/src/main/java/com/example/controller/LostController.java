package com.example.controller;

import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Lost;
import com.example.service.LostService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/lost")
public class LostController {

    @Resource
    private LostService lostService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Lost lost) {
        lostService.add(lost);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        lostService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        lostService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Lost lost) {
        lostService.updateById(lost);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Lost lost = lostService.selectById(id);
        return Result.success(lost);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Lost lost ) {
        List<Lost> list = lostService.selectAll(lost);
        return Result.success(list);
    }

    @GetMapping("/selectNewFour")
    public Result selectNewFour(Lost lost) {
        List<Lost> list = lostService.selectNewFour(lost);
        return Result.success(list);
    }



    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Lost lost,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Lost> page = lostService.selectPage(lost, pageNum, pageSize);
        return Result.success(page);
    }

}