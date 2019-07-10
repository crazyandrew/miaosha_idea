package com.yw.miaosha.controller;

import com.yw.miaosha.domain.MiaoshaOrder;
import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.domain.OrderInfo;
import com.yw.miaosha.result.CodeMsg;
import com.yw.miaosha.service.GoodsService;
import com.yw.miaosha.service.MiaoshaService;
import com.yw.miaosha.service.OrderService;
import com.yw.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(Model model, MiaoshaUser user,
                             @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if(user==null){
            return "login";
        }
        //判断库存
        GoodsVo goods =goodsService.getGoodsVoByGoodsId(goodsId);
        if(goods.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否秒杀过了（系统中是否已经存在订单）
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";
    }
}
