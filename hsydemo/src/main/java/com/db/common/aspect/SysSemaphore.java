package com.db.common.aspect;

import java.util.concurrent.Semaphore;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import com.db.common.cache.CacheKey;
import com.db.common.cache.ReadWriteLockLruCache;
import com.db.common.exception.ServiceException;

@Aspect
@Service
public class SysSemaphore {
	private Semaphore sh=new Semaphore(1,true);
	
	@Around("@annotation(com.db.common.annotation.xianliu)")
	public Object aroundSemaphore(ProceedingJoinPoint jp)//连接点 
			throws Throwable{
		sh.acquire();//获取一个信号量(令牌)
		CacheKey key=new CacheKey();//1,创建目标对象s
		Object target=jp.getTarget();//2,获取目标对象
		key.setTargetClass(target.getClass());//3,设置目标的类型为目标类
		MethodSignature ms=(MethodSignature)jp.getSignature();//4,获取方法签名
		key.setTargetMethod(ms.getMethod());//5,设置方法为目标方法
		key.setArgs(jp.getArgs());//6,设置参数为目标参数
	
		Object result = jp.proceed();//7.执行目标上的方法
		
		//sh.release();//释放信号量(令牌)
		return result;
	}
}
