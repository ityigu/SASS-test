package com.ityi.service.cargo;


import com.github.pagehelper.PageInfo;
import com.ityi.domain.cargo.Export;
import com.ityi.domain.cargo.ExportExample;
import com.ityi.vo.ExportResult;


public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo findAll(ExportExample example, int page, int size);

    void updateE(ExportResult exportResult);
}
