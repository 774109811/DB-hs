package com.db.sys.dao;

import com.db.sys.entity.SysConfigs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigDao {
    int getRowCount(String name);

    List<SysConfigs> findPageObjects(@Param("name")String name,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize);

    SysConfigs findObjectById(Integer id);
}
