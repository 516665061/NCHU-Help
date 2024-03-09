package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.OrdersService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private OrdersService ordersService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        } else {
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        return Result.success(account);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            account.setTime(DateUtil.now());
            userService.register(account);
        } else {
            return Result.error(ResultCodeEnum.ROLE_ERROR);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        return Result.success();
    }

    /**
     * 获取统计图数据
     *
     * @return 动态数据
     */
    @GetMapping("/charts")
    public Result charts(String startTime, String endTime) {
        //包装柱状折线图数据
        List<Orders> list = ordersService.list(startTime, endTime);
        Set<String> times = list.stream().map(orders -> orders.getTime().substring(0, 10)).collect(Collectors.toSet());
        List<String> timeList = CollUtil.newArrayList(times);
        timeList.sort(Comparator.naturalOrder());
        List<Dict> lineList = new ArrayList<>();
        for (String time : timeList) {
            //统计当前日期的所有金额总数和
            Double sum = list.stream().filter(orders -> orders.getTime().substring(0, 10).equals(time)).
                    map(Orders::getPrice).reduce(Double::sum).orElse(0.0);
            Long amount = list.stream().filter(orders -> orders.getTime().substring(0, 10).equals(time) && !(orders.getStatus().equals("待接单") || orders.getStatus().equals("已取消"))).count();
            Double profit = list.stream().filter(orders -> orders.getTime().substring(0, 10).equals(time)).
                    map(Orders::getServiceCharge).reduce(Double::sum).orElse(0.0);
            Dict dict = Dict.create();
            Dict line = dict.set("time", time).set("value", sum).set("amount", amount).set("profit",profit);
            lineList.add(line);
        }

        List<Dict> pieList = new ArrayList<>();
        Set<String> types = list.stream().map(Orders::getType).collect(Collectors.toSet());
        for (String type : types) {
            Long share = list.stream().filter(orders -> orders.getType().equals(type) && !(orders.getStatus().equals("待接单") || orders.getStatus().equals("已取消"))).count();
            Dict dict = Dict.create();
            Dict pie = dict.set("name", type).set("value", share);
            pieList.add(pie);
        }

        Dict res = Dict.create().set("line", lineList).set("pie", pieList);
        return Result.success(res);
    }

    @GetMapping("/panel")
    public Result Panel() {
        Map<String, Long> userMap = new HashMap<String, Long>();
        User user = new User();
        List<User> userList = userService.selectAll(user);
        userMap.put("allUser", Long.valueOf(userList.size()));
        System.out.println(DateUtil.yesterday().toString());
        userMap.put("yesterdayUser", userList.stream().filter(user1 -> user1.getTime().substring(0, 10).equals(DateUtil.yesterday().toString().substring(0, 10))).count());

        Map<String, Double> orderMap = new HashMap<String, Double>();
        Orders orders = new Orders();
        List<Orders> ordersList = ordersService.selectAll(orders);
        orderMap.put("allDeal", ordersList.stream().filter(orders1 -> !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).map(Orders::getPrice).reduce(Double::sum).orElse(0.0));
        orderMap.put("yesterdayDeal", ordersList.stream().filter(orders1 -> orders1.getTime().substring(0, 10).equals(DateUtil.yesterday().toString().substring(0, 10)) && !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).map(Orders::getPrice).reduce(Double::sum).orElse(0.0));

        Map<String, Double> profitMap = new HashMap<String, Double>();
        profitMap.put("allProfit", ordersList.stream().filter(orders1 -> !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).map(Orders::getServiceCharge).reduce(Double::sum).orElse(0.0));
        profitMap.put("yesterdayProfit", ordersList.stream().filter(orders1 -> orders1.getTime().substring(0, 10).equals(DateUtil.yesterday().toString().substring(0, 10)) && !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).map(Orders::getServiceCharge).reduce(Double::sum).orElse(0.0));

        Map<String, Long> completeMap = new HashMap<String, Long>();
        completeMap.put("allComplete", ordersList.stream().filter(orders1 -> !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).count());
        completeMap.put("yesterdayComplete", ordersList.stream().filter(orders1 -> orders1.getTime().substring(0, 10).equals(DateUtil.yesterday().toString().substring(0, 10)) && !(orders1.getStatus().equals("已取消") || orders1.getStatus().equals("待接单"))).count());

        Map<String, Map> res = new HashMap<String, Map>();
        res.put("user", userMap);
        res.put("order", orderMap);
        res.put("profit", profitMap);
        res.put("complete", completeMap);
        return Result.success(res);
    }
}
