package com.company.funda.erp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.company.funda.erp.util.PrintUtil;

@Component
@Aspect
public class ServeLog {
	
	private Logger logger = LoggerFactory.getLogger("serviceLogger");
	
	@Before("within(com.company.funda.erp.service.*)")
	public void inServiceLayer(JoinPoint joinPoint) {
		
		Class<?> classService = joinPoint.getTarget().getClass();
		MDC.put("serviceName", classService.getSimpleName());
		logger.info("******************** Start {}.{} ********************",classService.getSimpleName(),joinPoint.getSignature().getName());
		logger.info("joinPoint:{}",joinPoint.getArgs().length);
		for(Object arg : joinPoint.getArgs()) {
			logger.info("argument :{}",PrintUtil.printMultiLine(arg));
		}
		
	}
	
	@After("within(com.company.funda.erp.service.*)")
	public void inAfterServiceLayer(JoinPoint joinPoint) {
		Class<?> classService = joinPoint.getTarget().getClass();
		logger.info(" ***** END {}.{} ***** ",classService.getSimpleName(),joinPoint.getSignature().getName());
	}

}
