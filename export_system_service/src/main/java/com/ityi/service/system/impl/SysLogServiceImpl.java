package com.ityi.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.system.SysLogDao;
import com.ityi.domain.system.SysLog;
import com.ityi.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public PageInfo findAll(String companyId, int page, int sizi) {
        //1.设置分页信息
        PageHelper.startPage(page,sizi);
        //2.查询所有
        List<SysLog> sysLogList = sysLogDao.findAll(companyId);
        //3.创建PageInfo并返回
        return new PageInfo(sysLogList);
    }

    @Override
    public void save(SysLog sysLog) {
        //1.设置id
        sysLog.setId(UtilFuns.generateID());
        //2.保存
        sysLogDao.save(sysLog);
    }

    @Override
    public List findOnlineData() {
        return sysLogDao.findOnlineData();
    }

    @Override
    public List findFactory() {
        return sysLogDao.findFactory();
    }

    @Override
    public List findfactorysale() {
        return sysLogDao.findfactorysale();
    }
}
