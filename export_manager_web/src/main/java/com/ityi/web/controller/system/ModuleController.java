package com.ityi.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Module;
import com.ityi.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    /*
    * 查询所有部门,根据当前登录用户id,由于当前没有获取id，所以id先写死
    * */
    @RequestMapping("list")
    public String list(HttpServletRequest request,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int sizi){
        PageInfo pageInfo = moduleService.findAll("1",page,sizi);
        request.setAttribute("page",pageInfo);
        return "system/module/module-list";
    }

    //前往新增页面
    @RequestMapping("toAdd")
    public String toAdd(HttpServletRequest request){
        //1.查询所有部门，不能带分页
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("menus",moduleList);
        return "system/module/module-add";
    }

    //保存或者更新的方法
    @RequestMapping("edit")
    public String edit(Module module){

        //判断是否有ID有就是保存，没有就是更新
        if (UtilFuns.isEmpty(module.getId())){
            moduleService.save(module);
        }else {
            //更新
            //生成id
            Module dbModule = moduleService.findById(module.getId());
            moduleService.update(dbModule);
        }
        return "redirect:/system/module/list.do";
    }

    //前往更新页面
    @RequestMapping("toUpdate")
    public String toUpdtate(HttpServletRequest request,String id){
        //根据id查询部门
        Module module = moduleService.findById(id);
        request.setAttribute("module",module);
        List<Module> moduleList = moduleService.findAll();
        request.setAttribute("moduleList",moduleList);
        return "system/module/module-update";
    }
    //更具id删除
    @RequestMapping("delete")
    public String delete(String id){
        //更具id删除
        moduleService.delete(id);
        //重定向到列表
        return "redirect:/system/module/list.do";
    }
}
