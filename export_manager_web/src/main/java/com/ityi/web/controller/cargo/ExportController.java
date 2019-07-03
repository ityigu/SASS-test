package com.ityi.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.ityi.domain.cargo.*;
import com.ityi.service.cargo.ContractService;
import com.ityi.service.cargo.ExportProductService;
import com.ityi.service.cargo.ExportService;
import com.ityi.vo.ExportProductVo;
import com.ityi.vo.ExportResult;
import com.ityi.vo.ExportVo;
import com.ityi.web.controller.BaseController;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController{

    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    //查询合同信息
    @RequestMapping("contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int sizi){
        //创建条件查询对象
        ContractExample example = new ContractExample();
        example.createCriteria().andStateEqualTo(1).andCompanyIdEqualTo(getCurrentUserCompanyId());
        example.setOrderByClause("create_time desc");
        PageInfo all = contractService.findAll(example, page, sizi);
        request.setAttribute("page",all);
        return "cargo/export/export-contractList";
    }

    /*
    * 查询所有报运单
    * */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int sizi){
        ExportExample exportExample = new ExportExample();

        exportExample.createCriteria().andCompanyIdEqualTo(getCurrentUserCompanyId());
        exportExample.setOrderByClause("create_time desc");
        PageInfo pageInfo = exportService.findAll(exportExample, page, sizi);
        request.setAttribute("page",pageInfo);
        return "/cargo/export/export-list";
    }

    /*
    * 跳转到添加包运单信息页面
    * */
    @RequestMapping("toExport")
    public String toExport(String id){//用一个字符串来接收数组，数组元素会以逗号分隔
        request.setAttribute("id",id);
        return "/cargo/export/export-toExport";
    }


    /*
    * 增加和修改
    * */
    @RequestMapping("edit")
    public String edit(Export export){
        if (StringUtils.isEmpty(export.getId())){
            export.setCompanyId(getCurrentUserCompanyId());
            export.setCompanyName(getCurrentUserCompanyName());
            //save
            exportService.save(export);
        }else {
            //update
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }

    //去往修改页面
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        //1.根据id查询报运单
        Export export = exportService.findById(id);
        request.setAttribute("export",export);
        //2.根据id查询包运单下的货物
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> all = exportProductService.findAll(exportProductExample);
        request.setAttribute("eps",all);
        return "/cargo/export/export-update";
    }


    /** 电子报运功能
    *
    * */
    @RequestMapping("exportE")
    public String exportE(String id){
        //1.根据id查询export 包装成exportVo
        Export export = exportService.findById(id);
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export,exportVo);
        exportVo.setExportId(export.getId());//设置包运单id
        //2.根据id查询exportProduct 包装成exportproductVo
        ExportProductExample example = new ExportProductExample();
        ExportProductExample.Criteria criteria = example.createCriteria();
        criteria.andExportIdEqualTo(id);//根据id查询所有货物
        List<ExportProduct> exportProducts = exportProductService.findAll(example);
        List<ExportProductVo> epvs = new ArrayList<>();
        for (ExportProduct ep: exportProducts){
            ExportProductVo epv = new ExportProductVo();
            BeanUtils.copyProperties(ep,epv);
            epv.setExportId(export.getId());//设置包运单id
            epv.setExportProductId(ep.getId());//设置报运单下货物id
            epvs.add(epv);
        }
        exportVo.setProducts(epvs);//建立包运单vo与货物vo之间的关系，在向海关系统发送请求时，只传递exportVO
        //3.post请求海关系统 完成报运
        WebClient.create("http://localhost:8080/ws/export").type(MediaType.APPLICATION_JSON_TYPE).post(exportVo);
        //4.get请求 海关系统 获取报运信息
        ExportResult exportResult = WebClient.create("http://localhost:8080/ws/export/" + id).get(ExportResult.class);
        //调用service来完成更新包运单信息及货物信息
        exportService.updateE(exportResult);
        return "redirect:/cargo/export/list.do";
    }

}
