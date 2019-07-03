package com.ityi.dao.system;


import com.ityi.domain.system.Module;

import java.util.List;
import java.util.Map;

/**
 */
public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    int delete(String moduleId);

    //添加用户
    int save(Module module);

    //更新用户
    int update(Module module);

    //查询全部
    List<Module> findAll();

    //查询符合条件的json数据
    List<Map> findRoleModule(String roleId);

    //更具用户的id获取用户的菜单
    List<Module> findUserModules(String userId);

    //根据belong查询模块列表
    List<Module> findByBelong(Integer belong);

}