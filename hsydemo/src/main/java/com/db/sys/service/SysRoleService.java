package com.db.sys.service;



import java.util.List;
import java.util.Map;

import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;

import com.db.sys.entity.SysRole;

public interface SysRoleService {
     /**
      * 通过此方法执行日志分页查询
      * 1)获取总记录数
      * 2)获取当前页的记录
      * 3)计算总页数
      * 4)封装查询结果
      * @param username 查询条件(基于用户名查询日志行为数据)
      * @param pageCurrent 分页条件(当前页的页码值)
      * @return
      */
	 PageObject<SysRole> findPageObjects(
             String name,
             Integer pageCurrent);
	 
	
	 int deleteObject(Integer id);
	 //添加校色对象和对应的菜单值
	 int insertObject(SysRole entity, Integer[] menuIds);
	 
	 Map<String,Object> findObjectById(Integer id) ;
	 
	 
	 int updateObject(SysRole entity, Integer[] menuIds);
	 
//	 业务描述
//	 通过SysRoleService中的方法获取所有的角色id,角色名
//	 业务实现(在SysRoleService接口及实现中定义方法)
	 List<CheckBox> findObjects();
}



