package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * 
 * @author Howard Chang
 * @Description :
 * 非擔當責任差異原因(NON-OPERATOR ACCOUNTABILITY (NOA)):
		(P)計劃停機/分 NOA_SCHEDULED_IDLE_TIME
		(S)自主停機/分 NOA_SELF_IDLE_TIME
		(M)人員請假  NOA_OPERATOR_ON_LEAVE
		上課/分 NOA_TRAINING
		RD件試樣/分 NOA_RD_SAMPLING
		(C)待線/分 NOA_WAITING_FOR_WIRE
		(E)待判/分  NOA_WAITING_FOR_QC 
		(Q)開會/分 NOA_MEETING
		(N)打掃/分 NOA_CLEANING
 * 屬擔當責任差異原因(OPERATOR ACCOUNTABILITY (OA)):

		(H)支援其他車台/分 OA_SUPPORTING_OTHERS
		(B)待模/分 OA_WAITING_FOR_MOLD 
		(D)機台損壞/分 OA_MACHINE_DOWN_TIME
		(K)機台調機/分 OA_MACHINE_ADJUSTMENT
		(R)試模/分 OA_TESTING_OF_TOOLING
		(F)其他/分 OA_MISCELLANEOUS 
 
 * 實際生產(PRODUCTION (P)):

		架車 P_MAJOR_SETUP 
		調機 P_MINOR_SETUP 
		生產 P_PRODUCTION 
 */
public enum WorkRecordStatus implements EnumClass<Integer> {

	P_MAJOR_SETUP(1),
	P_MINOR_SETUP(2),
	P_PRODUCTION(3),
	
	NOA_SCHEDULED_IDLE_TIME(100),
	NOA_SELF_IDLE_TIME(101),
	NOA_OPERATOR_ON_LEAVE(102),
	NOA_TRAINING(103),
	NOA_RD_SAMPLING(104),
	NOA_WAITING_FOR_WIRE(105),
	NOA_WAITING_FOR_QC(106),
	NOA_MEETING(107),
	NOA_CLEANING(108),
	
	OA_SUPPORTING_OTHERS(200),
	OA_WAITING_FOR_MOLD(201),
	OA_MACHINE_DOWN_TIME(202),
	OA_MACHINE_ADJUSTMENT(203),
	OA_TESTING_OF_TOOLING(204),
	OA_MISCELLANEOUS(205);

    private Integer id;

    WorkRecordStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static WorkRecordStatus fromId(Integer id) {
        for (WorkRecordStatus at : WorkRecordStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
    
}