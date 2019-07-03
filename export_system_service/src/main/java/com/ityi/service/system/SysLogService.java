package com.ityi.service.system;

import com.github.pagehelper.PageInfo;
import com.ityi.domain.system.SysLog;

import java.util.List;

public interface SysLogService {
    /*
    * 查询日志列表
    * */
    PageInfo findAll(String companyId, int page, int sizi);

    /*
    * 保存日志
    * */
    void save(SysLog sysLog);


    List findOnlineData();

    List findFactory();

    List findfactorysale();
}
