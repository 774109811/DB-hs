package com.db.sys.service.impl;

import com.db.common.exception.ServiceException;
import com.db.common.vo.PageObject;
import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;
import com.db.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysLogServiceImpl implements SysLogService {
	  /**
     * @Qualifier 要与@Autowired注解结合使用。
     * 当为@Autowired修饰的属性按类型注入值，存
     * 在多个类型时，可以借助@Qualifier指定其要
     * 注入的bean的名字
     */
	@Autowired
	@Qualifier("sysLogDao")
	private SysLogDao sysLogDao;
	
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		//1.对方方法参数进行校验       当前页的页码值
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码不正确");//无效参数异常
		//2. 总行数(通过查询获得) 依据条件查询总记录数     
		int rowCount = sysLogDao.getRowCount(username);
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
		List<SysLog> records = sysLogDao.findPageObjects(username, startIndex, pageSize);
		//5.对查询结果进行封装并返回
		//5.1)构建PageObject对象
		PageObject<SysLog> pageObject = new PageObject<SysLog>();
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
	@RequiresPermissions("sys:user:valid")
	public int deleteObjects(Integer... ids) {
		//1.对参数进行校验
		if(ids==null||ids.length==0)
			throw new IllegalArgumentException("请先选中要删除的对象");
		//2.执行删除操作
		int rows=0;	
		try {//通过dao访问数据库服务器可能会有异常,万一服务中断
			rows = sysLogDao.deleteObjects(ids);
		}catch (Exception e) {
			e.printStackTrace();
			//给运维人员发短信
			throw new ServiceException(e);
		}
		//验证删除的结果
		if(rows==0)
			throw new ServiceException("记录已经删除不存在");
		return rows;
	}

}
