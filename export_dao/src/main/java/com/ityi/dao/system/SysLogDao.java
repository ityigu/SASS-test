package com.ityi.dao.system;

import com.ityi.domain.system.SysLog;
import java.util.List;

public interface SysLogDao {
    //查询全部
    List<SysLog> findAll(String companyId);

    //添加
    int save(SysLog log);

    List findOnlineData();

    List findFactory();

    List findfactorysale();
}