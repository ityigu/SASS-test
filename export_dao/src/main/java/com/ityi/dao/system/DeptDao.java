package com.ityi.dao.system;

import com.ityi.domain.system.Dept;

import java.util.List;

public interface DeptDao {

    //select * from pe_dept where company_id = 当前登录用户的所属企业ID
    //查询所有
    List<Dept> findAll(String companyId);
    //根据id查询
    Dept findById(String id);
    //保存
    void save(Dept dept);
    //修改，更新
    void update(Dept dept);
    //根据id删除
    void delete(String id);

}
