package com.ityi.service.system;

import com.github.pagehelper.PageInfo;
import com.ityi.domain.system.User;

import java.util.List;

public interface UserService {
    //根据企业id查询全部
    List<User> findAll(String companyId);

    //根据id查询
    User findById(String userId);

    //根据id删除
    int delete(String userId);

    //保存
    int save(User user);

    //更新
    int update(User user);

    //根据企业id分页擦寻
    public PageInfo findAll(String companyId, int page, int size);

    //根据用户id查询用户所具备的角色
    List<String> findUserRole(String id);

    //更新用户的角色
    void changeUserRole(String id, String[] roleIds);


    //根据邮箱查询用户
    User findByEmial(String email);

}
