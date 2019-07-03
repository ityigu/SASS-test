package com.ityi.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ityi.domain.system.Company;
import com.ityi.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {
    @Reference
    private CompanyService companyService;

    /*
    * 企业申请
    * */
    @RequestMapping("/apply")
    public @ResponseBody String apply(Company company){
        companyService.save(company);
        return "1";
    }

}
