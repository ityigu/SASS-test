package com.ityi.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.system.ModuleDao;
import com.ityi.domain.system.Module;
import com.ityi.domain.system.User;
import com.ityi.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页信息
        PageHelper.startPage(page, size);
        //2.查询结果集，（查询所有即可）
        List<Module> deptList = moduleDao.findAll();
        //3.创建返回值并返回
        return new PageInfo(deptList);
    }

    @Override
    public List<Module> findAll() {
       return moduleDao.findAll();
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public void  delete(String id) {
         moduleDao.delete(id);
    }

    @Override
    public void save(Module module) {
        //设置id
        module.setId(UtilFuns.generateID());
        //保存
         moduleDao.save(module) ;
    }

    @Override
    public void update(Module module) {
         moduleDao.update(module);
    }

    @Override
    public List<Module> findUserModules(User user) {
        //1。判断当前用户是不是saas管理员
        if (user.getDegree() == 0){
            //sass管理员
            return moduleDao.findByBelong(0);
        }else if (user.getDegree() == 1){
            //企业管理员
            return moduleDao.findByBelong(1);
        }else {
        return moduleDao.findUserModules(user.getId());
    }}
}
