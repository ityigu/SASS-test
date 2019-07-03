package com.ityi.web.controller.stat;

import com.ityi.service.system.SysLogService;
import com.ityi.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("stat")
public class StatController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /*
    * http://localhost:8083/stat/toCharts.do?chartsType=factory
    * */
    @RequestMapping("toCharts")
    public String toCharts(String chartsType){
        return "/stat/stat-"+chartsType;//根据参数来跳转页面
    }

    //系统访问压力图
    @RequestMapping("findOnlineData")
    public @ResponseBody List findOnlineData(){
        return sysLogService.findOnlineData();
    }

    //厂家销售额统计
    @RequestMapping("findFactory")
    public @ResponseBody List findFactory(){
        return sysLogService.findFactory();
    }

    //产品销售排行
    @RequestMapping("findfactorysale")
    public @ResponseBody List findfactorysale(){
        return sysLogService.findfactorysale();
    }

}
