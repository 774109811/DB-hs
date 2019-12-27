package com.db.common.aspect;
import com.db.common.annotation.RequiresLog;
import com.db.common.utils.IPUtils;
import com.db.common.utils.ShiroUtils;
import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
/**
 * 使用@Aspect注解修饰的类为一个切面类型
 * 切面对象：封装扩展功能
 * 切面构成：
 * 1)切入点(Pointcut):切入扩展功能的点，连接点(一个具体方法)结合
 * 2)通知(Advice) 本质为一个实现了扩展功能的一个方法
 */
@Aspect
@Service
public class SysLogAspect {
	  
	  @Autowired
	  private SysLogDao sysLogDao;
      /**
       * 1)@Pointcut 注解用于定义切入点
       * 2)bean(...) 为一种切入点表达式(值为bean的id)
       */
	  //@Pointcut("bean(*ServiceImpl)") //粗粒度切入点表达式
	  //@Pointcut("bean(sysUserServiceImpl)")
	  //细粒度切入点表达式(可以精确到方法)多各切入点
	  @Pointcut("@annotation(com.db.common.annotation.RequiresLog)")
	  public void logPointCut(){}//空方法(相当于为切入点起了个别名)
	  /**
	   * 说明:
	   * 1)@Around 描述方法为一个环绕通知
	   *  JoinPoint 为一个连接点(切入点中的某个方法信息对象)
	   * 目标方法的执行结果
	   * Throwable
	   */
	  @Around("logPointCut()")
	  public Object around(ProceedingJoinPoint jp)//环绕通知
			  throws Throwable{
		  long start=System.currentTimeMillis();
		  Object result=jp.proceed();//执行目标方法(result为目标方法的执行结果)
		  long end=System.currentTimeMillis();
		  //System.out.println("execute time :"+(end-start));
		  saveObject(jp,end-start);//添加日志信息的方法和用时
		  return result;
	  }
	  //添加是谁修改用户的日志方法
	  private void saveObject(
			  ProceedingJoinPoint jp,
			  long totalTime) throws NoSuchMethodException, SecurityException{
		  //1.获取日志信息
		  //获取用户
		  String username=ShiroUtils.getUser().getUsername();
		  //获取IP
		  String ip=IPUtils.getIpAddr();
		  //获取方法签名信息(包含了方法名以及参数列表信息)
		  Signature s=jp.getSignature();
		  //System.out.println(s.getClass().getName());
		  MethodSignature ms=(MethodSignature)s;
		  //获取目标对象的类对象(字节码对象：反射的起点)
		  Class<?> targetCls=jp.getTarget().getClass();
		  //获取目标方法对象
		  Method targetMethod=targetCls.getDeclaredMethod(
		  ms.getName(),ms.getParameterTypes());
		  //获取目标方法上的RequiresLog注解
		  RequiresLog requiresLog=
		  targetMethod.getDeclaredAnnotation(RequiresLog.class);
		  //获取注解中value属性的值
		  String operation=requiresLog.value();
		  //获取方法
		  String method=targetCls.getName()+"."+targetMethod.getName();
		  System.out.println(method);
		   //获取链接点上的参数
		  String params=Arrays.toString(jp.getArgs());
		  System.out.println(params);
		  //2.封装日志信息(封装到SysLog对象)
		  SysLog log=new SysLog();
		  log.setUsername(username);
		  log.setIp(ip);
		  log.setOperation(operation);
		  log.setMethod(method);
		  log.setParams(params);
		  log.setTime(totalTime);
		  log.setCreatedTime(new Date());
		  //3.存储日志信息(写入到数据库)
		  int i = sysLogDao.insertObject(log);
		  System.out.println(i);
	  }
}











