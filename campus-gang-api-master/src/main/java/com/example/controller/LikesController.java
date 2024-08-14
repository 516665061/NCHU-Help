package com.example.controller;

import com.example.common.Result;
import com.example.entity.Likes;
import com.example.service.LikesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 功能
 * 作者：荒
 * 日期：2024/3/27 17:24
 */
@RestController
@RequestMapping("/likes")
public class LikesController {

    @Resource
    private LikesService likesService;

    @PostMapping("/add")
    public Result add(@RequestBody Likes likes){
        likesService.add(likes);
        return Result.success();
    }
}
