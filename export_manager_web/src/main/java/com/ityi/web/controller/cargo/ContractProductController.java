package com.ityi.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.cargo.ContractProduct;
import com.ityi.domain.cargo.ContractProductExample;
import com.ityi.domain.cargo.Factory;
import com.ityi.domain.cargo.FactoryExample;
import com.ityi.service.cargo.ContractProductService;
import com.ityi.service.cargo.FactoryService;
import com.ityi.web.controller.BaseController;
import com.ityi.web.utils.FileUploadUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;

    /*
    * 前往货物的列表页面
    * */
    @RequestMapping("list")
    public String list(String contractId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int sizi){
        //1.创建货物的查询条件条件对象
        ContractProductExample contractProductExample = new ContractProductExample();
        //2.设置查询条件
        contractProductExample.createCriteria().andContractIdEqualTo(contractId);
        //3.查询货物
        PageInfo pageInfo = contractProductService.findAll(contractProductExample, page, sizi);
        //4.存入请求域中
        request.setAttribute("page",pageInfo);
        //5.把购销合同的id存入请求域
        request.setAttribute("contractId",contractId);
        //6.创建生产厂家的查询条件对象
        FactoryExample factoryExample = new FactoryExample();
        //7.设置查询条件
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        //8.使用查询条件查询厂家
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //9.存入请求域中
        request.setAttribute("factoryList",factoryList);
        //10.前往请求页面
        return "cargo/product/product-list";
    }

    @Autowired
    private FileUploadUtil fileUploadUtil;
        /*
        * 保存或者更新
        * */
        @RequestMapping("edit")
        public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        //1.判断是保存还是更新
            if (UtilFuns.isEmpty(contractProduct.getId())){
                //保存
                //设置企业id和企业名字
                contractProduct.setCompanyId(super.getCurrentUserCompanyId());
                contractProduct.setCompanyName(super.getCurrentUserCompanyName());
                //判断是否提交了文件（提交了就上传，没提交就什么都不做）
                String imagePath = "";
                if (productPhoto != null){
                    //上传文件
                    imagePath = "http://"+fileUploadUtil.upload(productPhoto);
                }
                //给货物设置图片路径
                contractProduct.setProductImage(imagePath);
                //保存货物
                contractProductService.save(contractProduct);
            }else {
                //更新
                contractProductService.update(contractProduct);
            }
            //前往更新列表
            return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
        }

        //前往货物的编辑页面
        @RequestMapping("toUpdate")
        public String toUpdate(String id){
            //1.更具货物的id查询货物
            ContractProduct contractProduct = contractProductService.findById(id);
            //2.存入请求请求域中
            request.setAttribute("contractProduct",contractProduct);
            //3.创建生产厂家的条件查询
            FactoryExample factoryExample = new FactoryExample();
            //4.设置查询条件
            factoryExample.createCriteria().andCtypeEqualTo("货物");
            //5.使用查询条件厂家查询厂家
            List<Factory> factoryList = factoryService.findAll(factoryExample);
            //6.存入请求域中
            request.setAttribute("factoryList",factoryList);
            //7.前往编辑页面
            return "cargo/product/product-update";
        }

        @RequestMapping("delete")
        public String delete(String id,String contractId){
            //删除货物
            contractProductService.delete(id);
            //前往货物的方法
            return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
        }


        /*
        * 前往上传页面
        * */
        @RequestMapping("toImport")
        public String toImport(String contractId){
            request.setAttribute("contractId",contractId);
            return "/cargo/product/product-import";
        }

        /*
        * /cargo/contractProduct/import.do
        * 完成货物的批量上传
        * */
        @RequestMapping("import")
        public String importproduct(String contractId,MultipartFile file) throws IOException {
            //1.读取上传文件的内容 excel文件
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            //2.将文件内容封装成ContractProduct
            List<ContractProduct> cps = new ArrayList<>();
            for (int i = 1;i<=sheet.getLastRowNum();i++){
               Row row = sheet.getRow(i);//获取每一行
                Object[] objs = new Object[10];
                for (int j = 0;j < row.getLastCellNum();j++){
                    Cell cell = row.getCell(j);//得到当前行的每一个cell
                    if (cell != null){
                        objs[j] = getCellValue(cell);//将cell值封装到数组中
                    }
                }
                //将每一行中每一个cell中的值封装到了contractProduct对象中
                ContractProduct cp = new ContractProduct(objs, getCurrentUserCompanyId(), getCurrentUserCompanyName());
                cp.setContractId(contractId);//添加合同id
                cps.add(cp);
            }
            //3.调用service将contractProduct存储到数据库
            contractProductService.saveAll(cps);
            //4.页面跳转
            return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
        }

    private Object getCellValue(Cell cell) {
        Object obj = null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                obj = cell.getStringCellValue(); //String
                break;
            case BOOLEAN:
                obj = cell.getBooleanCellValue(); //boolean
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {//判断是否可以格式化成日期
                    obj = cell.getDateCellValue();
                } else {
                    obj = cell.getNumericCellValue(); //数值
                }
                break;
        }
        return obj;
    }

}
















