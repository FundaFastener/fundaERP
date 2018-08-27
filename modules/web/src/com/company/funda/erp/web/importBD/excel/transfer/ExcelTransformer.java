package com.company.funda.erp.web.importBD.excel.transfer;

import com.haulmont.cuba.core.entity.StandardEntity;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

public interface ExcelTransformer {
    <E extends StandardEntity> Map<String,String> transfer(Sheet sheet,List<E> list);
}
