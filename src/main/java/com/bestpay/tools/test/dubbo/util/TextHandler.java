package com.bestpay.tools.test.dubbo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by Jiaju on 2015/8/3.
 */
@Slf4j
@Component
public class TextHandler {
    public String getTextContent(String text, String splitFlag)
    {
        String[] list = text.split(splitFlag);
        try{
            return list[1];
        }
        catch (Exception e){
            log.error("Txt获取失败", e);
            return null;
        }
    }
}
