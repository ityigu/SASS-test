package com.ityi.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ityi.service.cargo.ContractProductService;
import com.ityi.web.controller.BaseController;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {
    /*@Reference
    private ContractProductService contractProductService;*/

    @RequestMapping("cxportPdf")
    public void exportPdf() throws JRException, IOException, ClassNotFoundException, SQLException {
        //1.查找到jarsper文件
        String path = session.getServletContext().getRealPath("/jasper/demo02.jasper");

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://192.168.11.128:3306/saas-export-ee108","root","root");
        //2.将模板与数据结合
        JasperPrint jp = JasperFillManager.fillReport(path,new HashMap<>(),con);
        //3.导出
        JasperExportManager.exportReportToPdfStream(jp,response.getOutputStream());
    }

    /*@RequestMapping("cxportPdf1")
    public void exportPdf1() throws JRException, IOException {
        //1.查找到jarsper文件
        String path = session.getServletContext().getRealPath("/jasper/demo01.jasper");
        //2.将模板与数据结合
        JasperPrint jp = JasperFillManager.fillReport(path,new HashMap<>(),new JREmptyDataSource());
        //3.导出
        JasperExportManager.exportReportToPdfStream(jp,response.getOutputStream());*/
    }

