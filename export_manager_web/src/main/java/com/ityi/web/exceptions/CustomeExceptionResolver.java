package com.ityi.web.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 自定义异常解析器
* */
@Component
public class CustomeExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //1.创建返回值
        ModelAndView mv = new ModelAndView();
        //2.设置视图名称
        mv.setViewName("error");
        //3.判断是系统异常还是业务异常
        if (ex instanceof CustomeException){
            mv.addObject("errorMsg",ex.getMessage());
        }else {
            //输出到控制台
            ex.printStackTrace();
            mv.addObject("errorMsg","服务忙。。");
        }
        return mv;
}
}
