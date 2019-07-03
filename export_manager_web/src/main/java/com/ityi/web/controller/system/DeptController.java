package com.ityi.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Dept;
import com.ityi.service.system.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /*
    * 查询所有部门,根据当前登录用户id,由于当前没有获取id，所以id先写死
    * */
    @RequestMapping("list")
    public String list(HttpServletRequest request,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int sizi){
        PageInfo pageInfo = deptService.findAll("1", page, sizi);
        request.setAttribute("page",pageInfo);
        return "system/dept/dept-list";
    }

    //前往新增页面
    @RequestMapping("toAdd")
    public String toAdd(HttpServletRequest request){
        //1.查询所有部门，不能带分页
        List<Dept> deptList = deptService.findAll("1");
        request.setAttribute("deptList",deptList);
        return "system/dept/dept-add";
    }

    //保存或者更新的方法
    @RequestMapping("edit")
    public String edit(Dept dept){
        //保存
        //设置企业id
        dept.setCompanyId("1");
        //设置企业名称
        dept.setCompanyName("苏泊莱国际贸易有限公司");
        //判断是否有ID有就是保存，没有就是更新
        if (UtilFuns.isEmpty(dept.getId())){
            deptService.save(dept);
        }else {
            //更新
            //生成id
            Dept dbDept = deptService.findById(dept.getId());
            dbDept.setCompanyName(dept.getCompanyName());
            BeanUtils.copyProperties(dept,dbDept,new String[]{"companyId","companyName"});
            deptService.update(dbDept);
        }
        return "redirect:/system/dept/list.do";
    }

    //前往更新页面
    @RequestMapping("toUpdate")
    public String toUpdtate(HttpServletRequest request,String id){
        //根据id查询部门
        Dept dept = deptService.findById(id);
        //设置企业id
        dept.setCompanyId("1");
        //设置企业名称
        dept.setCompanyName("苏泊莱国际贸易有限公司");
        request.setAttribute("dept",dept);
        List<Dept> deptList = deptService.findAll("1");
        request.setAttribute("deptList",deptList);
        return "system/dept/dept-update";
    }
    //更具id删除
    @RequestMapping("delete")
    public String delete(String id){
        //更具id删除
        deptService.delete(id);
        //重定向到列表
        return "redirect:/system/dept/list.do";
    }
}
