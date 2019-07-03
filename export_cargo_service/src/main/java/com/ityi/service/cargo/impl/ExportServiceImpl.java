package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.dao.cargo.*;
import com.ityi.domain.cargo.*;
import com.ityi.service.cargo.ExportService;
import com.ityi.vo.ExportProductResult;
import com.ityi.vo.ExportResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ExtEproductDao extEproductDao;


    @Override
    public void updateE(ExportResult exportResult) {
        //1.修改包运单
        Export export = new Export();
        export.setId(exportResult.getExportId());//设置id
        export.setRemark(exportResult.getRemark());//设置描述
        export.setState(exportResult.getState());//设置状态
        exportDao.updateByPrimaryKeySelective(export);//有选着的修改
        //2.修改货物
        Set<ExportProductResult> products = exportResult.getProducts();
        if (products!=null){
            for (ExportProductResult epr:products){
                //遍历所有海关返回的货物信息
                ExportProduct ep = new ExportProduct();
                ep.setId(epr.getExportProductId());//设置id
                ep.setTax(epr.getTax());//设置税
                exportProductDao.updateByPrimaryKeySelective(ep);
            }
        }
    }

    /*
    * 完成出口报运功能
    * 1.完善Export属性，并insert到数据库
    * 2.修改合同状态 update
    * 3.通过合同下的货物来得到报运下的货物信息，并insert
    * 4.通过合同下的货物下的附件得到报运下的货物下的附件并insert
    * */
    @Override
    public void save(Export export) {
        //1.完善Export属性，并insert到数据库
        export.setId(UUID.randomUUID().toString());
        export.setCreateTime(new Date());
        export.setState(0);
        //2.修改合同状态 update
        //2.1得到所有合同id
        String[] contractIds = export.getContractIds().split(",");
        String customerContract = "";
        for (String contractId:contractIds){
            //2.2根据id获取所有的合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            //2.3修改为已报运，状态为2
            contract.setState(2);//已报运
            contractDao.updateByPrimaryKeySelective(contract);
            customerContract+=contract.getContractNo()+" ";
        }
       //3.通过合同下的货物来得到报运下的货物信息，并insert
        //3.1根据合同id来查询所有的合同下的货物
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));//根据合同id进行in查询
        //得到合同下的所有货物
        Map<Object, Object> map = new HashMap<>();
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        //3.2 遍历所有合同下的货物，每遍历一次创建一个ExportProduct，并将ContractProduct复制到ExportProduct中
        for (ContractProduct contractProduct : contractProducts) {
            ExportProduct exportProduct = new ExportProduct();//报运单下的货物
            BeanUtils.copyProperties(contractProduct,exportProduct);//将合同下的货物属性copy到了报运单
            exportProduct.setId(UUID.randomUUID().toString());
            exportProduct.setExportId(export.getId());//报运单下货物关联到包运单
            exportProductDao.insertSelective(exportProduct);

            map.put(contractProduct.getId(),exportProduct.getId());
        }
       //4.通过合同下的货物下的附件得到报运下的货物下的附件并insert
        //3.1 根据合同id来查询所有的合同下的货物的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(Arrays.asList(contractIds));

        //根据合同id来查询所有附件
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //3.2遍历合同下的附件将其包装成一个个包运单下的附件
        for (ExtCproduct extCproduct : extCproducts) {
            ExtEproduct extEproduct = new ExtEproduct();
            BeanUtils.copyProperties(extCproduct,extEproduct);
            extCproduct.setId(UUID.randomUUID().toString());
            //设置附件所属的货物
            extEproduct.setExportProductId((String) map.get(extCproduct.getContractProductId()));
            //设置附件所属的合同
            extEproduct.setExportId(export.getId());
            extEproductDao.insertSelective(extEproduct);
        }

        export.setProNum(contractProducts.size());
        export.setExtNum(extCproducts.size());
        exportDao.insertSelective(export);

    }


    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }



    @Override
    public void update(Export export) {
        //1.修改export
        exportDao.updateByPrimaryKeySelective(export);
        //2.修改export下所有product
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null){
            for (ExportProduct ep : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(ep);
            }
        }
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<Export> exports = exportDao.selectByExample(example);
        return new PageInfo(exports);
    }
}
