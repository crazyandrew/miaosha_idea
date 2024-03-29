package com.yw.miaosha.service;

import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.domain.OrderInfo;
import com.yw.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //order_info miaosha_order
        return orderService.createOrder(user,goods);
    }
}
