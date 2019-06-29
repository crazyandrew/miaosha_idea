package com.yw.miaosha.controller;

import com.yw.miaosha.domain.User;
import com.yw.miaosha.redis.RedisService;
import com.yw.miaosha.redis.UserKey;
import com.yw.miaosha.result.Result;
import com.yw.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    //页面
    @RequestMapping("thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","yw");
        return "hello";
    }

    //rest api
    @RequestMapping("hello")
    @ResponseBody
    public Result<String> hello(Model model){
        model.addAttribute("name","yw");
        return Result.success("hello");
    }

    //rest api
    @RequestMapping("db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user=userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user=redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }

    @RequestMapping("redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user=new User();
        user.setId(1);
        user.setName("11");
        redisService.set(UserKey.getById,""+1,user);
        return Result.success(true);
    }
}
