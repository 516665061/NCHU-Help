package com.example.service;

import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能
 * 作者：封延民
 * 日期：2024/3/27 17:37
 */
@Service
public class LikesService {
    @Resource
    private LikesMapper likesMapper;

    public void add(Likes likes){
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = currentUser.getId();
        Integer fid = likes.getFid();
        Likes dbLikes = likesMapper.selectByUserIdAndFid(userId,fid);
        if (dbLikes != null){
            likesMapper.deleteById(dbLikes.getId());
        }else {
            likes.setUserId(userId);
            likesMapper.insert(likes);
        }
    }
}
