package com.db.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.Node;
import com.db.sys.entity.SysMenu;

public interface SysMenuDao {
	//通过此方法查询所有菜单以及上级菜单
	//一个map对象用于封装表中的一行记录(key为字段名,value为值)
	//多条菜单记录会封装为多个map,多个map再存储到List集合中
	
	List<Map<String,Object>> findObjects();
	//接口中定义基于id查询是否有子菜单的方法
	 /**
	  * 根据菜单id统计子菜单的个数
	  * @param id
	  * @return
	  */
	int getChildCount(Integer id);
	 /**
	  * 根据id 删除菜单
	  * @param id
	  * @return
	  */
	int deleteObject(Integer id);
	
	List<Node> findZtreeMenuNodes();
	
	
	int insertObject(SysMenu entity);
	int updateObject(SysMenu entity);
	
	List<String> findPermissions(
            @Param("menuIds")
                    Integer... menuIds);
	
}
