package com.heavytiger.automail.service;

import com.heavytiger.automail.mapper.ChpMapper;
import com.heavytiger.automail.pojo.Chp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author heavytiger
 * @version 1.0
 * @description TODO
 * @date 2022/3/1 15:15
 */
@Service
public class ChpService {

    @Autowired
    private ChpMapper chpMapper;

    public String getSentence(){
        Chp chp = chpMapper.queryChpNoUsed();
        chpMapper.updateChpUsed(chp.getId());
        if(chp.getSentence() == null) {
            // 说明没有获取成功，重试
            return getSentence();
        }
        return chp.getSentence();
    }
}
