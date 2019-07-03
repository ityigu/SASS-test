package com.ityi.service.system;

import com.github.pagehelper.PageInfo;
import com.ityi.domain.system.Module;
import com.ityi.domain.system.User;

import java.util.List;


public interface ModuleService {



    //根据id查询
    Module findById(String id);

    //保存
    void save(Module module);

    //修改，更新
    void update(Module module);

    //根据id删除
    void delete(String id);

    //查询所有角色，不带分页
    List<Module> findAll();

    //查询所有 带分页
    PageInfo findAll(String companyId, int page, int size);

    //根据用户id获取用户的菜单
    List<Module> findUserModules(User user);


}