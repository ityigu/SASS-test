package com.ityi.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import com.ityi.dao.cargo.ExportProductDao;
import com.ityi.domain.cargo.Export;
import com.ityi.domain.cargo.ExportExample;
import com.ityi.domain.cargo.ExportProduct;
import com.ityi.domain.cargo.ExportProductExample;
import com.ityi.service.cargo.ExportProductService;
import com.ityi.service.cargo.ExportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {

    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return null;
    }

    @Override
    public void save(ExportProduct exportProduct) {

    }

    @Override
    public void update(ExportProduct exportProduct) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        return null;
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return exportProductDao.selectByExample(exportProductExample);
    }
}
