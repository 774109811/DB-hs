package com.db.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.db.common.annotation.RequiresData;
import com.db.common.annotation.RequiresLog;
import com.db.common.exception.ServiceException;
import com.db.common.vo.Node;
import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.entity.SysMenu;
import com.db.sys.service.SysMenuService;
/**
 * 声明式事务控制(底层借助代理机制控制事务)
 * 1)基于xml(了解)
 * 2)基于注解(趋势,例如spring boot)
 * 注解方式的声明式事务控制需要借助
 * @Transactional
 * 注解对类或方法进行描述。
 * rollback-for	用于指定能够触发事务回滚的异常类型，
 * 如果有多个异常类型需要指定，各类型之间可以通过逗号分隔。
 * timeout   事务的超时时间，默认值为-1。不超时 6秒钟超时
 * 如果超过该时间限制但事务还没有完成，则自动回滚事务。
 * read-only 指定事务是否为只读事务，默认值为 false；为了忽略那些不需要事务的方法，
 * 比如只读取数据，可以设置 read-only 为 true。
 * isolation	事务的隔离度，默认值采用 DEFAULT。
 * READ_COMMITTED读取已提交的
 * 脏读:还没有提交,就读到了 12306允许
 */

@Transactional(rollbackFor=Throwable.class,
timeout=6,isolation=Isolation.READ_COMMITTED)

@Service
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	//propagation 事务的传播行为，默认值为 REQUIRED。读写操作
	@RequiresLog("菜单查询")
	@Transactional(readOnly=false,propagation=Propagation.REQUIRES_NEW)
	public List<Map<String, Object>> findObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		if(list==null||list.size()==0) {
			throw new ServiceException("没有数据");
		}
		return list ;
	}
	/**
	 * 声明式事务( 底层通过代理机制控制事务 )
	 */
	@Transactional(timeout=6)
	public int deleteObject(Integer id) {
		//1.验证数据的合法性
		if(id==null||id==0)
			throw new ServiceException("请先选择一个要删除的对象");
		//2.基于id进行子元素查询
		int rowcount = sysMenuDao.getChildCount(id);
		if(rowcount>0) 
			throw new ServiceException("请先删除子菜单");
		
		//3.删除菜单元素
		int rows = sysMenuDao.deleteObject(id);
		if(rows==0)
		throw new ServiceException("此对象已经不存在了");
		//4.删除角色,菜单关系数据
		int rows1 = sysRoleMenuDao.deleteObjectsByMenuId(id);
		if(rows1>1)
			throw new ServiceException("没有删除相关对象");
		return rows;
	}
	/**
	 * 假如是读事务，建议readOnly属性的值为true，
	 * readOnly默认为false
	 */
	@Transactional(readOnly=true)
	public List<Node> findZtreeMenuNodes() {
		List<Node> list = sysMenuDao.findZtreeMenuNodes();
		if(list==null||list.size()==0)
			throw new ServiceException("查询为空,没有数据");
		return list;
	}
	@RequiresData
	public int saveObject(SysMenu entity) {
				//1.合法验证
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
				throw new ServiceException("菜单名不能为空");
				int rows;
				//2.保存数据
				try{
					//往数据库添加数据
				rows=sysMenuDao.insertObject(entity);
				}catch(Exception e){
					//不成功就抛出异常
				e.printStackTrace();
				throw new ServiceException("保存失败");
				}
				//3.返回数据
				return rows;
	}
	@RequiresData
	public int updateObject(SysMenu entity) {
		//1.合法验证
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("菜单名不能为空");
		int rows;
		//2.保存数据
		try{
			//往数据库添加数据
			rows=sysMenuDao.updateObject(entity);
		}catch(Exception e){
			//不成功就抛出异常
			e.printStackTrace();
			throw new ServiceException("保存失败");
		}
		//3.返回数据
		return rows;
	}

}
