package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
	//角色于菜单模块关系表
public interface SysRoleMenuDao {
	//创建基于菜单id删除记录的方法
	int deleteObjectsByMenuId(Integer menuId);
	//基于角色id在SysRoleMenuDao中添加根据角色id删除关系数据的方法
	int deleteObjectsByRoleId(Integer roleId);
	//因为一个角色对应很多菜单所以菜单id用数组接受
	//保存角色于菜单的数据
	int insertObject(
            @Param("roleId") Integer roleId,
            @Param("menuIds") Integer[] menuIds);
	//2）基于角色id查询关联的菜单权限信息 
	List<Integer> findMenuIdsByRoleId(Integer id);
	
	List<Integer> findMenuIdsByRoleIds(
            @Param("roleIds") Integer... roleIds);
	
}
