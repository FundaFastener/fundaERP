package com.company.funda.erp.service;


import com.company.funda.erp.Exception.ImportFileEofEvaluationException;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.FileDescriptor;

import java.util.Collection;
import java.util.Map;

public interface ImportExcelService {
    String NAME = "fe_ImportExcelService";

    //preview of yearAndMonth

    //import selected
    

    /**
     * @param params
     */
    String doImport(Map<String, Object> params);

    Collection<BaseGenericIdEntity> preview(FileDescriptor fileDescriptor,Map<String, Object> params) throws Exception;

}