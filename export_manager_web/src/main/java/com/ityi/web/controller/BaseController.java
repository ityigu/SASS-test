package com.ityi.web.controller;


import com.ityi.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
/*
* 每次获取是都应该在当前线程中获取
* */
    @Autowired//这个注解每次都是重当前线程中获取
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;
    @Autowired
    protected HttpServletResponse response;

    /*
    * 获取当前登录的用户信息（从sission域中获取的）
    * */
    public User getCurrentUser(){
        return (User)session.getAttribute("user");
    }

    /*
    * 获取当前登录用户的企业id
    * */
    public String getCurrentUserCompanyId(){
        User user = (User)session.getAttribute("user");
        return user==null?"":user.getCompanyId();
    }

    /*
    * 获取当前登录的企业名称
    * */
    public String getCurrentUserCompanyName(){
        User user = getCurrentUser();
        return user==null?"":user.getCompanyName();
    }






    //protected String companyId = "1";
    //protected String companyName = "苏泊莱国际贸易有限公司";
    //protected User user;


   /* @ModelAttribute
    public void setReqAndResp(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        this.request = request;
        this.response = response;
        this.session = session;
        //模拟数据
	    //user = (User)session.getAttribute("loginUser");
	    //if(user != null) {
		//    this.companyId = user.getCompanyId();
		//    this.companyName=user.getCompanyName();
	    //}
    }*/
}
