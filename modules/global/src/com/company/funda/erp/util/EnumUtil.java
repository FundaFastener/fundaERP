package com.company.funda.erp.util;

import com.company.funda.erp.enums.OperateType;

public class EnumUtil {

	@SafeVarargs
	public static <E extends Enum<E>> boolean equalIn(E source,E ...targets){
		boolean result = false;
		for(E target:targets) {
			if(source == target) {
				return true;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(EnumUtil.equalIn(OperateType.AUTO, OperateType.DETACH,OperateType.INTERIM,OperateType.MANUAL));
		System.out.println(EnumUtil.equalIn(OperateType.AUTO, OperateType.DETACH));
		System.out.println(EnumUtil.equalIn(OperateType.AUTO, OperateType.DETACH,OperateType.INTERIM,OperateType.AUTO,OperateType.MANUAL));
		System.out.println(EnumUtil.equalIn(OperateType.AUTO, OperateType.AUTO,OperateType.MANUAL));
	}
}
