package com.yw.miaosha.config;

import com.yw.miaosha.domain.MiaoshaUser;
import com.yw.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    MiaoshaUserService userService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz=methodParameter.getParameterType();
        return clazz== MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletResponse response=nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String paramToken=request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken=getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        return userService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
