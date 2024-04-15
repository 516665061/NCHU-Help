package com.example.controller;

import com.example.common.Result;
import com.example.entity.Evaluate;
import com.example.service.EvaluateService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 评论表前端操作接口
 **/
@RestController
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Evaluate evaluate) {
        evaluateService.add(evaluate);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        evaluateService.deleteById(id);
        return Result.success();
    }

    /**
     * 递归删除
     */
    @DeleteMapping("/deleteDeep/{id}")
    public Result deleteDepp(@PathVariable Integer id) {
        evaluateService.deleteDeep(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        evaluateService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Evaluate evaluate) {
        evaluateService.updateById(evaluate);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Evaluate evaluate = evaluateService.selectById(id);
        return Result.success(evaluate);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Evaluate evaluate) {
        List<Evaluate> list = evaluateService.selectAll(evaluate);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Evaluate evaluate,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Evaluate> page = evaluateService.selectPage(evaluate, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectTree/{fid}/{module}")
    public Result selectTree(@PathVariable Integer fid, @PathVariable String module) {
        List<Evaluate> evaluateList = evaluateService.selectTree(fid, module);
        return Result.success(evaluateList);
    }

    @GetMapping("/selectCount/{fid}/{module}")
    public Result selectCount(@PathVariable Integer fid, @PathVariable String module) {
        Integer count = evaluateService.selectCount(fid, module);
        return Result.success(count);
    }

}