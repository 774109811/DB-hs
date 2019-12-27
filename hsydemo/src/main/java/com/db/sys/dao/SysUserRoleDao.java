package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysUserRoleDao {
	
	//借助此dao操作sys_user_roles表
	//删除
	 int deleteObjectsByRoleId(Integer roleId);
	 /**
		 * 负责将用户与角色的关系数据写入到数据库
		 * @param userId 用户id
		 * @param roleIds 多个角色id
		 * @return
		 */
		int insertObject(
                @Param("userId") Integer userId,
                @Param("roleIds") Integer[] roleIds);
		//根据id 修改用户自身的信息 
		//基于用户的id删除用户和角色表中的关系数据
		//接收业务层数据(roleIds),将用户角色关系数据更新到数据库
		//上面定义了添加了就不用再添加了
		int deleteObjectsByUserId(Integer userId);

//		2)根据用户id查询角色id(可能是多个)
		List<Integer> findRoleIdsByUserId(Integer id);
		
	
		
}
