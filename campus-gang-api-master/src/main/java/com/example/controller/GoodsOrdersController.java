package com.example.controller;

import com.example.common.Result;
import com.example.entity.GoodsOrders;
import com.example.service.GoodsOrdersService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 订单信息前端操作接口
 **/
@RestController
@RequestMapping("/goodsOrders")
public class GoodsOrdersController {

    @Resource
    private GoodsOrdersService goodsOrdersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody GoodsOrders goodsOrders) {
        goodsOrdersService.add(goodsOrders);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        goodsOrdersService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        goodsOrdersService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody GoodsOrders goodsOrders) {
        goodsOrdersService.updateById(goodsOrders);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        GoodsOrders goodsOrders = goodsOrdersService.selectById(id);
        return Result.success(goodsOrders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(GoodsOrders goodsOrders) {
        List<GoodsOrders> list = goodsOrdersService.selectAll(goodsOrders);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(GoodsOrders goodsOrders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<GoodsOrders> page = goodsOrdersService.selectPage(goodsOrders, pageNum, pageSize);
        return Result.success(page);
    }

}