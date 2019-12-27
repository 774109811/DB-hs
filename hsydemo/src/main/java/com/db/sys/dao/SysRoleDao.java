package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.CheckBox;

import com.db.sys.entity.SysRole;

public interface SysRoleDao {
	
	  /**
     * 基于条件(用户名)查询系统中的总记录数，
     * 我们会在业务层基于总记录数进行总页数
     * 的计算操作
     * @param username
     * @return
     */
	 int getRowCount(@Param("name") String name);
	 /**
	  * 基于条件查询当前页要显示的记录
	  * @param username 查询条件
	  * @param startIndex 当前页的起始位置
	  * @param pageSize 页面大小(每页要显示的记录数)
	  * @return
	  */
	 List<SysRole> findPageObjects(
             @Param("name") String name,
             @Param("startIndex") Integer startIndex,
             @Param("pageSize") Integer pageSize);
	 
	 int deleteObject(Integer id);
	 //添加角色对象
	 int insertObject(SysRole entity);
	 
	 //1）基于角色id查询角色信息修改页面呈现
	// 2）基于角色id查询菜单信息 
	 SysRole findObjectById(Integer id);
	 
	//	 业务描述
	//	 修改角色自身信息
	//	 修改角色与菜单的关系数据
	 int updateObject(SysRole entity);
	//	 业务描述
	//	 在SysRoleDao接口中添加一个查询角色ID,角色名的方法
	//
	//	 业务实现(方法定义)
	//	 1)方法名 findObjects
	//	 2)方法参数()
	//	 3)方法返回值 List<CheckBox>
	 List<CheckBox> findObjects();

	 
	 
}
