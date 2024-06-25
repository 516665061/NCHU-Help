package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.OrdersPropertyEnum;
import com.example.common.enums.RecordsTypeEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.GoodsOrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class GoodsOrdersService {

    @Resource
    private GoodsOrdersMapper goodsOrdersMapper;
    @Resource
    GoodsService goodsService;
    @Resource
    AddressService addressService;
    @Resource
    UserService userService;
    /**
     * 新增
     */
    public void add(GoodsOrders goodsOrders) {
        Integer goodsId = goodsOrders.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        goodsOrders.setGoodsName(goods.getName());
        goodsOrders.setGoodsImg(goods.getImg());
        goodsOrders.setAcceptId(goods.getUserId());  //卖家用户ID
        goodsOrders.setPrice(goods.getPrice());
        goodsOrders.setCategory(goods.getCategory());

        Account currentUser = TokenUtils.getCurrentUser();
        goodsOrders.setUserId(currentUser.getId());  //下单人的ID
        goodsOrders.setStatus(OrderStatusEnum.NOTPAY.getValue()); // 订单默认是待支付
        goodsOrders.setOrderNo(IdUtil.getSnowflakeNextIdStr()); // 订单号
        goodsOrders.setTime(DateUtil.now());
        goodsOrdersMapper.insert(goodsOrders);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        goodsOrdersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsOrdersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(GoodsOrders goodsOrders) {
        if (OrderStatusEnum.CANCEL.getValue().equals(goodsOrders.getStatus())) {

        } else if (OrderStatusEnum.NO_SEND.getValue().equals(goodsOrders.getStatus())) {
            Account currentUser = TokenUtils.getCurrentUser();
            BigDecimal account = currentUser.getAccount();
            BigDecimal price = goodsOrders.getPrice();
            if (price.compareTo(account) > 0) {
                throw new CustomException(ResultCodeEnum.ACCOUNT_LIMIT_ERROR);
            }
            // 更新账户余额
            currentUser.setAccount(account.subtract(price));
            userService.updateById((User) currentUser);
            Integer goodsId = goodsOrders.getGoodsId();
            Goods goods = goodsService.selectById(goodsId);
            goods.setSaleStatus("下架");
            goodsService.updateById(goods);
            goodsOrders.setStatus(OrderStatusEnum.NO_SEND.getValue());
            goodsOrders.setPayTime(DateUtil.now());
            //  记录收支明细
            RecordsService.addRecord(currentUser,"购买" + goodsOrders.getGoodsName(),price, RecordsTypeEnum.OUT.getValue());
        }else if (OrderStatusEnum.NO_ARRIVE.getValue().equals(goodsOrders.getStatus())) {

        } else if (OrderStatusEnum.NO_RECEIVE.getValue().equals(goodsOrders.getStatus())) {
            goodsOrders.setArriveTime(DateUtil.now());
        } else if (OrderStatusEnum.DONE.getValue().equals(goodsOrders.getStatus())) {
            Integer saleId = goodsOrders.getAcceptId();
            User user = userService.selectById(saleId);
            user.setAccount(user.getAccount().add(goodsOrders.getPrice()));
            userService.updateById(user);
            // 记录收支明细
            RecordsService.addRecord(user,"出售" + goodsOrders.getGoodsName(), goodsOrders.getPrice(), RecordsTypeEnum.INCOME.getValue());
        }
        goodsOrdersMapper.updateById(goodsOrders);
    }

    /**
     * 根据ID查询
     */
    public GoodsOrders selectById(Integer id) {
        GoodsOrders goodsOrders = goodsOrdersMapper.selectById(id);
        String address = addressService.selectById(goodsOrders.getTargetId()).getAddress();
        goodsOrders.setAddress(address);
        Address targetAddress = addressService.selectById(goodsOrders.getTargetId());
        goodsOrders.setTargetAddress(targetAddress);
        goodsOrders.setSalesName(userService.selectById(goodsOrders.getAcceptId()).getName());
        return goodsOrders;
    }

    /**
     * 查询所有
     */
    public List<GoodsOrders> selectAll(GoodsOrders goodsOrders) {
        List<GoodsOrders> goodsOrdersList = goodsOrdersMapper.selectAll(goodsOrders);
        for (GoodsOrders o:goodsOrdersList){
            Date date = new Date();
            int minutes = (int)DateUtil.between(DateUtil.parseDateTime(o.getTime()),date, DateUnit.MINUTE);
            o.setMinutes(minutes);
        }
        return goodsOrdersMapper.selectAll(goodsOrders);
    }

    /**
     * 分页查询
     */
    public PageInfo<GoodsOrders> selectPage(GoodsOrders goodsOrders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsOrders> list = goodsOrdersMapper.selectAll(goodsOrders);
        for (GoodsOrders l : list){
            l.setTargetAddress(addressService.selectById(l.getTargetId()));
            l.setSalesName(userService.selectById(l.getAcceptId()).getName());
        }
        return PageInfo.of(list);
    }

    public List<GoodsOrders> list(String startTime, String endTime) {
        return goodsOrdersMapper.list(startTime, endTime);
    }
}