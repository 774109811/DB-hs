package com.db.sys.controller;

import com.db.common.vo.JsonResult;
import com.db.sys.entity.SysMenu;
import com.db.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@RequestMapping("/menu")
@Controller
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;

	@RequestMapping("/doMenuListUI")
	public String doMenuListUI() {
		return "sys/menu_list";
	}
	@RequestMapping("/doFindObjects")
	@ResponseBody
	public JsonResult dofindObjects() {
		List<Map<String, Object>> list = sysMenuService.findObjects();
		return new JsonResult(list);
	}

	@RequestMapping("doDeleteObject")
	@ResponseBody
	public JsonResult doDeleteObject(Integer id) {
		System.out.println(id);
		int rows = sysMenuService.deleteObject(id);
		return new JsonResult("delete"+rows);

	}

	@RequestMapping("doMenuEditUI")
	public String doMenuEditUI(){
		return "sys/menu_edit";
	}
	@RequestMapping("doFindZtreeMenuNodes")
	@ResponseBody
	public JsonResult doFindZtreeMenuNodes() {
		return new
				JsonResult(sysMenuService.findZtreeMenuNodes());
	}
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysMenu entity){
		sysMenuService.saveObject(entity);
		return new JsonResult("save ok");
	}
	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(SysMenu entity){
		sysMenuService.updateObject(entity);
		return new JsonResult("update ok");
	}

}
