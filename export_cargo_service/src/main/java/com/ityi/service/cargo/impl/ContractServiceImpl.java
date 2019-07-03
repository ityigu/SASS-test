package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.cargo.ContractDao;
import com.ityi.dao.cargo.ContractProductDao;
import com.ityi.dao.cargo.ExtCproductDao;
import com.ityi.domain.cargo.*;
import com.ityi.service.cargo.ContractService;
import com.ityi.vo.ContractProductVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractProductDao contractProductDao;

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ContractExample example, int page, int size) {
        //1.设置分页条件
        PageHelper.startPage(page,size);
        //2.使用条件对象查询
        List<Contract> contractList = contractDao.selectByExample(example);
        return new PageInfo(contractList);
    }

    @Override
    public void save(Contract contract) {
    //设置合同id
        contract.setId(UtilFuns.generateID());
        //设置其他信息
        //创建时间
        contract.setCreateTime(new Date());
        //草稿
        contract.setState(0);
        //货物件数
        contract.setProNum(0);
        //附件数
        contract.setExtNum(0);
        //总金额
        contract.setTotalAmount(0d);
        //保存
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
    contractDao.updateByPrimaryKeySelective(contract);
    }

    //删除合同
    @Override
    public void delete(String id) {
        //1.获取附件的数量     要查谁就创建谁的条件对象
        //创建附件的条件对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        //设置查询条件合同id
        extCproductExample.createCriteria().andContractIdEqualTo(id);
        //查询所有附件的集合，遍历删除
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        for (ExtCproduct extCproduct : extCproducts) {
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        //创建货物的查询条件对象
        ContractProductExample contractProductExample = new ContractProductExample();
        //设置查询条件合同的id
        contractProductExample.createCriteria().andContractIdEqualTo(id);
        //查询所有货物
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        //4.遍历删除货物
        for (ContractProduct contractProduct : contractProducts) {
            contractProductDao.deleteByPrimaryKey(contractProduct.getId());
        }
        //5.删除合同
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ContractProductVo> findByShipTime(String shipTime, String companyId) {
        return contractDao.findByShipTime(shipTime,companyId);
    }
}
