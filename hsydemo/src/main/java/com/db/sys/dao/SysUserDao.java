package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.SysUserDeptResult;
import com.db.sys.entity.SysUser;

public interface SysUserDao {
	//基于id修改禁用状态和修改时间和修改的人
	int validById(
            @Param("id") Integer id,
            @Param("valid") Integer valid,
            @Param("modifiedUser") String modifiedUser);
	
	
	//基于用户名查询所有记录总数
	int getRowCount(@Param("username") String username); 
	
	//基于用户名查询当前页记录
	/**
	 * 
	 * @param username
	 * @param startIndex起始页位置
	 * @param pageSize页面大小
	 * @return
	 */
	List<SysUserDeptResult> findPageObjects(
            @Param("username") String username,
            @Param("startIndex") Integer startIndex,
            @Param("pageSize") Integer pageSize);
	
	
	/**
	 * 负责将用户信息写入到数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysUser entity);
	
	//1)根据用户id查询用户以及部门信息
	SysUserDeptResult findObjectById(Integer id);
	//根据id 修改用户自身的信息 
	//基于用户的id删除用户和角色表中的关系数据
	//接收业务层数据(roleIds),将用户角色关系数据更新到数据库
	int updateObject(SysUser entity);
	
	
	//查询有没有同名的和相同手机号和邮箱
	int findObjectByUser(
            @Param("colName") String colName,
            @Param("colValue") String colValue);
	
	
	
	SysUser findUserByUserName(String username);
	
}
