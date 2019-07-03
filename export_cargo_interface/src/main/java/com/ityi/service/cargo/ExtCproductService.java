package com.ityi.service.cargo;



import com.github.pagehelper.PageInfo;
import com.ityi.domain.cargo.ExtCproduct;
import com.ityi.domain.cargo.ExtCproductExample;

/**

 */
public interface ExtCproductService {
	/**
	 * 保存
	 */
	void save(ExtCproduct ExtCproduct);

	/**
	 * 更新
	 */
	void update(ExtCproduct ExtCproduct);

	/**
	 * 删除
	 */
	void delete(String id);

	/**
	 * 根据id查询
	 */
	ExtCproduct findById(String id);

	/**
	 * 分页查询
	 */
	PageInfo findAll(ExtCproductExample example, int page, int size);
}
