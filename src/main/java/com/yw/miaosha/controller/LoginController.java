package com.yw.miaosha.controller;

import com.yw.miaosha.result.CodeMsg;
import com.yw.miaosha.result.Result;
import com.yw.miaosha.service.MiaoshaUserService;
import com.yw.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());
        //userService.login(response,loginVo);
        userService.login(response,loginVo);
        return Result.success(true);
    }

}
