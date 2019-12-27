package com.db.common.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import com.db.common.utils.ShiroUtils;

/**
 * 通过此切面在目标方法执行之前
 * 1)获取目标方法对象
 * 2)获取目标方法对象中的参数对象
 * 3)执行参数对象的set方法将登陆用户信息存储到实体类
 * @author ta
 */

@Aspect
@Service
public class SysDataAspect {
	
	/**
	 * @Before 修饰的方法为一个前置通知：在目标方法执行之前执行
	 * @param jp
	 * @throws Exception
	 */
	@Before("@annotation(com.db.common.annotation.RequiresData)")
	public void beforeMethod(JoinPoint jp) throws Exception{
		//获取目标方法签名
		MethodSignature ms=(MethodSignature)jp.getSignature();
		//获目标取方法名
		String methodName=ms.getName();
		System.out.println(methodName);
		//获取实际参数对象中的第1个参数
		Object entity=jp.getArgs()[0];
		Class<?> entityCls=entity.getClass();//获取这个类对象
		String username=ShiroUtils.getUser().getUsername();//获取用户名字
		//如果这个方法名为添加则把它的添加用户修改为当前用户
		if(methodName.startsWith("save")){
			Method m1=entityCls.getDeclaredMethod("setCreatedUser",String.class);
			m1.invoke(entity,username);
		//如果这个方法名为修改则把它的修改用户修改为当前用户	
		}else if(methodName.startsWith("update")){
			Method m2=entityCls.getDeclaredMethod("setModifiedUser",String.class);
			m2.invoke(entity, username);
		}
	}
}
