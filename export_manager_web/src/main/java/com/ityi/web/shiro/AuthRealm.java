package com.ityi.web.shiro;

import com.ityi.domain.system.Module;
import com.ityi.domain.system.User;
import com.ityi.service.system.ModuleService;
import com.ityi.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    /*
     * 授权
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.取出当前认证成功的用户
//        principals.getPrimaryPrincipal();//当我们只有一个用户认证成功时，它就是主要的主体内容。所以可以直接得到。
        //当我们有多个到时候，可以使用以下方法获取
        User user = (User) principals.fromRealm(this.getName()).iterator().next();
        //2.使用用户获取当前的菜单
        List<Module> moduleList = moduleService.findUserModules(user);
        //3.创建封装模块名称的集合，要去除重复
        Set<String> permissions = new HashSet<>();
        //4.遍历用户的模块集合
        for (Module module : moduleList) {
            //5.添加到权限set中
        permissions.add(module.getName());
        }
        //6.创建返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //7.把权限信息加入到返回值中
        info.setStringPermissions(permissions);
//        info.addStringPermissions(permissions);//给权限集合赋值，当原来没有的时候就是set，当原来已经有了权限时，是追加
        //8.返回
        return info;
    }

    /*
     * 认证
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.将参数转成UsernamePasswordToken
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        //2.获取登录用户名（这里用户名也就是邮箱）
        String email = uToken.getUsername();
        //3.根据邮箱去数据库查询用户
        User user = userService.findByEmial(email);
        //4.判断用户在数据库是否存在
        //用户存在
        if (user != null) {
            //创建返回值对象
            //String thisName = this.getName();
            //System.out.println("this.getName() is "+thisName);
            AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
            //返回
            return info;
        }
        //5.不存在返回null
        //shiro当遇到此方法返回null时，会自动抛出异常
        return null;
    }
}
