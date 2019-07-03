package com.ityi.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Dept;
import com.ityi.domain.system.Role;
import com.ityi.domain.system.User;
import com.ityi.service.system.DeptService;
import com.ityi.service.system.RoleService;
import com.ityi.service.system.UserService;
import com.ityi.web.controller.BaseController;
import com.ityi.web.utils.MailUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/system/user/")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    private MailUtil mailUtil;

    @RequestMapping(value = "list",name = "用户名称")
    public String list(HttpServletRequest request, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int sizi){
        PageInfo pageInfo = userService.findAll("1", page, sizi);
        request.setAttribute("page",pageInfo);
        return "system/user/user-list";
    }

    //前往新增页面
    @RequestMapping("toAdd")
    public String toAdd(HttpServletRequest request){
        //查询所有用户所属部门，不带分页
        List<Dept> deptList = deptService.findAll("1");
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    //保存或者更新的方法
    @RequestMapping("edit")
    public String edit(User user)  {
        //保存
        //设置企业id
        user.setCompanyId("1");
        //设置企业名称
        user.setCompanyName("苏泊莱国际贸易有限公司");
        //判断是否有ID有就是更新，没有就是保存
        if (UtilFuns.isEmpty(user.getId())){
            userService.save(user);
            String concent = "亲爱的"+user.getUserName()+"您的saas已经注册成功"+"账号为"+user.getUserName()+"密码为："+user.getPassword()+"详情请登录www.baidu.com查看";
            //保存成功，发送邮件
            try {
                mailUtil.send("2664308952@qq.com","SAAS用户注册成功",concent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            //更新
            //生成id
            User dbDept = userService.findById(user.getId());
            dbDept.setCompanyName(user.getCompanyName());
            BeanUtils.copyProperties(user,dbDept,new String[]{"companyId","companyName"});
            userService.update(dbDept);
        }
        return "redirect:/system/user/list.do";
    }

    //前往更新页面
    @RequestMapping("toUpdate")
    public String toUpdtate(HttpServletRequest request,String id){
        //根据id查询部门
        User user = userService.findById(id);
        /*//设置企业id
        user.setCompanyId("1");
        //设置企业名称
        user.setCompanyName("苏泊莱国际贸易有限公司");*/
        request.setAttribute("user",user);
        List<Dept> userList = deptService.findAll("1");
        request.setAttribute("deptList",userList);
        return "system/user/user-update";
    }

    //更具id删除
    @RequestMapping("delete")
    public String delete(String id){
        //更具id删除
        userService.delete(id);
        //重定向到列表
        return "redirect:/system/user/list.do";
    }

    //前往给用户分配角色页面
    @RequestMapping("roleList")
    public String roleList(String id){
        //1.查询当前用户
        User user = userService.findById(id);
        //查询所有角色
        List<Role> roleList = roleService.findAll(getCurrentUserCompanyId());
        //3.查询当前用户所具备的角色
        List<String> userRoleList = userService.findUserRole(id);
        //4.把所需数据存入请求域中
        request.setAttribute("user",user);
        request.setAttribute("roleList",roleList);
        request.setAttribute("userRoleStr",userRoleList);
        //返回
        return "system/user/user-role";
    }


    @RequestMapping("changeRole")
    public String changeRole(@RequestParam("userid") String id , String[] roleIds){
        //1.调用service实现changUserRole(改变用户角色)
        userService.changeUserRole(id,roleIds);
        //2.重定向到用户列表页面
        return "redirect:/system/user/list.do";
    }

}
