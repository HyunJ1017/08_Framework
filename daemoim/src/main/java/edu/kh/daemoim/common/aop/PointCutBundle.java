package edu.kh.daemoim.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCutBundle {

	// 모든 컨트롤러 지정
	@Pointcut("execution(* edu.kh.daemoim..*Controller*.*(..))")
	public void controllerPointcut() {}
	
	// 모든 ServiceImpl 지정
	@Pointcut("execution(* edu.kh.daemoim..*ServiceImpl*.*(..))")
	public void serviceImplPointcut() {}
	
}
