package com.yw.miaosha.controller;

import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.redis.MiaoshaUserKey;
import com.yw.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("to_list")
    public String toGoodsList(Model model,MiaoshaUser user){

        model.addAttribute("user",user);
        return "goods_list";
    }




}
