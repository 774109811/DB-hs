package com.db.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.db.common.exception.ServiceException;
import com.db.common.utils.ShiroUtils;
import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;

import com.db.sys.dao.SysRoleDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysRole;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysRoleService;
@Service
public class SysRoleServiceImpl implements SysRoleService {
	/**
	 * @Qualifier 要与@Autowired注解结合使用。
	 * 当为@Autowired修饰的属性按类型注入值，存
	 * 在多个类型时，可以借助@Qualifier指定其要
	 * 注入的bean的名字
	 */
	@Autowired

	private SysRoleDao sysRoleDao;

	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		//1.对方方法参数进行校验       当前页的页码值
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码不正确");//无效参数异常
		//2. 总行数(通过查询获得) 依据条件查询总记录数     
		int rowCount = sysRoleDao.getRowCount(name);
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
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//5.对查询结果进行封装并返回
		//5.1)构建PageObject对象
		PageObject<SysRole> pageObject = new PageObject<SysRole>();
		//5.2)封装数据
		pageObject.setRowCount(rowCount);//总行数(通过查询获得)
		pageObject.setRecords(records);//查询几行
		pageObject.setPageCurrent(pageCurrent);// 当前页的页码值
		pageObject.setPageSize(pageSize);//	页面大小
		int pageCount=(rowCount-1)/pageSize+1;//总行数-1/页面大小+1
		pageObject.setPageCount(pageCount);//总页数(通过计算获得)
		//5.返回封装结果。
		return pageObject;
	}

	public int deleteObject(Integer id) {

		if(id==null||id<1)
			throw new ServiceException("id值不正确"+id);
		int rows = sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录已经不存在");
		

			sysRoleMenuDao.deleteObjectsByRoleId(id);
			sysUserRoleDao.deleteObjectsByRoleId(id);
		
		return rows;
	}

	public int insertObject(SysRole entity, Integer[] menuIds) {
		//1.合法性验证
		if(entity==null)
			throw new ServiceException("保存数据不能为空");
		//角色名不能为空
		if(StringUtils.isEmpty(entity.getName()))
			throw new ServiceException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色赋予权限");
		
		 SysUser user = ShiroUtils.getUser();
		 entity.setCreatedUser(user.getUsername());
		
		//2.保存数据
		int rows=sysRoleDao.insertObject(entity);
		//保存校色和菜单的关系
		sysRoleMenuDao.insertObject(
				entity.getId(),menuIds);
		//3.返回结果
		return rows;
	}
//	业务描述：基于角色id查询角色及关联的菜单信息
//	1)通过参数变量接收控制层数据
//	2)对数据进行合法验证
//	3)基于参数数据查询角色信息
//	4)基于参数数据查询菜单信息
//	5)对数据进行封装并返回
//
//	业务实现：
//	在SysRoleService接口及实现类中定义基于id查询的方法
	
	public Map<String, Object> findObjectById(Integer id) {
		//1.合法性验证
    	if(id==null||id<=0)
    	throw new ServiceException("id的值不合法");
    	//2.执行查询
    	SysRole role=sysRoleDao.findObjectById(id);
    	System.out.println(role.getId());
  	//3.验证结果并返回
    	if(role==null)
    	throw new ServiceException("此记录已经不存在");
    	List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);
    	
    	System.out.println(id);
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("role", role);
    	map.put("menuIds", menuIds);
    	return map;
	}
//	业务描述
//	1)接收控制层数据,对数据进行合法验证.
//	2)调用dao层方法更新数据
//	3)验证结果,并返回.
//	业务实现(在SysRoleService接口及实现类中定义方法及实现.)
//	1)方法名 updateObject
//	2)参数列表 SysRole entity，Integer[] menuIds
//	3)返回值 int

	public int updateObject(SysRole entity, Integer[] menuIds) {
	 	//1.合法性验证
    	if(entity==null)
    throw new ServiceException("更新的对象不能为空");
    	if(entity.getId()==null)
    	throw new ServiceException("id的值不能为空");
    	
    	if(StringUtils.isEmpty(entity.getName()))
    	throw new ServiceException("角色名不能为空");
    	if(menuIds==null||menuIds.length==0)
    	throw new ServiceException("必须为角色指定一个权限");
    	 SysUser user = ShiroUtils.getUser();
		 entity.setModifiedUser(user.getUsername());
    	
    	//2.更新数据
    	int rows=sysRoleDao.updateObject(entity);
    	if(rows==0)
    throw new ServiceException("对象可能已经不存在");
    	sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
    	sysRoleMenuDao.insertObject(entity.getId(),menuIds);
 
    	//3.返回结果
    	return rows;
	}
//	业务描述
//	通过SysRoleService中的方法获取所有的角色id,角色名
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}






}
