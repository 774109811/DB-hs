 package com.db.sys.service.impl;

import com.db.common.annotation.RequiresCacheKey;
import com.db.common.annotation.RequiresLog;
import com.db.common.exception.ServiceException;
import com.db.common.utils.ShiroUtils;
import com.db.common.vo.PageObject;
import com.db.common.vo.SysUserDeptResult;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SysUserServiceImpl implements SysUserService {
	  /**
     * @Qualifier 要与@Autowired注解结合使用。
     * 当为@Autowired修饰的属性按类型注入值，存
     * 在多个类型时，可以借助@Qualifier指定其要
     * 注入的bean的名字
     */
	@Autowired
	
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	
	@RequiresCacheKey//这个注解自己定义的 有个这个注解点击用户第一次在数据库取,第二次就在缓存中取
	public PageObject<SysUserDeptResult> findPageObjects(String username, Integer pageCurrent) {
		System.out.println("LOG,findPageObjects");
		//1.对方方法参数进行校验       当前页的页码值
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码不正确");//无效参数异常
		//2. 总行数(通过查询获得) 依据条件查询总记录数     
		int rowCount = sysUserDao.getRowCount(username);
		//3.对总记录数进行校验(等于0表示没有记录)
		if(rowCount==0)
		throw new ServiceException("记录不存在");//自己创建的异常增强异常代码的可读性
		//throw new RuntimeException("记录不存在");//运行时异常
		//4.依据条件查询当前页要显示的记录(pageSize定义为2)
		//4.1)定义pageSize页面大小
		int pageSize=3;
		//4.2)计算startIndex
		int startIndex=(pageCurrent-1)*pageSize;
		//4.3)执行当前数据的查询操作
		List<SysUserDeptResult> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
		//5.对查询结果进行封装并返回
		//5.1)构建PageObject对象
		PageObject<SysUserDeptResult> pageObject = new PageObject<SysUserDeptResult>();
		 //5.2)封装数据
		pageObject.setRowCount(rowCount);//总行数(通过查询获得)
		pageObject.setRecords(records);//查询的所有数据行
		pageObject.setPageCurrent(pageCurrent);// 当前页的页码值
		pageObject.setPageSize(pageSize);//	页面大小
		int pageCount=(rowCount-1)/pageSize+1;//总行数-1/页面大小+1
		pageObject.setPageCount(pageCount);//总页数(通过计算获得)
		//5.返回封装结果。
		return pageObject;
	}
	//在需要进行授权检测的方法上添加执行此方法需要的权限标识
	@RequiresLog(value="用户启用禁用")
	@RequiresPermissions("sys:log:delete")
	public int validById(Integer id, Integer valid) {
				//1.合法性验证
				if(id==null||id<=0)
				throw new ServiceException("参数不合法,id="+id);
				if(valid!=1&&valid!=0)
				throw new ServiceException("参数不合法,valie="+valid);
//				if(StringUtils.isEmpty(modifiedUser))
//				throw new ServiceException("修改用户不能为空");
				
				 SysUser user = ShiroUtils.getUser();
				//2.执行禁用或启用操作
				int rows=0;
				try{
			    rows=sysUserDao.validById(id, valid, user.getUsername());
				}catch(Throwable e){
				e.printStackTrace();
				//报警,给维护人员发短信
				throw new ServiceException("底层正在维护");
				}
				//3.判定结果,并返回
				if(rows==0)
				throw new ServiceException("此记录可能已经不存在");
				return rows;
	}

	public int saveObject(SysUser entity, Integer[] roleIds) {
			//1.验证数据合法性
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getUsername()))
			    throw new ServiceException("用户名不能为空");
				if(StringUtils.isEmpty(entity.getPassword()))
				throw new ServiceException("密码不能为空");
				if(StringUtils.isEmpty(entity.getDeptId()))
					throw new ServiceException("部门不能为空");
		        if(roleIds==null || roleIds.length==0)
				throw new ServiceException("至少要为用户分配角色");

				//2.将数据写入数据库
				String salt=UUID.randomUUID().toString();
				entity.setSalt(salt);
				//加密(先了解,讲shiro时再说)
				SimpleHash sHash=
			    new SimpleHash("MD5",entity.getPassword(), salt,1);
				entity.setPassword(sHash.toString());

				int rows=sysUserDao.insertObject(entity);
			    sysUserRoleDao.insertObject(
						entity.getId(),
						roleIds);//"1,2,3,4";
				//3.返回结果
				return rows;
	}

	
//	1)获取控制层数据(userId),并对其进行合法性验证
//	2)调用dao方法根据用户id查询用户信息,部门信息以及对应的角色信息
//	3)对查询到的结果数据进行封装,并返回.
	//放入用户自己的属性和关联表的角色id
	public Map<String, Object> findObjectById(Integer userId) {
		//1.合法性验证
		if(userId==null||userId<=0)
		throw new ServiceException(
		"参数数据不合法,userId="+userId);
		//2.业务查询
		SysUserDeptResult user=
		sysUserDao.findObjectById(userId);
		if(user==null)
		throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(userId);
		//3.数据封装
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}

	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.合法验证
	    if(entity==null)
	    throw new ServiceException("用户信息不能为空");
	    if(StringUtils.isEmpty(entity.getUsername()))
	    throw new ServiceException("用户名不能为空");
	    //用户名已经存在的验证,尝试自己实现.
	    if(StringUtils.isEmpty(roleIds))
	    throw new ServiceException("用户必须选一个角色");
	    
		//2.更新数据
	    int rows=0;
	    try{
	    rows=sysUserDao.updateObject(entity);
	    sysUserRoleDao.deleteObjectsByUserId(entity.getId());
	    sysUserRoleDao.insertObject(
	    entity.getId(),roleIds);
	    }catch(Throwable e){
	     e.printStackTrace();
	     //发起报警信息
	     throw new ServiceException("服务端现在异常,请稍后访问");
	    }
		//3.返回结果
		return rows;
	}
	//查询有没有同名的和相同手机号和邮箱
	public int findObjectByUser(String colName, String colValue) {
		if(StringUtils.isEmpty(colName))
			throw new IllegalArgumentException("参数名不能为空");
		if(StringUtils.isEmpty(colValue))
			throw new IllegalArgumentException("参数值不能为空");
	
		int rows = sysUserDao.findObjectByUser(colName, colValue);
		if(rows>0)
			throw new IllegalArgumentException("数据已存在");
		
		
		return rows;
	}

	
	
}
