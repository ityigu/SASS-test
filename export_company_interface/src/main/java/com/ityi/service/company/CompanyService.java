package com.ityi.service.company;

import com.github.pagehelper.PageInfo;
import com.ityi.common.entity.PageResult;
import com.ityi.domain.system.Company;


import java.util.List;

/**
 */
public interface CompanyService {

    /**
     * 查询所有
     * @return
     */
    List<Company> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 保存
     * @param company
     */
    void save(Company company);

    /**
     * 更新
     * @param company
     */
    void update(Company company);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 查询带有分页的结果集
     * @param page 当前页
     * @param size 页大小
     * @return
     */
    PageResult findPage(int page, int size);

    /**
     * 使用PageHelper插件实现分页
     * @param page
     * @param size
     * @return
     */
    PageInfo findByPageHelper(int page, int size);
}
