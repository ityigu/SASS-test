package com.ityi.web.controller.system;


import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Role;
import com.ityi.service.system.RoleService;
import com.ityi.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    public String roleList( @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "5") int sizi){
        PageInfo pageInfo = roleService.findAll(super.getCurrentUserCompanyId(), page, sizi);
        request.setAttribute("page",pageInfo);
        return "system/role/role-list";
    }

    @RequestMapping("toAdd")
    public String toAdd(){
        return "/system/role/role-add";
    }

    @RequestMapping("delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("edit")
    public String edit(Role role){
        if (UtilFuns.isEmpty(role.getId())){
            role.setCompanyId("1");
            roleService.save(role);
        }else {
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "/system/role/role-update";
    }

    @RequestMapping("roleModule")
    public String roleModule(@RequestParam("roleid") String id){
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "/system/role/role-module";
    }

    @RequestMapping("initModuleData")
    public@ResponseBody List<Map> initModuleData(String id){
        //1.查询符合条件的json数据
       List<Map> list = roleService.findRoleModule(id);
       //返回结果
        return list;
    }

    /*
    * 更新角色和模块的对应关系
    * */

    @RequestMapping("updateRoleModule")
    public String updateRoleModule(@RequestParam("roleid") String id , String moduleIds){
        System.out.println("111");
            //1.调用业务层实现更新
        roleService.updateRoleModule(id,moduleIds);
            //2.重定向到角色的列表
        return "redirect:/system/role/list.do";
    }


}
