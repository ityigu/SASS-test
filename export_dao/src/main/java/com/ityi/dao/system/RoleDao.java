package com.ityi.dao.system;

import com.ityi.domain.system.Role;
import org.apache.ibatis.annotations.Param;


import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部用户
    List<Role> findAll(String companyId);

	//根据id删除
    int delete(String id);

	//添加
    int save(Role role);

	//更新
    int update(Role role);

    //根据角色id删除角色模块中间表数据
    void deleteRoleModule(String id);

    //在角色模块中间表保存角色和模块的关系
    void saveRoleModule(@Param("roleId") String id, @Param("moduleId") String moduleId);

}