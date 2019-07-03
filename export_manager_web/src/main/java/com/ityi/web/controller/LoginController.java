package com.ityi.web.controller;


import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Module;
import com.ityi.domain.system.User;
import com.ityi.service.system.ModuleService;
import com.ityi.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LoginController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /*
    * 登录功能，shiro
    * */
    @GetMapping("/login")
    @ResponseBody
    public String login(String email,String password) {

        //判断当前用户是否已经认证过
        User user = (User) session.getAttribute("user");
        if (user != null){
            //已经认证过
            return "1";
        }

        //1.判断是否有email
        if (UtilFuns.isEmpty(email)){
            //前往登录页面
            return "0";
        }
        try {
            //2.获取shiro中的Subject
            Subject subject = SecurityUtils.getSubject();
            //3.获取用户的令牌
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            //4.调用登录方法
            subject.login(token);//如果用户名不匹配或者密码不匹配都会抛出异常
            //5.获取当前用户信息
             user = (User) subject.getPrincipal();
            //6.登录成功，存入会话域
            session.setAttribute("user", user);
            //7.根据用户id获取用户菜单
            List<Module> moduleList = moduleService.findUserModules(user);
            //8.将用户菜单存入会话域中
            session.setAttribute("modules", moduleList);
            //9.前往主页面
            return "1";
        }catch (Exception e){
            //设置异常返回的信息
            //前往登录页面
            return "0";
        }
    }


    @RequestMapping("/main")
    public String main(){
       return "home/main";
    }



    /*@RequestMapping("/login")
    @ResponseBody
    public ModelAndView login(String email, String password) {
       ModelAndView modelAndView=new ModelAndView();
       modelAndView.addObject("view","home/main");
       modelAndView.setViewName("home/main");
        //判断当前用户是否已经认证过
        User user = (User) session.getAttribute("user");
        if (user != null){
            modelAndView.setViewName("home/main");
            //已经认证过
            return modelAndView;
        }

        //1.判断是否有email
        if (UtilFuns.isEmpty(email)){
            //前往登录页面
            modelAndView.setViewName("redirect:/login.jsp");
            return modelAndView;
        }
        try {
            //2.获取shiro中的Subject
            Subject subject = SecurityUtils.getSubject();
            //3.获取用户的令牌
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            //4.调用登录方法
            subject.login(token);//如果用户名不匹配或者密码不匹配都会抛出异常
            //5.获取当前用户信息
            user = (User) subject.getPrincipal();
            //6.登录成功，存入会话域
            session.setAttribute("user", user);
            //7.根据用户id获取用户菜单
            List<Module> moduleList = moduleService.findUserModules(user);
            //8.将用户菜单存入会话域中
            session.setAttribute("modules", moduleList);
            //9.前往主页面
            modelAndView.setViewName("home/main");
            return modelAndView;
        }catch (Exception e){
            //设置异常返回的信息
            request.setAttribute("error","用户名或密码错误");
            //前往登录页面
            modelAndView.setViewName("forward:/login.jsp");
            return modelAndView;
        }
    }*/


    /*
    * 登录功能，传统方法
    * *//*
	@RequestMapping("/login")
	public String login(String email,String password) {
	    //1.判断是否有emial，如果没有的话，就认为是首次启动服务
        if(UtilFuns.isEmpty(email)){
            //前往登录页面
            return "redirect:login.jsp";
        }
        //2.使用邮箱查询用户
        User user = userService.findByEmial(email);
        //3.判断是否有此用户
        if (user == null){
            request.setAttribute("error","用户名或密码不存在");
            return "forward:/login.jsp";
        }
        //4.判读用户密码是否正确
        if(!user.getPassword().equals(password)){
            request.setAttribute("error","用户名或密码不存在");
            return "forward:/login.jsp";
        }
        //5.登陆成功，存入会话域
        session.setAttribute("user",user);
        //6.根据用户id获取用户的菜单
        List<Module> moduleList = moduleService.findUserModules(user);
        //7.存入绘画域
        session.setAttribute("modules",moduleList);
        //8.前往主页面
		return "home/main";
	}*/

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
