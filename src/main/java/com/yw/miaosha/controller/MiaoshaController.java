package com.yw.miaosha.controller;

import com.yw.miaosha.domain.MiaoshaOrder;
import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.domain.OrderInfo;
import com.yw.miaosha.result.CodeMsg;
import com.yw.miaosha.result.Result;
import com.yw.miaosha.service.GoodsService;
import com.yw.miaosha.service.MiaoshaService;
import com.yw.miaosha.service.OrderService;
import com.yw.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * GET POST有什么区别
     * GET幂等，不对服务器端数据产生影响（搜索引擎如果调用getURL会影响数据）
     * POST修改数据
     */
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user,
                             @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods =goodsService.getGoodsVoByGoodsId(goodsId);
        if(goods.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀过了（系统中是否已经存在订单）
        MiaoshaOrder order=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(user,goods);

        return Result.success(orderInfo);
    }
}
