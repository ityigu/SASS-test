package com.ityi.web.shiro;

import com.ityi.common.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
/*
* 自定义密码比较器
* */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.把token转成UsernamePasswordTokean
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        //2.取出用户名和密码
        String email = uToken.getUsername();
        String password = new String(uToken.getPassword(), 0, uToken.getPassword().length);
        //3.对明文密码加密
        String md5password = Encrypt.md5(password, email);
        //4.取出数据库中的密码（加密后密码）
        String dbpassword = info.getCredentials().toString();
        //5.比较密码
        return super.equals(md5password,dbpassword);//返回值时false时会抛出异常


    }
}
