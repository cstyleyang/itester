package com.bestpay.tools.test.dubbo.reporter;

import com.bestpay.tools.test.dubbo.model.TypeValue;
import com.bestpay.tools.test.dubbo.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/2.
 */
@Component
public class Reporter {
    @Autowired
    FileHandler fileHandler;

    @Value("#{app.reportPath}")
    String reportPath;

    @Value("#{app.retFlag}")
    String retFlag;

    StringBuffer reportBuffer;

    public void init(){
        reportBuffer = new StringBuffer();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reportBuffer.append("测试时间: " + df.format(new Date()) + retFlag);
    }

    public void add(int seq, String service, String method, List<TypeValue> paramList, Object result){
        reportBuffer.append("序号：" + seq + retFlag);
        reportBuffer.append("测试服务和方法：" + service + "." + method + retFlag);
        Iterator<TypeValue> paramIt = paramList.iterator();
        while (paramIt.hasNext()) {
            reportBuffer.append("测试参数：" + paramIt.next().getValue() + retFlag);
        }
        reportBuffer.append("测试结果：" + result + retFlag);
        reportBuffer.append(retFlag);
    }

    public void report(){
        fileHandler.writeStringToFile(reportPath, reportBuffer.toString());
    }
}
