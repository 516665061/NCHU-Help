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
import com.example.entity.*;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private AuditorService auditorService;

    @Resource
    private UserService userService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private GoodsOrdersService goodsOrdersService ;

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
        } else if (RoleEnum.AUDITOR.name().equals(account.getRole())) {
            account = auditorService.login(account);
        }else if (RoleEnum.USER.name().equals(account.getRole())) {
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
        } else if (RoleEnum.AUDITOR.name().equals(account.getRole())) {
            auditorService.updatePassword(account);
        }else if (RoleEnum.USER.name().equals(account.getRole())) {
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
        List<GoodsOrders> goodsOrdersList = goodsOrdersService.list(startTime, endTime);
        Set<String> times1 = goodsOrdersList.stream().map(goodsOrders -> goodsOrders.getTime().substring(0, 10)).collect(Collectors.toSet());
        Set<String> times2 = list.stream().map(orders -> orders.getTime().substring(0, 10)).collect(Collectors.toSet());
        // 合并两个集合
        Set<String> times = Stream.concat(times1.stream(), times2.stream()).collect(Collectors.toSet());
        List<String> timeList = CollUtil.newArrayList(times);
        timeList.sort(Comparator.naturalOrder());
        List<Dict> lineList = new ArrayList<>();
        for (String time : timeList) {
            //商品销售额
            BigDecimal goodsSum = goodsOrdersList.stream().filter(goodsOrders -> !(OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(goodsOrders.getStatus())) && time.equals(goodsOrders.getTime().substring(0, 10))).
                    map(GoodsOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            //跑腿销售额
            BigDecimal taskSum = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()))).
                    map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
             //商品销量
            Long goodsAmount = goodsOrdersList.stream().filter(goodsOrders -> !(OrderStatusEnum.CANCEL.getValue().equals(goodsOrders.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders.getStatus())) && time.equals(goodsOrders.getTime().substring(0, 10)) ).count();

            //跑腿销量
            Long errandAmount = list.stream().filter(orders -> time.equals(orders.getTime().substring(0, 10)) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()))).count();

            BigDecimal allSum =goodsSum.add(taskSum);//总销售额
            Long allAmount = goodsAmount + errandAmount;//总销量
            Dict dict = Dict.create();
            Dict line = dict.set("time", time).set("allSum", allSum).set("goodsSum",goodsSum).set("taskSum",taskSum).set("allAmount", allAmount).set("goodsAmount",goodsAmount).set("errandAmount",errandAmount);
            lineList.add(line);
        }

        //跑腿
        List<Dict> pieList = new ArrayList<>();
        Set<String> types = list.stream().map(Orders::getType).collect(Collectors.toSet());
        for (String type : types) {
            Long share = list.stream().filter(orders -> type.equals(orders.getType()) && !(OrderStatusEnum.NO_ACCEPT.getValue().equals(orders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(orders.getStatus()))).count();
            Dict dict = Dict.create();
            Dict pie = dict.set("name", type).set("value", share);
            pieList.add(pie);
        }

        //商品
        List<Dict> hoopList = new ArrayList<>();
        Set<String> categories = goodsOrdersList.stream().map(GoodsOrders::getCategory).collect(Collectors.toSet());
        for (String category : categories) {
            Long share = goodsOrdersList.stream().filter(goodsOrders -> category.equals(goodsOrders.getCategory()) && !(OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders.getStatus()) || OrderStatusEnum.CANCEL.getValue().equals(goodsOrders.getStatus()))).count();
            Dict dict = Dict.create();
            Dict hoop = dict.set("name", category).set("value", share);
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
        GoodsOrders goodsOrders = new GoodsOrders();
        List<Orders> ordersList = ordersService.selectAll(orders);
        List<GoodsOrders> goodsOrdersList = goodsOrdersService.selectAll(goodsOrders);
        double allDeal = ordersList.stream().filter(orders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue() + goodsOrdersList.stream().filter(goodsOrders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(goodsOrders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders1.getStatus()))).map(GoodsOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue();
        orderMap.put("allDeal", allDeal);
        double yesterdayDeal = ordersList.stream().filter(orders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(orders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).map(Orders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue() + goodsOrdersList.stream().filter(goodsOrders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(goodsOrders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(goodsOrders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders1.getStatus()))).map(GoodsOrders::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).doubleValue();
        orderMap.put("yesterdayDeal", yesterdayDeal);

        Map<String, Long> postMap = new HashMap<String, Long>();
        Posts posts = new Posts();
        List<Posts> postsList = postsService.selectAll(posts);
        postMap.put("allPost", Long.valueOf(postsList.size()));
        postMap.put("yesterdayPost", postsList.stream().filter(posts1 -> DateUtil.yesterday().toString().substring(0,10).equals(posts1.getTime().substring(0,10))).count());

        Map<String, Long> completeMap = new HashMap<String, Long>();
        long allComplete = ordersList.stream().filter(orders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).count() + goodsOrdersList.stream().filter(goodsOrders1 -> !(OrderStatusEnum.CANCEL.getValue().equals(goodsOrders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders1.getStatus()))).count();
        long yesterdayComplete = ordersList.stream().filter(orders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(orders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(orders1.getStatus()) || OrderStatusEnum.NO_ACCEPT.getValue().equals(orders1.getStatus()))).count() + goodsOrdersList.stream().filter(goodsOrders1 -> DateUtil.yesterday().toString().substring(0, 10).equals(goodsOrders1.getTime().substring(0, 10)) && !(OrderStatusEnum.CANCEL.getValue().equals(goodsOrders1.getStatus()) || OrderStatusEnum.NOTPAY.getValue().equals(goodsOrders1.getStatus()))).count();
        completeMap.put("allComplete", allComplete);
        completeMap.put("yesterdayComplete", yesterdayComplete);

        Map<String, Map> res = new HashMap<String, Map>();
        res.put("user", userMap);
        res.put("order", orderMap);
        res.put("post", postMap);
        res.put("complete", completeMap);
        return Result.success(res);
    }
}
