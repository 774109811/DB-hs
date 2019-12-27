package com.db.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;
@Repository

/**SysLogDaoImpl ref -->sqlSessionFactory
 * SysLogDaoImpl <--di <--spring ioc
 * @author tarena
 *
 */
public class SysLogDaoImpl implements SysLogDao{
	/**
	 * @Autowired 用于修饰 属性,set,构造方法 
	 * 目的告诉spring默认按类型(属性类型,参数类型)
	 * 为属性赋值,假如类型有多个还可以参考名字为对象注入值
	 */
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	public int getRowCount(String username) {
		//获取sqlsession对象
		SqlSession session = sqlSessionFactory.openSession();
		//执行sql语句
		
		SysLogDao dao = session.getMapper(SysLogDao.class);
		//释放
		int Count = dao.getRowCount(username); 
		
		//String statement ="com.db.sys.SysLogDao";
		

		
		return Count;
	}

	public List<SysLog> findPageObjects(String username, Integer startIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public int deleteObjects(Integer... ids) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertObject(SysLog entity) {
		// TODO Auto-generated method stub
		return 0;
	}



}
