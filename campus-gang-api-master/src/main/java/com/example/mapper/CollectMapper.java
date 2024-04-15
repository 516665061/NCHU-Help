package com.example.mapper;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectMapper {

    @Insert("insert into collect (fid, user_id, module) values  (#{fid}, #{userId}, #{module})")
    void insert(Collect collect);

    @Delete("delete from collect where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from collect where user_id = #{userId} and fid = #{fid}")
    Collect selectByUserIdAndFid(@Param("userId") Integer userId, @Param("fid") Integer fid);

    @Select("select count(*) from collect where fid = #{fid}")
    int selectCountByFid(Integer fid);

    @Select("select collect.*, goods.name as goodsName, goods.img as goodsImg " +
            "from collect left join goods on collect.fid = goods.id where collect.user_id = #{userId}" +
            " order by collect.id desc")
    List<Collect> selectAll(Integer userId);

}