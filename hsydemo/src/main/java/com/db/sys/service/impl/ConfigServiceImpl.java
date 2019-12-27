package com.db.sys.service.impl;

import com.db.common.exception.ServiceException;
import com.db.common.vo.PageObject;
import com.db.sys.dao.ConfigDao;
import com.db.sys.entity.SysConfigs;
import com.db.sys.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ConfigServiceImol
 * @Description ToDo
 * @Author housaiying
 * @Date 2019/12/23 15:29
 **/
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigDao configDao;
    public PageObject<SysConfigs> findPageObjects(String name, Integer pageCurrent) {
        if(pageCurrent==null||pageCurrent<1)
            throw new IllegalArgumentException("页码不正确");//无效参数异常
        //2. 总行数(通过查询获得) 依据条件查询总记录数
        int rowCount = configDao.getRowCount(name);
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
        List<SysConfigs> records = configDao.findPageObjects(name, startIndex, pageSize);
        //5.对查询结果进行封装并返回
        //5.1)构建PageObject对象
        PageObject<SysConfigs> pageObject = new PageObject<SysConfigs>();
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

    public Map<String, Object> findObjectById(Integer id) {
        //1.合法性验证
        if(id==null||id<=0)
            throw new ServiceException(
                    "参数数据不合法,Id="+id);
        //2.业务查询
        SysConfigs configs=
                configDao.findObjectById(id);
        if(configs==null)
            throw new ServiceException("此用户已经不存在");
        //3.数据封装
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("configs", configs);
        return map;
    }
}
