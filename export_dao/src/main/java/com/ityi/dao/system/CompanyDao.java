package com.ityi.dao.system;

import com.ityi.domain.system.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
* 业务持久层接口
* */
public interface CompanyDao {
    /*
    * 查询所有
    * */
    List<Company> findAll();

    /*
    * 根据id查询
    v       * */
    Company findById(String id);

    /*
    * 保存
    * */
    void save(Company company);

    /*
    * 更新
    * */
    void update(Company company);

    /*
    * 删除
    * */
    void delete(String id);

    /*
    * 查询总记录条数
    * */
    int findTotal();

    /*
    *查询带有分页结果集
    * */
    List<Company> findPage(@Param("page")int page,@Param("size") int size);

}
