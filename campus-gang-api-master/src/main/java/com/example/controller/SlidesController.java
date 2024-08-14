package com.example.controller;

/**
 * 功能
 * 作者：荒
 * 日期：2024/2/22 22:38
 */

import com.example.common.Result;
import com.example.entity.Slides;
import com.example.service.SlidesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/slides")
public class SlidesController {
    @Resource
    private SlidesService slidesService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Slides slides) {
        slidesService.add(slides);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        slidesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        slidesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Slides slides) {
        slidesService.updateById(slides);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Slides slides = slidesService.selectById(id);
        return Result.success(slides);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Slides slides ) {
        List<Slides> list = slidesService.selectAll(slides);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Slides slides,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Slides> page = slidesService.selectPage(slides, pageNum, pageSize);
        return Result.success(page);
    }

}
