package com.db.common.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@MapperScan(basePackages="com.db.**.dao")
@Configuration//通过此注解声明这是配置文件
@EnableTransactionManagement
public class AppMyBatisConfig {
	 //假如bean没有指定名字,此bean的默认名字为方法名
	 /***
	  * 当spring框架调用如下方法时,要为方法参数传入
	  * 一个对象,默认首先是从容器按类型查找与此参数
	  * 相匹配的对象,假如有多个还会检测哪个bean的id
	  * 与参数匹配,有匹配的则直接赋值,没有则抛出异常.
	  * @param dataSource
	  * @return
	  * 外界假如通过sqlSessionFactory(
	  * @Bean注解中定义的key的名字)
	  * 获取对象, 默认会调用SqlSessionFactoryBean
	  * 对象的getObject方法来创建一个工厂对象,对外
	  * 提供具体应用.
	  */
	 @Bean("sqlSessionFactory")
	 public SqlSessionFactoryBean newSqlSesionFactoryBean(
		@Autowired DataSource dataSource)throws IOException{
		 //1.构建bean对象
		 SqlSessionFactoryBean fBean=
		 new SqlSessionFactoryBean();
		 //2.配置数据源
		 fBean.setDataSource(dataSource);
		 //3.设置映射文件
		 Resource[] mapperLocations=
         new PathMatchingResourcePatternResolver()
		 .getResources("classpath:mapper/*.xml");
		 fBean.setMapperLocations(mapperLocations);
         
		 return fBean;
	 }
	 
	 	// 配置事物管理对象
	 @Bean("txManager")
	 public DataSourceTransactionManager newTransactionManager(DataSource dataSource) {
		 
		 DataSourceTransactionManager tManager = new DataSourceTransactionManager();
		 tManager.setDataSource(dataSource);
		 return tManager;
	 }
	 
}




