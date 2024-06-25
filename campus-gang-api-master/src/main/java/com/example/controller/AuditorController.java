package com.example.controller;

import com.example.common.Result;
import com.example.entity.Auditor;
import com.example.service.AuditorService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@RestController
@RequestMapping("/auditor")
public class AuditorController {

    @Resource
    private AuditorService auditorService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Auditor auditor) {
        auditorService.add(auditor);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        auditorService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        auditorService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Auditor auditor) {
        auditorService.updateById(auditor);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Auditor auditor = auditorService.selectById(id);
        return Result.success(auditor);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Auditor auditor ) {
        List<Auditor> list = auditorService.selectAll(auditor);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Auditor auditor,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Auditor> page = auditorService.selectPage(auditor, pageNum, pageSize);
        return Result.success(page);
    }

}