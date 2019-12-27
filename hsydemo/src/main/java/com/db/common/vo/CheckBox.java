package com.db.common.vo;

import java.io.Serializable;

//业务描述
//借助此对象封装角色id,角色名称.
//业务实现(定义类并实现序列化接口,添加版本id,提供set/get)

public class CheckBox implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282985653226440750L;
	private Integer id;//角色的id
	private String name;//角色的名字
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CheckBox [id=" + id + ", name=" + name + "]";
	}
	
	
}
