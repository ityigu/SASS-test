package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.cargo.ContractDao;
import com.ityi.dao.cargo.ContractProductDao;
import com.ityi.dao.cargo.ExtCproductDao;
import com.ityi.domain.cargo.*;
import com.ityi.service.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class ContractProductServiceImpl implements ContractProductService{

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        //设置分页信息
        PageHelper.startPage(page,size);
        //2.使用查询条件对象查询
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(example);
        //3.返回
        return new PageInfo(contractProductList);
    }

    @Override
    public void save(ContractProduct contractProduct) {
        //1.设置货物的id
        contractProduct.setId(UtilFuns.generateID());
        // 2.判断是否有单价和数量
        double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice()!=null){
            amount = contractProduct.getPrice()*contractProduct.getCnumber();
        }
        //3.设置总金额
        contractProduct.setAmount(amount);
        //4.根据购销合同的id查询合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //5.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        //6.设置货物数
        contract.setProNum(contract.getProNum()+1);
        //7.保存货物
        contractProductDao.insertSelective(contractProduct);
        //8.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1.查询数据库中的数据
        ContractProduct dbContractProduct = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        //2.获取数据库中原货物的总金额
        Double dbAmount = dbContractProduct.getAmount();
        // 3.判断是否有单价和数量
        double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice()!=null){
            amount = contractProduct.getPrice()*contractProduct.getCnumber();
        }
        //4.设置总金额
        contractProduct.setAmount(amount);
        //5.根据购销合同的id查询合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //6.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        //7.更新货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //8.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.创建附件的查询条件对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        //2.使用货物的id作为条件
        extCproductExample.createCriteria().andContractIdEqualTo(id);
        //3.查询所有的附件
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //4.遍历附件的集合，取每一个附件的总金额求和，删除附件
        double extAmount = 0d;
        for (ExtCproduct extCproduct : extCproducts) {
            //附件总金额求和
            extAmount+=extCproduct.getAmount();
            //删除附件
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        //5.根据id查询货物
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        //6.根据货物中的合同id，获取购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //7.记录下货物的总金额
        Double productAmount = contractProduct.getAmount();
        //8.删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //9.设置合同总金额
        contract.setTotalAmount(contract.getTotalAmount()-productAmount-extAmount);
        //10.设置货物的数量
        contract.setProNum(contract.getProNum()-1);
        //11.设置附件数量
        contract.setExtNum(contract.getExtNum()-extCproducts.size());
        //12.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);


    }

    @Override
    public void saveAll(List<ContractProduct> cps) {
        for (ContractProduct cp :cps){
            save(cp);
        }
    }
}
