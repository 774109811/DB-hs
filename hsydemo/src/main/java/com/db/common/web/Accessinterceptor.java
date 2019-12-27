package com.db.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.db.common.exception.ServiceException;
//拦截处理 AppMvcConfig
public class Accessinterceptor extends HandlerInterceptorAdapter {
	//控制层方法执行之前
	//结果代表请求是继续向下执行还是到此为止
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("++preHandle++");
		long time= System.currentTimeMillis();
		//设置访问时间
	
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.HOUR_OF_DAY, 4);//设置时
		c1.set(Calendar.MINUTE, 30);//分
		c1.set(Calendar.SECOND, 0);//秒
		long startTime = c1.getTimeInMillis();//当前时间
		c1.set(Calendar.HOUR_OF_DAY, 20);
		long endTime = c1.getTimeInMillis();//结束时间
		if(startTime>time||endTime<time) {
			throw new ServiceException("彪彪不要P 8:25--17:30允许登录");
		
		}
		
		return true;
	}
}
