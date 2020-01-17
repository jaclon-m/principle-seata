package com.jaclon.learning.server1;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DemoDao {

    @Insert("insert into p_seata(name) values(#{name})")
    public void insert(@Param("name") String name);
}
