package com.ityi.service.system;

import com.github.pagehelper.PageInfo;
import com.ityi.domain.system.Dept;

import java.util.List;
public interface DeptService {

    //select * from pe_dept where company_id = 当前登录用户的所属企业ID
    //查询所有 带分页
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    Dept findById(String id);

    //保存
    void save(Dept dept);

    //修改，更新
    void update(Dept dept);

    //根据id删除
    void delete(String id);

    //查询所有部门，不带分页
    List<Dept> findAll(String companyId);

}