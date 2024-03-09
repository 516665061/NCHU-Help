package com.example.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderStatusEnum;
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
        List<Orders> ordersList = ordersService.selectAll(params);
        for (Orders orders : ordersList){
            String expectTime = orders.getExpectTime();
            DateTime dateTime = DateUtil.parseDateTime(expectTime);
            if (dateTime.before(new Date())){  //超过了5分钟  自动取消
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
                orders.setStatus(OrderStatusEnum.NO_COMMENT.getValue());
                ordersService.updateById(orders);
            }
        }
        log.info("========================订单扫描任务结束========================");
    }

}
