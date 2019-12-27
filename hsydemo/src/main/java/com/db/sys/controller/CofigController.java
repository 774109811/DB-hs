package com.db.sys.controller;

import com.db.common.vo.JsonResult;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysConfigs;
import com.db.sys.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @ClassName CofigController
 * @Description ToDo
 * @Author housaiying
 * @Date 2019/12/23 15:19
 **/
@Controller
@RequestMapping("/config/")
public class CofigController {
    @Autowired
    private ConfigService configService;

    @RequestMapping("doConfigListUI")
    public String doUserListUI() {
        return "sys/config_list";
    }

    @RequestMapping("doFindPageObjects")
    @ResponseBody
    public JsonResult doFindPageObjects(
            String name,Integer pageCurrent){
        PageObject<SysConfigs> pageObject=
                configService.findPageObjects(name,
                        pageCurrent);
        return new JsonResult(pageObject);
    }

    @RequestMapping("doFindObjectById")
    @ResponseBody
    public JsonResult doFindObjectById(
            Integer id){
        Map<String,Object> map=
                configService.findObjectById(id);
        System.out.println(map);
        return new JsonResult(map);
    }

    //配置的修改
    @RequestMapping("doConfigEditUI")
    public String doRoleEditUI(){
        return "sys/config_edit";
    }
}
