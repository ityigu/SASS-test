package com.ityi.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.system.ModuleDao;
import com.ityi.dao.system.RoleDao;
import com.ityi.domain.system.Role;
import com.ityi.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ModuleDao  moduleDao;


    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public void save(Role role) {
        String id = UtilFuns.generateID();
        role.setId(id);
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public List<Map> findRoleModule(String id) {
        return moduleDao.findRoleModule(id);
    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页信息
        PageHelper.startPage(page, size);
        //2.查询结果集，（查询所有即可）
        List<Role> deptList = roleDao.findAll(companyId);
        //3.创建返回值并返回
        return new PageInfo(deptList);
    }

    @Override
    public void updateRoleModule(String id, String moduleIds) {
        //1.使用角色的id去角色模块的中间表删除
        roleDao.deleteRoleModule(id);
        //2.把moduleIds用逗号隔开分割成有ModuleId组成的集合
        String[] moduleIdArray = moduleIds.split(",");
        //3.遍历moduleId的数组
        for (String moduleId : moduleIdArray){
            //4.保存在角色模块中间表
            roleDao.saveRoleModule(id,moduleId);
        }
    }
}
