package com.db.sys.service;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;
import com.db.common.vo.SysUserDeptResult;
import com.db.sys.entity.SysUser;


public interface SysUserService {
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
	 PageObject<SysUserDeptResult> findPageObjects(
             String username,
             Integer pageCurrent);
	 
	
	 
	 //实现点击禁用修改参数根据id 修改  修改的用户和修改的时间
	 int validById(
             Integer id,
             Integer valid);
	 
	 int saveObject(SysUser entity, Integer[] roleIds);
//	 1)获取控制层数据(userId),并对其进行合法性验证
//	 2)调用dao方法根据用户id查询用户信息,部门信息以及对应的角色信息
//	 3)对查询到的结果数据进行封装,并返回.
	 //查询用户以及关联信息
	 //放入用户自己的属性和关联表的角色id
	 Map<String, Object> findObjectById(
             Integer userId);
	 
	 
	 int updateObject(SysUser entity,
                      Integer[] roleIds);
	 
	 //查询有没有同名的和相同手机号和邮箱
	 int findObjectByUser(String colName, String colValue);
}



