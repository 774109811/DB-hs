package com.db.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.apache.shiro.mgt.SecurityManager;

/**
 * 此类为shiro配置类对象
 * 
 */
@Configuration
public class AppShiroConfig {
	
	//Spring 集成shiro框架的基本应用 认证配置接受
	@Bean("securityManager")//返回给用户认证认证管理器
	public DefaultWebSecurityManager  newDefaultWebSecurityManager( 
			AuthorizingRealm userRealm){
			DefaultWebSecurityManager sManager=
			new DefaultWebSecurityManager();
			//此时必须保证realm对象已经存在了
			sManager.setRealm(userRealm);
			return sManager;
	}
	//配置安全过滤器
	@Bean("shiroFilterFactory")
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(
				SecurityManager securityManager){//shiro 包
			ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
			bean.setSecurityManager(securityManager);
		    //当此用户是一个非认证用户,需要先登陆进行认证
			//没有认证的话就跳转到登陆页面
			bean.setLoginUrl("/doLoginUI.do");
			LinkedHashMap<String,String> fcMap=
			new LinkedHashMap<String, String>();
			//webapp目录下的文件，anon表示允许匿名访问
			fcMap.put("/bower_components/**","anon");
			fcMap.put("/build/**", "anon");
			fcMap.put("/dist/**","anon");
			fcMap.put("/plugins/**","anon");
			//登陆页面允许访问
			fcMap.put("/user/doLogin.do","anon");
			//doLogout.do  shiro给的
			fcMap.put("/user/doLogout.do ","anon");//logout给的
			
			//其他的都需要
			fcMap.put("/**", "authc");//必须授权才能访问
			//设置过滤
			bean.setFilterChainDefinitionMap(fcMap);
			return bean;
	}
	/**
	 * 负责shiro中相关bean对象(代理对象
	 * )的声明周期管理
	 */
	
	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor newLifecycleBeanPostProcessor(){
			return new LifecycleBeanPostProcessor();
	}
	
	 // 通过此对象底层创建代理对象(需要进行授权访问的service)
	@DependsOn(value="lifecycleBeanPostProcessor")
	@Bean
	public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator(){
			return new DefaultAdvisorAutoProxyCreator();
	}

	/**
	 * 授权属性设置(例如由谁进行授权操作等)
	 * securityManager
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor newAuthorizationAttributeSourceAdvisor(
				SecurityManager securityManager){
			    AuthorizationAttributeSourceAdvisor bean=
				new AuthorizationAttributeSourceAdvisor();
			    bean.setSecurityManager(securityManager);
			return bean;
		}
	
	
}
