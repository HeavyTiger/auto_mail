package com.heavytiger.automail.mapper;

import com.heavytiger.automail.pojo.Chp;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author heavytiger
 * @version 1.0
 * @description TODO
 * @date 2022/3/1 15:17
 */
@Mapper
public interface ChpMapper {

    public Chp queryChpNoUsed();

    public Integer updateChpUsed(Integer id);
}
