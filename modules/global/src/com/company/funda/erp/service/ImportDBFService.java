package com.company.funda.erp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.enums.DbfImportType;
import com.haulmont.cuba.core.entity.StandardEntity;

public interface ImportDBFService {
    String NAME = "fe_ImportDBFService";
    
    String TYPE_KEY = "DBF_BEAN_TYPE_KEY";
    
    String DATE_FROM = "DATE_FROM";
    
    String DATE_TO = "DATE_TO";
    
    String NO = "NO";
    
    String WORK_ORDER_NO = "WORK_ORDER_NO";
    
    String INVENTORY_ITEM_NO = "INVENTORY_ITEM_NO";
    
    String SKIP_DATE_CHECK = "SKIP_DATE_CHECK";
    
    /**
     * 
     * @param params
     * @return
     */
    <E extends StandardEntity>List<E> loadFromDBF(Map<String, Object> params);
    
    int setInventoryItemToDb(Set<InventoryItem> datas,DbfImportType dbfImportType);
    
    int setMachineToDb(Set<Machine> datas,DbfImportType dbfImportType);
    
    Map<String,String> setWorkOrderToDb(Set<WorkOrder> datas,DbfImportType dbfImportType);
}