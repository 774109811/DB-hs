package com.db.sys.service;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysConfigs;

import java.util.Map;

public interface ConfigService {
    PageObject<SysConfigs> findPageObjects(String name, Integer pageCurrent);

    Map<String, Object> findObjectById(Integer id);
}
