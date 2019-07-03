package com.ityi.web.utils;

import com.ityi.domain.system.SysLog;
import com.ityi.domain.system.User;
import com.ityi.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysLogService sysLogService;

    /**
     * 环绕通知
     * pjp.proceed()明确方法触发切入点方法的执行
     */
    @Around("execution(* com.ityi.web.controller.*.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        //1.获取HttpSession对象
        HttpSession session = request.getSession();
        //2.获取当前登录用户
        User user = (User)session.getAttribute("user");
        if (user != null){
            //1.获取pjp的签名
            Signature signature = pjp.getSignature();
            //判断此方法是不是方法签名
            if (signature instanceof MethodSignature) {
                //强转
                MethodSignature methodSignature = (MethodSignature) signature;
                //3.取出此签名对应的方法
                Method method = methodSignature.getMethod();
                //4.判断当前方法是否被@RequestMapping注解修饰了
                boolean hasAnnotated = method.isAnnotationPresent(RequestMapping.class);
                if (hasAnnotated) {
                    //5.取出方法上的注解
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    //6.取出注解name属性的值
                    String name = requestMapping.name();
                    //7.取出方法名称
                    String methodName = method.getName();
                    //8.创建日志对象
                    SysLog sysLog = new SysLog();
                    //9.设置日志属性
                    sysLog.setUserName(user.getUserName());
                    sysLog.setCompanyId(user.getCompanyId());
                    sysLog.setCompanyName(user.getCompanyName());
                    sysLog.setTime(new Date());
                    sysLog.setIp(request.getRemoteAddr());
                    sysLog.setAction(name);
                    sysLog.setMethod(methodName);
                    sysLogService.save(sysLog);
                }
            }
        }
        //定义返回值
        Object rtValue = null;
        try {
            //得到方法执行时所需的参数
            Object[] args = pjp.getArgs();
            //执行方法
             rtValue = pjp.proceed(args);
        } catch (Throwable tl) {
            tl.printStackTrace();
        }
        return rtValue;
    }
}
