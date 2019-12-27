package com.db.common.web;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.exception.ServiceException;
import com.db.common.vo.JsonResult;
/**
 * @ControllerAdvice 此注解描述的类
 * 称之为全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	 	@ExceptionHandler(ShiroException.class)
	 	@ResponseBody
		public JsonResult doHandleShiroException(ShiroException e) {
	 		JsonResult r = new JsonResult();
	 		r.setState(0);
	 		e.printStackTrace();
	 		if(e instanceof UnknownAccountException) { 
	 			r.setMessage("用户名不存在");
	 		}else if(e instanceof LockedAccountException) {
	 			r.setMessage("用户被禁用");
	 		}else if(e instanceof IncorrectCredentialsException) {
	 			r.setMessage("密码错误");
	 		}else if(e instanceof AuthorizationException) {
	 			r.setMessage("没有操作权限");
	 		}
	 		else {
	 			r.setMessage("");
	 		}
	 		
	 		
	 		return r;
	 	}
	
	
	
		/**
	   * @ExceptionHandler 对象描述的方法为一个异常处理方法
	   * @ExceptionHandler 内部定义的异常处理类型为本方法
	   * 可以处理的异常
	   * @param e
	   * @return
	   */
	  @ExceptionHandler(Exception.class)
	  @ResponseBody
	  public JsonResult doHandleException(
			  Exception e){
		  System.out.println("GlobalExceptionHandler.doHandleException");
		  e.printStackTrace();
		  //System.out.println(e instanceof ServiceException);
		  return new JsonResult(e);//封装异常
	  }
	  @ExceptionHandler(Throwable.class)
	  @ResponseBody
	  public JsonResult doHandleThrowable(
			  Throwable e){
		  e.printStackTrace();
		  JsonResult r=new JsonResult();
		  r.setState(0);
		  r.setMessage("系统运维中");
		  //给运维人员发短信
		  return r;//封装异常
	  }
	  
}
