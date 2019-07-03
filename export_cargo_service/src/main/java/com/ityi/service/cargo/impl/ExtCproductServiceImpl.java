package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.cargo.ContractDao;
import com.ityi.dao.cargo.ExtCproductDao;
import com.ityi.domain.cargo.Contract;
import com.ityi.domain.cargo.ExtCproduct;
import com.ityi.domain.cargo.ExtCproductExample;
import com.ityi.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        //1.设置分页信息
        PageHelper.startPage(page,size);
        //2.使用条件查询所有
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);
        //3.返回
        return new PageInfo(extCproductList);
    }



    @Override
    public void save(ExtCproduct extCproduct) {
        //1.设置id
        extCproduct.setId(UtilFuns.generateID());
        //2.判断是否存入单价和数量
        double amount = 0d;
        if(extCproduct.getCnumber() != null && extCproduct.getPrice()!=null){
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //3.设置金额
        extCproduct.setAmount(amount);
        //4.查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //5.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        //6.设置附件数
        contract.setExtNum(contract.getExtNum()+1);
        //7.保存附件
        extCproductDao.insertSelective(extCproduct);
        //8.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        //1.先把数据库的附件查询出来
        ExtCproduct dbExtCproduct = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        //记录数据库原来的金额
        double oldAmout = dbExtCproduct.getAmount();
        //2.判断是否存入单价和数量
        double amount = 0d;
        if(extCproduct.getCnumber() != null && extCproduct.getPrice()!=null){
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //3.设置金额
        extCproduct.setAmount(amount);
        //4.查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //5.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-oldAmout+amount);
        //6.更新附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        //7.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
    //1.先把数据库中的附件查询出来
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //2.记录数据库当前附件的金额
        Double oldAmout = extCproduct.getAmount();
        //3.查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //4.删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //5.设置合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-oldAmout);
        //6.设置合同的附件数
        contract.setExtNum(contract.getExtNum()-1);
        //7.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }


}
