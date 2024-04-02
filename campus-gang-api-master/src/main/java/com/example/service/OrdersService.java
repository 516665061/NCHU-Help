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
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能
 * 作者：封延民
 * 日期：2023/12/7 17:35
 */
@Service
public class OrdersService {
    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private AddressService addressService;

    @Resource
    private UserService userService;

    @Resource
    private CertificationService certificationService;

    @Resource
    private GoodsService goodsService;
    /**
     * 新增
     */
    public void add(Orders orders) {
        orders.setProperty(OrdersPropertyEnum.GOODS.getValue());
        Integer goodsId = orders.getGoodsId();
        Goods goods = goodsService.selectById(goodsId);
        orders.setName(goods.getName());
        orders.setImg(goods.getImg());
        orders.setAcceptId(goods.getUserId());  //卖家用户ID
        orders.setPrice(goods.getPrice());

        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());  //下单人的ID
        orders.setStatus(OrderStatusEnum.NOTPAY.getValue()); // 订单默认是待支付
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr()); // 订单号
        orders.setTime(DateUtil.now());
        ordersMapper.insert(orders);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    @Transactional
    public void updateById(Orders orders) {
        if (OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty())){
            if (OrderStatusEnum.NO_COMMENT.getValue().equals(orders.getStatus())) {
                // 用户确认收货了
                //  打钱
                Integer acceptId = orders.getAcceptId();
                User user = userService.selectById(acceptId);
                user.setAccount(user.getAccount().add(orders.getPrice()));
                userService.updateById(user);
                // 记录收支明细
                RecordsService.addRecord(user,"接单" + orders.getName(), orders.getPrice(), RecordsTypeEnum.INCOME.getValue());
            } else if (OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus())) {
                //用户取消订单了
                //取消订单后需要归还用户金额
                User user = userService.selectById(orders.getUserId());
                user.setAccount(user.getAccount().add(orders.getPrice()));
                userService.updateById(user);
                //  打钱
                RecordsService.addRecord(user,"取消订单" + orders.getName(), orders.getPrice(), RecordsTypeEnum.CANCEL.getValue());
            } else if (OrderStatusEnum.NO_RECEIVE.getValue().equals(orders.getStatus())) {
                orders.setArriveTime(DateUtil.now());
            }
        }else if (OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty())) {
            if (OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus())) {

            } else if (OrderStatusEnum.NO_SEND.getValue().equals(orders.getStatus())) {
                Account currentUser = TokenUtils.getCurrentUser();
                BigDecimal account = currentUser.getAccount();
                BigDecimal price = orders.getPrice();
                if (price.compareTo(account) > 0) {
                    throw new CustomException(ResultCodeEnum.ACCOUNT_LIMIT_ERROR);
                }
                // 更新账户余额
                currentUser.setAccount(account.subtract(price));
                userService.updateById((User) currentUser);
                orders.setStatus(OrderStatusEnum.NO_SEND.getValue());
                orders.setAcceptTime(DateUtil.now());
                //  记录收支明细
                RecordsService.addRecord(currentUser,"购买" + orders.getName(),price, RecordsTypeEnum.OUT.getValue());
            }else if (OrderStatusEnum.NO_RECEIVE.getValue().equals(orders.getStatus())) {

            }else if (OrderStatusEnum.DONE.getValue().equals(orders.getStatus())) {
                Integer acceptId = orders.getAcceptId();
                User user = userService.selectById(acceptId);
                user.setAccount(user.getAccount().add(orders.getPrice()));
                userService.updateById(user);
                // 记录收支明细
                RecordsService.addRecord(user,"出售" + orders.getName(), orders.getPrice(), RecordsTypeEnum.INCOME.getValue());
            }
        }

        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        Address address = addressService.selectById(orders.getAddressId());
        orders.setAddress(address);
        Address targetAddress = addressService.selectById(orders.getTargetId());
        orders.setTargetAddress(targetAddress);

        Certification certification = certificationService.selectByUserId(orders.getAcceptId());
        orders.setCertification(certification);
        return orders;
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        List<Orders> ordersList = ordersMapper.selectAll(orders);
        for (Orders o:ordersList){
            Date date = new Date();
            int minutes = (int)DateUtil.between(DateUtil.parseDateTime(o.getTime()),date, DateUnit.MINUTE);
            o.setMinutes(minutes);
        }
        return ordersList;
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        for (Orders l : list){
            l.setTargetAddress(addressService.selectById(l.getTargetId()));
        }
        return PageInfo.of(list);
    }

    public void addOrder(Orders orders) {
        orders.setProperty(OrdersPropertyEnum.ERRAND.getValue());
        Account currentUser = TokenUtils.getCurrentUser();
        BigDecimal account = currentUser.getAccount();
        BigDecimal price = orders.getPrice();
        if (price.compareTo(account) > 0) {
            throw new CustomException(ResultCodeEnum.ACCOUNT_LIMIT_ERROR);
        }
        // 更新账户余额
        currentUser.setAccount(account.subtract(price));
        userService.updateById((User) currentUser);

        orders.setUserId(currentUser.getId());
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr());  // 设置唯一的订单编号
        orders.setStatus(OrderStatusEnum.NO_ACCEPT.getValue());
        orders.setTime(DateUtil.now());
        ordersMapper.insert(orders);

        //  记录收支明细
        RecordsService.addRecord(currentUser,"下单" + orders.getName(),price, RecordsTypeEnum.OUT.getValue());
    }

    public void accept(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();  // 骑手用户
        orders.setAcceptId(currentUser.getId());
        orders.setAcceptTime(DateUtil.now());
        orders.setStatus(OrderStatusEnum.NO_ARRIVE.getValue());
        this.updateById(orders);
    }


    public List<Orders> list(String startTime, String endTime) {
        return ordersMapper.list(startTime, endTime);
    }
}
