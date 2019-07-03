package com.ityi.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.DownloadUtil;
import com.ityi.common.utils.UtilFuns;
import com.ityi.domain.cargo.Contract;
import com.ityi.domain.cargo.ContractExample;
import com.ityi.domain.system.User;
import com.ityi.service.cargo.ContractService;
import com.ityi.vo.ContractProductVo;
import com.ityi.web.controller.BaseController;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    /*
     * 查询所有购销合同
     *   细节：每个用户只能查询自己企业的购销合同
     * */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int sizi) {
        //1.创建查询条件对象
        ContractExample example = new ContractExample();
        /*//2.设置查询条件
        example.createCriteria().andCompanyIdEqualTo(super.getCurrentUserCompanyId());*/
        //设置排序
        example.setOrderByClause("create_time desc");
        //创建增强条件的对象
        ContractExample.Criteria criteria = example.createCriteria();
        //添加细粒度权限控制的条件
        //获取当前登录的用户
        User user = (User)session.getAttribute("user");
        //获取当前用户的等级
        Integer degree = user.getDegree();
        switch (degree){
            case 0:
                //saas管理员，什么都不能看
                throw new IllegalStateException("您无权浏览购销合同的数据。");
            case 1:
                //企业管理员
                criteria.andCompanyIdEqualTo(user.getCompanyId());//企业管理员，可以查看本企业所有
                break;
            case 2:
                //部门总监
                //此种方式可以实现功能，但是要先把当前用户的部门以及它下辖的子部门都获取出来，然后才能拼接条件，查询购销合同
                //所以需要跟数据库交互两次，才能得到购销合同的数据
//                List<String> deptIdList = deptService.findDeptIdByParentId(user.getDeptId());//select dept_id from pe_dept where parent_id = ?
//                criteria.andCreateDeptIn(deptIdList);

                //当我们的部门设计成有规律可循的id时，可以使用下面这种
                criteria.andCreateDeptLike(user.getDeptId()+"%");
                break;
            case 3:
                //部门经理
                criteria.andCreateDeptEqualTo(user.getDeptId());//部门经理可以查看部门下的所有数据
                break;
            case 4:
                //普通员工
                criteria.andCreateByEqualTo(user.getId());//普通员工只能查看自己的数据
                break;
            default:
                throw new  IllegalStateException("Who are you ???");
        }
        //3.使用条件查询
        PageInfo pageInfo = contractService.findAll(example, page, sizi);
        //4.存入请求域中
        request.setAttribute("page", pageInfo);
        //5.返回
        return "cargo/contract/contract-list";
    }



    //前往添加页面
    @RequestMapping("toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    //保存或更新
    @RequestMapping("edit")
    public String edit(Contract contract){
        //判断是保存还是更新
        if (UtilFuns.isEmpty(contract.getId())){
            //保存，没有id,没有名字
            contract.setCompanyId(super.getCurrentUserCompanyId());
            contract.setCompanyName(super.getCurrentUserCompanyName());
            contractService.save(contract);
        }else {
            //更新
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    //去往编辑页面
    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        Contract serviceById = contractService.findById(id);
        request.setAttribute("contract",serviceById);
        return "cargo/contract/contract-update";
    }

    //去往查看页面
    @RequestMapping("toView")
    public String toView(String id){
        Contract serviceById = contractService.findById(id);
        request.setAttribute("contract",serviceById);
        return "cargo/contract/contract-view";
    }

    //删除合同
    @RequestMapping("delete")
    public String delete(String id) {
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    //提交按钮
    @RequestMapping("submit")
    public String submit(String id)throws Exception{
        /*//判断合同状态是否可以提交
        Contract contract1 = contractService.findById(id);
        if (contract1.getState() > 1){
            throw new CustomeException("合同当前状态不可提交");
        }*/
        //创建一个Contract对象
        Contract contract = new Contract();
        //2.设置购销合同的id和状态
        contract.setId(id);
        contract.setState(1);
        //3.更新
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    //查询合同当前状态
    @RequestMapping("checkUpdateState")
    public @ResponseBody String checkUpdateState(String id){
        //根据id查询购销合同
        Contract contract = contractService.findById(id);
        //判断购销合同的状态是否小于2
        if (contract.getState() < 2){
            return "1";
        }else {
            return"0";
        }
    }

    //取消按钮
    @RequestMapping("cancel")
    public String cancel(String id){
        //创建一个Contract对象
        Contract contract = new Contract();
        //2.设置购销合同的id和状态
        contract.setId(id);
        contract.setState(0);
        //3.更新
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    /*
    * 跳转到出货表
    * */
    @RequestMapping("print")
    public String print(){
        return "cargo/print/contract-print";
    }

    /*
    * 出货表下载 --无样式
    * */
    @RequestMapping("printExcel1")
    public void printExcel1(String inputDate) throws IOException {
        //1.根据参数去查询要下载的数据 contract contractProduct两部分数据
        List<ContractProductVo> contractProductVos = contractService.findByShipTime(inputDate, getCurrentUserCompanyId());
        //2.将查询的数据包装到excel中
        int rowNum = 0;
        //2.1创建workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //2.2创建sheet
        XSSFSheet sheet = workbook.createSheet();
        //2.3创建第一行 大标题
        Row row_big_title = sheet.createRow(rowNum++);
        Cell cell_big_title = row_big_title.createCell(1);
        cell_big_title.setCellValue(inputDate+"月份出货表");
        //2.4 创建第二行 小标题
        Row row_small_title = sheet.createRow(rowNum++);
        String[] titles = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i<titles.length;i++){
            row_small_title.createCell(i+1).setCellValue(titles[i]);
        }
        //2.5将contractProductVos包装到excel中的数据部分
        for (ContractProductVo vo:contractProductVos){
            int celNum = 1;
            Row row_content = sheet.createRow(rowNum++);
            Cell cell1 = row_content.createCell(celNum++);
            cell1.setCellValue(vo.getCustomName()); //将客户名称设置到第二列

            Cell cell2 = row_content.createCell(celNum++);
            cell2.setCellValue(vo.getContractNo()); //将订单号设置到第二列

            Cell cell3 = row_content.createCell(celNum++);
            cell3.setCellValue(vo.getProductNo());

            Cell cell4 = row_content.createCell(celNum++);
            cell4.setCellValue(vo.getCnumber());

            Cell cell5 = row_content.createCell(celNum++);
            cell5.setCellValue(vo.getFactoryName()); //将客户名称设置到第二列

            Cell cell6 = row_content.createCell(celNum++);
            cell6.setCellValue(vo.getDeliveryPeriod()); //将订单号设置到第二列

            Cell cell7 = row_content.createCell(celNum++);
            cell7.setCellValue(vo.getShipTime());

            Cell cell8 = row_content.createCell(celNum++);
            cell8.setCellValue(vo.getTradeTerms());
        }

        //3.下载
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        new DownloadUtil().download(baos, response,"出货表.xls");//参数一是要下载的文件内容，参数二是response对象，参数三是下载的文件名称
    }

    /*
     * 出货表下载 --有样式
     * */
    @RequestMapping("printExcel2")
    public void printExcel2(String inputDate) throws IOException {
        //1.根据参数去查询要下载的数据 contract contractProduct两部分数据
        List<ContractProductVo> contractProductVos = contractService.findByShipTime(inputDate, getCurrentUserCompanyId());
        //2.将查询的数据包装到excel中
        int rowNum = 0;
        //2.1创建workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //2.2创建sheet
        XSSFSheet sheet = workbook.createSheet();
        //设置每一列的宽
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(0,26*256);
        sheet.setColumnWidth(0,12*256);
        sheet.setColumnWidth(3,30 * 256);
        sheet.setColumnWidth(4,12 * 256);
        sheet.setColumnWidth(5,15 * 256);
        sheet.setColumnWidth(6,10 * 256);
        sheet.setColumnWidth(7,10 * 256);
        sheet.setColumnWidth(8,10 * 256);
        //2.3创建第一行 大标题
        Row row_big_title = sheet.createRow(rowNum++);
        Cell cell_big_title = row_big_title.createCell(1);
        cell_big_title.setCellValue(inputDate.replace("-0", "-").replace("-", "年") + "月份出货表");
        //设置样式
        cell_big_title.setCellStyle(bigTitle(workbook)); //设置大标题样式
//合并单元格 参数1 开始行  参数2 结束行  参数3 开始列  参数4  结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        //2.4 创建第二行 小标题
        Row row_small_title = sheet.createRow(rowNum++);
        String[] titles = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i<titles.length;i++){
            row_small_title.createCell(i+1).setCellValue(titles[i]);
        }
        //2.5将contractProductVos包装到excel中的数据部分
        for (ContractProductVo vo:contractProductVos){
            int celNum = 1;
            Row row_content = sheet.createRow(rowNum++);

            Cell cell1 = row_content.createCell(celNum++);
            cell1.setCellValue(vo.getCustomName()); //将客户名称设置到第二列
            cell1.setCellStyle(text(workbook));

            Cell cell2 = row_content.createCell(celNum++);
            cell2.setCellValue(vo.getContractNo()); //将订单号设置到第二列
            cell2.setCellStyle(text(workbook));

            Cell cell3 = row_content.createCell(celNum++);
            cell3.setCellValue(vo.getProductNo());
            cell3.setCellStyle(text(workbook));

            Cell cell4 = row_content.createCell(celNum++);
            cell4.setCellValue(vo.getCnumber());
            cell4.setCellStyle(text(workbook));

            Cell cell5 = row_content.createCell(celNum++);
            cell5.setCellValue(vo.getFactoryName()); //将客户名称设置到第二列
            cell5.setCellStyle(text(workbook));

            Cell cell6 = row_content.createCell(celNum++);
            cell6.setCellValue(vo.getDeliveryPeriod()); //将订单号设置到第二列
            cell6.setCellStyle(text(workbook));

            Cell cell7 = row_content.createCell(celNum++);
            cell7.setCellValue(vo.getShipTime());
            cell7.setCellStyle(text(workbook));

            Cell cell8 = row_content.createCell(celNum++);
            cell8.setCellValue(vo.getTradeTerms());
            cell8.setCellStyle(text(workbook));
        }

        //3.下载
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        new DownloadUtil().download(baos, response,"出货表.xls");//参数一是要下载的文件内容，参数二是response对象，参数三是下载的文件名称
    }

    //大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }

    /*
     * 出货表下载 --基于模板打印
     * */
    @RequestMapping("printExcel")
    public void printExcel(String inputDate) throws IOException {
        //1.根据参数去查询要下载的数据 contract contractProduct两部分数据
        List<ContractProductVo> contractProductVos = contractService.findByShipTime(inputDate, getCurrentUserCompanyId());
        //2.将查询的数据包装到excel中
        int rowNum = 0;
        //2.1创建workbook
        String path = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(path);
        //2.2创建sheet
        Sheet sheet = workbook.getSheetAt(0);
        //2.3创建第一行 大标题
        Row row_big_title = sheet.getRow(rowNum++);
        Cell cell_big_title = row_big_title.getCell(1);
        cell_big_title.setCellValue(inputDate.replace("-0", "-").replace("-", "年") + "月份出货表");
        //2.4 创建第二行 小标题
       rowNum++;
        //2.5将contractProductVos包装到excel中的数据部分
        //2.5.1 获取第三行的每一个cell的样式，保存到CellStyle[]中
        CellStyle[] css = new CellStyle[9];
        Row row = sheet.getRow(rowNum);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null){
                css[i] = cell.getCellStyle();//获取第三行每一个cell样式保存到数组中
            }
        }
        //2.5.2
        for (ContractProductVo vo:contractProductVos){
            int celNum = 1;
            Row row_content = sheet.createRow(rowNum++);

            Cell cell1 = row_content.createCell(celNum++);
            cell1.setCellValue(vo.getCustomName()); //将客户名称设置到第二列
            cell1.setCellStyle(css[1]);

            Cell cell2 = row_content.createCell(celNum++);
            cell2.setCellValue(vo.getContractNo()); //将订单号设置到第二列
            cell1.setCellStyle(css[2]);

            Cell cell3 = row_content.createCell(celNum++);
            cell3.setCellValue(vo.getProductNo());
            cell1.setCellStyle(css[3]);

            Cell cell4 = row_content.createCell(celNum++);
            cell4.setCellValue(vo.getCnumber());
            cell1.setCellStyle(css[4]);

            Cell cell5 = row_content.createCell(celNum++);
            cell5.setCellValue(vo.getFactoryName()); //将客户名称设置到第二列
            cell1.setCellStyle(css[5]);

            Cell cell6 = row_content.createCell(celNum++);
            cell6.setCellValue(vo.getDeliveryPeriod()); //将订单号设置到第二列
            cell1.setCellStyle(css[6]);

            Cell cell7 = row_content.createCell(celNum++);
            cell7.setCellValue(vo.getShipTime());
            cell1.setCellStyle(css[7]);

            Cell cell8 = row_content.createCell(celNum++);
            cell8.setCellValue(vo.getTradeTerms());
            cell1.setCellStyle(css[8]);

        }

        //3.下载
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        new DownloadUtil().download(baos, response,"出货表.xls");//参数一是要下载的文件内容，参数二是response对象，参数三是下载的文件名称
    }

}

