package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.OrdersPropertyEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.entity.Posts;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.OrdersService;
import com.example.service.PostsService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private PostsService postsService;
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
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
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
            //总销售额
            BigDecimal allSum = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()))).
                    map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            //商品销售额
            BigDecimal goodsSum = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty()) && !(OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()))).
                    map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            //跑腿销售额
            BigDecimal taskSum = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus())) && OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty())).
                    map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            //总销量
            Long allAmount = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()))).count();

             //商品销量
            Long goodsAmount = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()) && OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty())).count();
            //跑腿销量
            Long errandAmount = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty()) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()))).count();

            Dict dict = Dict.create();
            Dict line = dict.set("time", time).set("allSum", allSum).set("goodsSum",goodsSum).set("taskSum",taskSum).set("allAmount", allAmount).set("goodsAmount",goodsAmount).set("errandAmount",errandAmount);
            lineList.add(line);
        }

        List<Dict> pieList = new ArrayList<>();
        Set<String> types = list.stream().filter(orders -> OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty())).map(Orders::getType).collect(Collectors.toSet());
        for (String type : types) {
            Long share = list.stream().filter(orders -> type.equals(orders.getType()) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus())) && OrdersPropertyEnum.ERRAND.getValue().equals(orders.getProperty())).count();
            Dict dict = Dict.create();
            Dict pie = dict.set("name", type).set("value", share);
            pieList.add(pie);
        }

        List<Dict> hoopList = new ArrayList<>();
        Set<String> hTypes = list.stream().filter(orders -> OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty())).map(Orders::getType).collect(Collectors.toSet());
        for (String type : hTypes) {
            Long share = list.stream().filter(orders -> type.equals(orders.getType()) && !(OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus())) && OrdersPropertyEnum.GOODS.getValue().equals(orders.getProperty())).count();
            Dict dict = Dict.create();
            Dict hoop = dict.set("name", type).set("value", share);
            hoopList.add(hoop);
        }

        Dict res = Dict.create().set("line", lineList).set("pie", pieList).set("hoop",hoopList);
        return Result.success(res);
    }

    @GetMapping("/panel")
    public Result Panel() {
        Map<String, Long> userMap = new HashMap<String, Long>();
        User user = new User();
        List<User> userList = userService.selectAll(user);
        userMap.put("allUser", Long.valueOf(userList.size()));
        System.out.println(DateUtil.yesterday().toString());
        userMap.put("yesterdayUser", userList.stream().filter(user1 -> DateUtil.yesterday().toString().substring(0, 10).equals(user1.getTime().substring(0, 10))).count());

        Map<String, Double> orderMap = new HashMap<String, Double>();
        Orders orders = new Orders();
        List<Orders> ordersList = ordersService.selectAll(orders);
        orderMap.put("allDeal", ordersList.stream().filter(orders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()))).map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue());
        orderMap.put("yesterdayDeal", ordersList.stream().filter(orders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(orders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(orders.getStatus()))).map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue());

        Map<String, Long> postMap = new HashMap<String, Long>();
        Posts posts = new Posts();
        List<Posts> postsList = postsService.selectAll(posts);
        postMap.put("allPost", Long.valueOf(postsList.size()));
        postMap.put("yesterdayPost", postsList.stream().filter(posts1 -> DateUtil.yesterday().toString().substring(0,10).equals(posts1.getTime().substring(0,10))).count());

        Map<String, Long> completeMap = new HashMap<String, Long>();
        completeMap.put("allComplete", ordersList.stream().filter(orders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).count());
        completeMap.put("yesterdayComplete", ordersList.stream().filter(orders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(orders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).count());

        Map<String, Map> res = new HashMap<String, Map>();
        res.put("user", userMap);
        res.put("order", orderMap);
        res.put("post", postMap);
        res.put("complete", completeMap);
        return Result.success(res);
    }
}
