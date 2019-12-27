package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.sys.entity.SysLog;

/**
 * 数据持久层对象(数据访问对象)
 * 
 * 借助此接口对象的实现类
 * 访问数据库中的日志信息
 * 
 */
public interface SysLogDao {
     /**
      * 基于条件(用户名)查询系统中的总记录数，
      * 我们会在业务层基于总记录数进行总页数
      * 的计算操作
      * @param username
      * @return
      */
	 int getRowCount(@Param("username") String username);
	 /**
	  * 基于条件查询当前页要显示的记录
	  * @param username 查询条件
	  * @param startIndex 当前页的起始位置
	  * @param pageSize 页面大小(每页要显示的多少条记录数)
	  * @param 动态sql语句必须命名
	  * @return
	  */
	 List<SysLog> findPageObjects(
             @Param("username") String username,
             @Param("startIndex") Integer startIndex,
             @Param("pageSize") Integer pageSize);
	//
	 //
	 //基于id进行删除行数
	 int deleteObjects(@Param("ids") Integer... ids);
	 
	 
	 //1）接收业务层数据(SysLog)
	// 2）将数据写入到数据库(ORM)
	 int insertObject(SysLog entity);
}





