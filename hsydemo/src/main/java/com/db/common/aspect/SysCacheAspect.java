package com.db.common.aspect;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import com.db.common.cache.CacheKey;
import com.db.common.cache.ReadWriteLockLruCache;
import com.db.common.cache.SynchronizedLruCache;
//缓存
//将经常不适用的缓存删除面向切面
//这个注解自己定义的 有个这个注解点击用户第一次在数据库取,第二次就在缓存中取
@Aspect
@Service
public class SysCacheAspect {
	 //private Map<CacheKey,Object> cacheMap=new ConcurrentHashMap<>();
	//private SynchronizedLruCache<CacheKey,Object> cacheMap=new SynchronizedLruCache<>(3);
	//基于读写锁实现的LRUCache
	 private ReadWriteLockLruCache<CacheKey,Object> cacheMap
	 =new ReadWriteLockLruCache<CacheKey, Object>(3); 
	 @Around("@annotation(com.db.common.annotation.RequiresCacheKey)")
	 public Object aroundCache(ProceedingJoinPoint jp)//连接点 
	 throws Throwable{
		 
		 CacheKey key=new CacheKey();//1,创建目标对象
		 Object target=jp.getTarget();//2,获取目标对象
		 key.setTargetClass(target.getClass());//3,设置目标的类型为目标类
		 MethodSignature ms=(MethodSignature)jp.getSignature();//4,获取方法签名
		 key.setTargetMethod(ms.getMethod());//5,设置方法为目标方法
		 key.setArgs(jp.getArgs());//6,设置参数为目标参数
		 
		 Object result=cacheMap.get(key);//8,获取map里的类 方法和参数
		 if(result!=null){//如果不是空的也直接返回这个方法结果
			 System.out.println("data from cache");
			 return result;
		 }
		 //7,调用目标方法(这个步骤会通过service访问数据库)
		 result=jp.proceed();//执行目标方法
		 //9,将数据放入缓存,访问后将这个方法和结果放入map里,下次用直接调用
		 cacheMap.put(key,result);
		 return result;
	 }
}
