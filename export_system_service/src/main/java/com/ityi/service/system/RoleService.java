package com.ityi.service.system;

import com.github.pagehelper.PageInfo;
import com.ityi.domain.system.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface RoleService {

    //查询所有 带分页
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    Role findById(String id);

    //保存
    void save(Role role);

    //修改，更新
    void update(Role role);

    //根据id删除
    void delete(String id);

    //查询所有角色，不带分页
    List<Role> findAll(String companyId);

     //查询符合条件的json数据
    List<Map> findRoleModule(String id);

    /*
     * 更新角色和模块的关系
     * */
    void updateRoleModule(String id, String moduleIds);

}