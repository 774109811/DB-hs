package com.db.sys.controller;

import com.db.common.vo.JsonResult;
import com.db.common.vo.PageObject;
import com.db.common.vo.SysUserDeptResult;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;
import com.db.sys.service.realm.ShiroUserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
@RequestMapping("/user/")
@Controller
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private ShiroUserRealm shiroUserRealm;

	@RequestMapping("doUserListUI")
	public String doUserListUI() {

		return "sys/user_list";
	}

	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(
			String username,Integer pageCurrent){
		PageObject<SysUserDeptResult> pageObject=
				sysUserService.findPageObjects(username,
						pageCurrent);
		return new JsonResult(pageObject);
	}
	@RequestMapping("doUserEditUI")
	public String doUserEditUI(){
		return "sys/user_edit";
	}
	//修改禁用和启用的方法 
	@RequestMapping("doValidById")
	@ResponseBody
	public JsonResult doValidById(
			Integer id,
			Integer valid){
		sysUserService.validById(
				id,
				valid);//"admin"用户将来是登陆用户
		return new JsonResult("update ok");
	}


	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(
			SysUser entity,
			Integer[] roleIds){
		sysUserService.saveObject(entity,roleIds);
		return new JsonResult("save ok");
	}


	@RequestMapping("doFindObjectById")
	@ResponseBody
	public JsonResult doFindObjectById(
			Integer id){
		Map<String,Object> map=
				sysUserService.findObjectById(id);
		System.out.println(map);
		return new JsonResult(map);
	}

	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(
			SysUser entity,Integer[] roleIds){
		sysUserService.updateObject(entity,
				roleIds);
		return new JsonResult("update ok");
	}


	//查询有没有同名的和相同手机号和邮箱
	@RequestMapping("dofindObjectByUser")
	@ResponseBody
	public JsonResult findObjectByUser(String colName, String colValue) {
		int row = sysUserService.findObjectByUser(colName, colValue);
		System.out.println(colName);
		System.out.println(colValue);
		return new JsonResult("ok"+row);
	}


	//在SysUserController添加登录方法
	//接收用户名及密码参数，并对其进行有效验证
	//执行登录认证

	//统计数量
	private AtomicInteger counter=new AtomicInteger(0);//默认从零开始
	//private int count;


	@RequestMapping("doLogin")
	@ResponseBody
	public JsonResult doLogin(String username ,String password ) {

		//1.获取Subject对象
		Subject subject=SecurityUtils.getSubject();
		//2.通过Subject提交用户信息,交给shiro框架进行认证操作
		//2.1对用户进行封装
		UsernamePasswordToken token=
				new UsernamePasswordToken(
						username,//身份信息
						password);//凭证信息
		//2.2对用户信息进行身份认证
		subject.login(token);//提交认证管理器

		//统计数量 
		/*   synchronized (token) {//排他锁
				   count++;//本身是不安全的
				   System.out.println("在线人数"+count);
			    }*/
		int count = counter.incrementAndGet();//先添加后获取
		System.out.println("在线人数"+count);



		//分析:
		//1)token会传给shiro的SecurityManager
		//2)SecurityManager将token传递给认证管理器
		//3)认证管理器会将token传递给realm
		return new JsonResult("login ok");

	}

	@RequestMapping("doLogout")
	public String doLogout(){
		System.out.println("=doLogout=");

		//统计数量  
		int count = counter.decrementAndGet();
		System.out.println("还有"+count+"人在线");



		shiroUserRealm.logout();
		return "redirect:../doLoginUI.do";
	}


}
