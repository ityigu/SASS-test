package com.ityi.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.cargo.*;
import com.ityi.service.cargo.ExtCproductService;
import com.ityi.service.cargo.FactoryService;
import com.ityi.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    //前往附件列表页面
    @RequestMapping("list")
    public String list(String contractId, String contractProductId, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int sizi){
        //1.创建附加查询对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        //2.设置查询条件，我们要获取的是货物下的某个附件
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        //3.查询附件列表
        PageInfo pageInfo = extCproductService.findAll(extCproductExample, page, sizi);
        //4.存入请求域中
        request.setAttribute("page",pageInfo);
        //5.把购销合同的id存入请求域，目的是提娜佳附件时，可以直接试用
        request.setAttribute("contractId",contractId);
        //6.把货物的id存入请求域
        request.setAttribute("contractProductId",contractProductId);
        //7.创建生产厂家的查询条件对象
        FactoryExample factoryExample = new FactoryExample();
        //8.设置生产厂家的查询条件对象
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        //9.查询生产厂家
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //10.将生产厂家存入请求域
        request.setAttribute("factoryList",factoryList);
        //11.前往附件的列表页面
        return "cargo/extc/extc-list";
    }

    //保存或者更新
    @RequestMapping("edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto){
        //1.判断保存还是更新
        if (UtilFuns.isEmpty(extCproduct.getId())){
            //保存
            //设置企业信息
            extCproduct.setCompanyId(super.getCurrentUserCompanyId());
            extCproduct.setCompanyName(super.getCurrentUserCompanyName());
            //判断是否提供了文件
//            if (productPhoto != null){
//                //上传文件
//            }
            //保存附件
            extCproductService.save(extCproduct);
        }else {
            //更新
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    //前往编辑页面
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        //1.根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        //2.存入请求域
        request.setAttribute("extCproduct",extCproduct);
        //3.创建生产厂家的查询条件对象
        FactoryExample factoryExample = new FactoryExample();
        //4.设置查询条件：我们只要生产附件的厂家
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        //5.查询生产厂家
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //6.生产厂家存入请求域
        request.setAttribute("factoryList",factoryList);
        //7.设置合同的id到请求域
        request.setAttribute("contractId",extCproduct.getContractId());
        //8.设置货物的id到请求域
        request.setAttribute("contractProductId",extCproduct.getContractProductId());
        //9.前往编辑页面
        return "cargo/extc/extc-update";
    }

    //根据id删除
    @RequestMapping("delete")
    public String delete(String id,String contractId,String contractProductId){
        //直接调用service删除
        extCproductService.delete(id);
        //前往列表方法
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }

}
