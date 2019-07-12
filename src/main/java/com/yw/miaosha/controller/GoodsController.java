package com.yw.miaosha.controller;

import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.redis.GoodsKey;
import com.yw.miaosha.redis.MiaoshaUserKey;
import com.yw.miaosha.redis.RedisService;
import com.yw.miaosha.result.Result;
import com.yw.miaosha.service.GoodsService;
import com.yw.miaosha.service.MiaoshaUserService;
import com.yw.miaosha.vo.GoodsDetailVo;
import com.yw.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "to_list",produces = "text/html")
    @ResponseBody
    public String toGoodsList(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user){
        model.addAttribute("user",user);
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsList=goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);

        WebContext ctx=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }

        return html;
    }

    @RequestMapping(value = "to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toGoodsDetail2(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user,
                                @PathVariable(value = "goodsId")long goodsId){
        model.addAttribute("user",user);
        //取缓存
        String html=redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);
        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int miaoshaStatus=0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        WebContext ctx=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html=thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    @RequestMapping("detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo>  goodsDetail(Model model, MiaoshaUser user,
                                              @PathVariable(value = "goodsId")long goodsId){

        GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt=goods.getStartDate().getTime();
        long endAt=goods.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int miaoshaStatus=0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo=new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }





}
