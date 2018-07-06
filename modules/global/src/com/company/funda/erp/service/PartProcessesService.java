package com.company.funda.erp.service;

import com.company.funda.erp.entity.PartProcessesStandard;
import com.haulmont.bali.datastruct.Pair;

public interface PartProcessesService {
    String NAME = "fe_PartProcessesService";
    
    Pair<Boolean,String> newPartProcessesStandard(PartProcessesStandard partProcessesStandard);
    
    Pair<Boolean,String> editPartProcessesStandard(PartProcessesStandard partProcessesStandard);
    
    Pair<Boolean,String> deletePartProcessesStandard(PartProcessesStandard partProcessesStandard);
}