package com.ityi.web.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.system.Company;
import com.ityi.service.company.CompanyService;
import com.ityi.web.controller.BaseController;
import com.ityi.web.exceptions.CustomeException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/*
* 企业的控制器
* */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {
    @Reference
    private CompanyService companyService;

    /*
    * 查询所有企业：使用MyBatis插件：PageHelper
    * */
    @RequiresPermissions("企业管理")
    @RequestMapping("/list")
    public String list(HttpServletRequest request, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "6") int sizi){
        //1.调用业务层查询
        PageInfo pageInfo = companyService.findByPageHelper(page, sizi);
        //2.存入请求域中
        request.setAttribute("page",pageInfo);
        //3.转发到列表页面
        return "company/company-list";
    }

    //有分页，用的是自己写的
//    @RequestMapping("/list")
//    public String list(HttpServletRequest request, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "1") int sizi){
//        //1.调用业务层查询
//        PageResult pageResult = companyService.findPage(page,sizi);
//        //2.存入请求域中
//        request.setAttribute("page",pageResult);
//        //3.转发到列表页面
//        return "company/company-list";
//    }

    //未分页
//    @RequestMapping("/list")
//    public String list(HttpServletRequest request){
//        //1.调用业务层查询
//        List<Company> companyList = companyService.findAll();
//        //2.存入请求域中
//        request.setAttribute("list",companyList);
//        //3.转发到列表页面
//        return "company/company-list";
//    }

    /*
    *
    * 前往到添加页面
    **/
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    /*
    * 保存或者更新
    * */
    @RequestMapping("/edit")
    public String edit(Company company)throws Exception{
        if (UtilFuns.isEmpty(company.getName())){
            throw new CustomeException("企业名称是必须的");
        }
        //1.判断company是否有主键信息
        //等同于company.getId()==null||"".equals(company.getId())
        if (StringUtils.isEmpty(company.getId())){
            //保存
            companyService.save(company);
        }else {
            //更新
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }

    /*
    * 前往跟新页面
    * */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,HttpServletRequest request){
        //1.根据id查询企业信息
        Company company = companyService.findById(id);
        //2.吧查询出来的企业信息存入请求域中
        request.setAttribute("company",company);
        //3.转发到跟新页面
        return "company/company-update";
    }
    /*
    * 根据id删除
    * */
    @RequestMapping("/delete")
    public String delete(String id){
        companyService.delete(id);
        //2.重定向到列表页面
        return "redirect:/company/list.do";
    }
}
