package com.ityi.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ityi.common.utils.UtilFuns;
import com.ityi.dao.system.UserDao;
import com.ityi.domain.system.User;
import com.ityi.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll(String companyId) {
       return userDao.findAll(companyId);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public int delete(String id) {
        userDao.deleteRoleUser(id);
        return userDao.delete(id);
    }

    @Override
    public int save(User user) {
        //设置id
        user.setId(UtilFuns.generateID());
        //保存
        return userDao.save(user) ;
    }

    @Override
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页信息
        PageHelper.startPage(page,size);
        //2.查询结果集，（查询所有即可）
        List<User> deptList = userDao.findAll(companyId);
        //3.创建返回值并返回
        return new PageInfo(deptList);
    }

    @Override
    public List<String> findUserRole(String id) {
        return userDao.findUserRole(id);
    }

    @Override
    public void changeUserRole(String id, String[] roleIds) {
        //1.删除当前用户和角色的关联关系
        userDao.deleteUserRole(id);
        //2.遍历角色id的数组
        for (String roleId : roleIds) {
            //3.保存用户和角色的关联关系
            userDao.saveUserRole(id,roleId);
        }
    }

    @Override
    public User findByEmial(String email) {
        return userDao.findByEmial(email);
    }
}
