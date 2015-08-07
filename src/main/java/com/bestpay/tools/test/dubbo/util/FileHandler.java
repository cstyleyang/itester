package com.bestpay.tools.test.dubbo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/1.
 */
@Slf4j
@Component
public class FileHandler {
    @Value("#{app.retFlag}")
    String retFlag;

    @Value("#{app.charset}")
    String charset;

    public String readFileToString(String filePath)
    {
        try {
            File file = new File(filePath);
            return  FileUtils.readFileToString(file);
        } catch (IOException e) {
            log.error("读取文件{}报错:{}", filePath, e);
            return null;
        }
    }

    public List<String> readLines(String filePath)
    {
        try {
            File file = new File(filePath);
            return  FileUtils.readLines(file, charset);
        } catch (IOException e) {
            log.error("读取文件{}报错:{}", filePath, e);
            return null;
        }
    }

    public void  writeStringToFile(String filePath, String str)
    {
        try {
            File file = new File(filePath);
            FileUtils.writeStringToFile(file, str, charset);
        } catch (IOException e) {
            log.error("写入文件{}报错:{}", filePath, e);
        }
    }

    public void clean(String filePath)
    {
        try {
            File file = new File(filePath);
             FileUtils.forceDelete(file);
        } catch (IOException e) {
            log.error("清除文件{}报错:{}", filePath, e);
        }
    }
}
