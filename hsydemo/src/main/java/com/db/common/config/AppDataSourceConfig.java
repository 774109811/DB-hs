package com.db.common.config;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
//系统底层读取properties时会将内容封装到Envionment对象
@PropertySource("classpath:configs.properties")
/**在此类中进行相关数据源的配置*/
@Lazy
@Configuration //等效于@Controller,@Service,@Component
public class AppDataSourceConfig {
  /*static{
		System.out.println("static{}");
	}
	public AppDataSourceConfig() {
		System.out.println("AppDataSourceConfig() ");
	}
	*/
	 /**系统底层会将@Bean注解修饰的方法创建的对象Environment交给
	  * spring框架管理*/
	 @Lazy(false)
	 @Bean(value="dataSource",initMethod="init",
	 destroyMethod="close")//等效于<bean id="" class="">
	 public DataSource newDruidDataSource(
			 Environment env){
	   //System.out.println("==newDruidDataSource==");
	   DruidDataSource ds=new DruidDataSource();
	   ds.setDriverClassName(env.getProperty("jdbcDriver"));
	   ds.setUrl(env.getProperty("jdbcUrl"));
	   ds.setUsername(env.getProperty("jdbcUser"));
	   ds.setPassword(env.getProperty("jdbcPassword"));
	   return ds;
	 }//何时使用bean标签描述bean对象呢?第三方API对象

	 /**整合c3p0连接池*/
	 @Bean("c3p0")
     public DataSource newC3p0DataSource(
    		 Environment env)throws Exception{
		 ComboPooledDataSource ds=
		 new ComboPooledDataSource();
		 ds.setDriverClass(env.getProperty("jdbcDriver"));
		 ds.setJdbcUrl(env.getProperty("jdbcUrl"));
		 ds.setUser(env.getProperty("jdbcUser"));
		 ds.setPassword(env.getProperty("jdbcPassword"));
    	 return ds;
     }




}






