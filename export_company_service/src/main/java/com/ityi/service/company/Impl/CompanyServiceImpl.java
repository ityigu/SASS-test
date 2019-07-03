package com.ityi.service.company.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.entity.PageResult;
import com.ityi.dao.system.CompanyDao;
import com.ityi.domain.system.Company;
import com.ityi.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void save(Company company) {
        //创建id
        String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        //2.给company赋值
        company.setId(id);
        //3.保存
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
    companyDao.update(company);
    }

    @Override
    public void delete(String id) {
    companyDao.delete(id);
    }

    @Override
    public PageResult findPage(int page, int size) {
        //1.查询总记录条数
        int total = companyDao.findTotal();
        //2.查询带有分页的结果集
        List<Company> companies = companyDao.findPage((page-1)*size,size);
        //3.创建返回值对象
        PageResult pageResult = new PageResult(total,companies,page,size);
        //4.返回
        return pageResult;
    }

    /*
     * 使用pageHelper实现分页
     * */
    @Override
    public PageInfo findByPageHelper(int page, int size) {

            //1.使用PageHelper的静态方法设置信息
            PageHelper.startPage(page,size);
            //2.紧跟着的查询方法会被分页
            List<Company> companies = companyDao.findAll();
            //3.创建返回值
            PageInfo pageInfo = new PageInfo(companies);
            //4,返回
            return pageInfo;

    }


}
