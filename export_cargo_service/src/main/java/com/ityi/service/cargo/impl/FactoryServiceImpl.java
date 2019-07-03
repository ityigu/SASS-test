package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.cargo.FactoryDao;
import com.ityi.domain.cargo.Factory;
import com.ityi.domain.cargo.FactoryExample;
import com.ityi.service.cargo.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {
    @Autowired
    private FactoryDao factoryDao;

    @Override
    public void save(Factory factory) {
        factory.setId(UtilFuns.generateID());
        factoryDao.insertSelective(factory);
    }

    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }

    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Factory> findAll(FactoryExample example) {
        return factoryDao.selectByExample(example);
    }
}
