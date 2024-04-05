package com.example.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.OrdersPropertyEnum;
import com.example.entity.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class OrdersSchedule {
    private static Logger log = LoggerFactory.getLogger(OrdersSchedule.class);

    @Resource
    OrdersService ordersService;

    @Resource
    UserService userService;

    @Scheduled(fixedRate = 60000)
    public void task() {
        log.info("========================订单扫描任务开始========================");
        Orders params = new Orders();
        params.setStatus(OrderStatusEnum.NO_ACCEPT.getValue());
        params.setProperty(OrdersPropertyEnum.ERRAND.getValue());
        List<Orders> ordersList = ordersService.selectAll(params);
        for (Orders orders : ordersList){
            String expectTime = orders.getExpectTime();
            DateTime dateTime = DateUtil.parseDateTime(expectTime);
            if (dateTime.before(new Date())){  //超过了期望时间  自动取消
                orders.setStatus(OrderStatusEnum.CANCEL.getValue());
                ordersService.updateById(orders);
            }
        }
        Orders accomplish = new Orders();
        accomplish.setStatus(OrderStatusEnum.NO_RECEIVE.getValue());
        ordersList = ordersService.selectAll(accomplish);
        for (Orders orders : ordersList){
            String arriveTime = orders.getArriveTime();
            DateTime dateTime = DateUtil.parseDateTime(arriveTime);
            long seconds = DateUtil.between(dateTime, new Date(), DateUnit.SECOND);// 计算下单的时间跟当前时间的间隔秒数
            if (seconds > 604800){  //超过了7天  自动收货
                if (OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty())){
                    orders.setStatus(OrderStatusEnum.NO_COMMENT.getValue());
                }else if (OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty())){
                    orders.setStatus(OrderStatusEnum.DONE.getValue());
                }
                ordersService.updateById(orders);
            }
        }

        Orders goods = new Orders();
        goods.setStatus(OrderStatusEnum.NOTPAY.getValue());
        goods.setProperty(OrdersPropertyEnum.GOODS.getValue());
        ordersList = ordersService.selectAll(goods);
        for (Orders orders : ordersList){
            String time = orders.getTime();
            DateTime dateTime = DateUtil.parseDateTime(time);
            long seconds = DateUtil.between(dateTime, new Date(), DateUnit.SECOND);// 计算下单的时间跟当前时间的间隔秒数
            if (seconds > 86400){  //超过了24小时  自动取消订单
                orders.setStatus(OrderStatusEnum.CANCEL.getValue());
                ordersService.updateById(orders);
            }
        }
        log.info("========================订单扫描任务结束========================");
    }

}
