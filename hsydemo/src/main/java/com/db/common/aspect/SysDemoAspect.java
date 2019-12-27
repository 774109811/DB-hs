package com.db.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Order(1)//数字越小优先级越高
@Aspect
@Service
public class SysDemoAspect {

	@Pointcut("execution(* com.db.sys.service.impl.*.*(..))")
	public void demoPointCut(){}

	@Around("demoPointCut()")
	public Object aroundMethod(ProceedingJoinPoint jp)
			throws Throwable{
		System.out.println("@Around");
		Object result=jp.proceed();
		//System.out.println("@Around-end");
		return result;
	}

	@Before("demoPointCut()")
	public void beforeMethod(){
		System.out.println("@Before");
	}
	@After("demoPointCut()")
	public void afterMethod(){
		System.out.println("@After");
	}
	@AfterReturning("demoPointCut()")
	public void afterReturning(){
		System.out.println("@AfterReturning");
	}
	@AfterThrowing("demoPointCut()")
	public void afterThrowing(){
		System.out.println("@AfterThrowing");
	}


}
