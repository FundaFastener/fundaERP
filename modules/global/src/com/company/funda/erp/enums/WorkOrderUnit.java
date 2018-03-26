package com.company.funda.erp.enums;

import java.math.BigDecimal;

import javax.annotation.Nullable;

import com.company.funda.erp.util.UnitTransferUtil;
import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum WorkOrderUnit implements EnumClass<Integer> {

    PC(1){
    	@Override
    	public BigDecimal toPC(BigDecimal value,BigDecimal unitWeightG) {
    		return value;
    	}
    	@Override
    	public BigDecimal toKG(BigDecimal value,BigDecimal unitWeightG) {
    		return UnitTransferUtil.getKgFromPcs(value,unitWeightG);
    	}
		@Override
		public BigDecimal toSelf(BigDecimal value) {
			return toPC(value, null).setScale(0, BigDecimal.ROUND_FLOOR);
		}
    },
    KG(2){
    	@Override
    	public BigDecimal toPC(BigDecimal value,BigDecimal unitWeightG) {
    		return UnitTransferUtil.getPcsFromKg(value, unitWeightG);
    	}
    	@Override
    	public BigDecimal toKG(BigDecimal value,BigDecimal unitWeightG) {
    		return value;
    	}
		@Override
		public BigDecimal toSelf(BigDecimal value) {
			return toKG(value, null).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
    };

    private Integer id;

    WorkOrderUnit(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static WorkOrderUnit fromId(Integer id) {
        for (WorkOrderUnit at : WorkOrderUnit.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
    
    public abstract BigDecimal toPC(BigDecimal value,BigDecimal unitWeight);
    
    public abstract BigDecimal toKG(BigDecimal value,BigDecimal unitWeight);
    
    public abstract BigDecimal toSelf(BigDecimal value);
}