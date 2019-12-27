package com.db.sys.controller;

import com.db.common.vo.JsonResult;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysRole;
import com.db.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/role/")
@Controller
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	//角色的列表
	@RequestMapping("doRoleListUI")
	public String doRoleListUI(){
		return "sys/role_list";
	}
	//角色的修改
	@RequestMapping("doRoleEditUI")
	public String doRoleEditUI(){
		return "sys/role_edit";
	}




	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(
			String name,
			Integer pageCurrent){
		PageObject<SysRole> pageObject=
				sysRoleService.findPageObjects(name,
						pageCurrent);
		return new JsonResult(pageObject);
	}
	@RequestMapping("doDeleteObject")
	@ResponseBody
	public JsonResult doDeleteObject(Integer id) {
		sysRoleService.deleteObject(id);
		return new JsonResult("delete OK");
	}

	//添加
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(
			SysRole entity,Integer[] menuIds){
		sysRoleService.insertObject(entity, menuIds);
		return new JsonResult("save ok");
	}

	@RequestMapping("doFindObjectById")
	@ResponseBody
	public JsonResult doFindObjectById(Integer id){
		Map<String,Object> map=
				sysRoleService.findObjectById(id);
		return new JsonResult(map);
	}
	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(SysRole entity,
									 Integer[] menuIds){
		sysRoleService.updateObject(entity,menuIds);
		return new JsonResult("update ok");
	}
	//	 业务描述
//	 1)依据请求获取角色id和角色名
//	 2)封装数据,并返回.
	@RequestMapping("doFindRoles")
	@ResponseBody
	public JsonResult doFindObjects(){
		return new JsonResult(sysRoleService.findObjects());
	}

}
