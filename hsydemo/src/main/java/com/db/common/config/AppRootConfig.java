package com.db.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 问题：项目中如何对系统资源对象进行配置？两种
 * 1)基于xml
 * 2)基于annotation (注解)：jdk1.5新特性
 * 本应用采用基于注解方式对系统资源进行配置
 * 
 * 注解配置应用说明：
 * @ComponentScan 修饰配置类，用于定义要对
 * 哪些包中类型进行扫描，属性value指定具体
 * 要扫描的包，属性excludeFilters表示对扫描
 * 包中的哪些类不进行加载
 * 其中Controller.class,ControllerAdvice.class
 * 为web请求层spring 中的常用注解
 * */
@ComponentScan(//@Component,@Controller,@Service,@Configuration...
	value="com.db",
    excludeFilters={//要排除加载的类(例如使用controller注解修饰的类不进行加载)
    @Filter(type=FilterType.ANNOTATION,//约束classes属性中的内容
    		classes={Controller.class,
    				 ControllerAdvice.class})})//<context:component-scan base-package="com.jt"/>
@EnableAspectJAutoProxy//启用AOP切面
public class AppRootConfig {//取代spring-configs.xml
   
}









