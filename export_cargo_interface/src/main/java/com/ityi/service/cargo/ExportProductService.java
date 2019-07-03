package com.ityi.service.cargo;


import com.github.pagehelper.PageInfo;
import com.ityi.domain.cargo.ExportProduct;
import com.ityi.domain.cargo.ExportProductExample;

import java.util.List;


public interface ExportProductService {

	ExportProduct findById(String id);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo findAll(String companyId, int page, int size);

	List<ExportProduct> findAll(ExportProductExample exportProductExample);
}
