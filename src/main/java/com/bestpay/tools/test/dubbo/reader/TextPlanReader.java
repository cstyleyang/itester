package com.bestpay.tools.test.dubbo.reader;

import com.bestpay.tools.test.dubbo.caches.Cache;
import com.bestpay.tools.test.dubbo.model.ServiceDetail;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.model.TypeValue;
import com.bestpay.tools.test.dubbo.util.FileHandler;
import com.bestpay.tools.test.dubbo.util.TextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/3.
 */
@Component
public class TextPlanReader extends  BasePlanReader{
    @Autowired
    FileHandler fileHandler;

    @Autowired
    TextHandler textHandler;

    @Autowired
    Cache cache;

    public boolean read(String planPath, ClassPathXmlApplicationContext context){
        List<String> planJextList;
        planJextList = fileHandler.readLines(planPath);

        if(null == planJextList || 0 == planJextList.size())
            return false;

        Iterator<String> textIt = planJextList.iterator();
        testTaskList = new ArrayList<TestTask>();
        List<TypeValue> paramList = new ArrayList<TypeValue>();
        TestTask testTask = null;

        while(textIt.hasNext())
        {
            String text = textIt.next();
            if(text.contains("接口")){
                if(null != testTask){
                    //设定接口类型
                    ServiceDetail serviceDetail = cache.getServiceCache().get(
                            testTask.getService()  + "." + testTask.getMethod());
                    testTask.setClazz(serviceDetail.getClazz());

                    //TODO 设定参数类型 可以优化
                    for(int i = 0; i < paramList.size(); i++){
                        paramList.get(i).setType(serviceDetail.getParamClazzList().get(i));
                    }

                    testTask.setParamList(paramList);
                    paramList = new ArrayList<TypeValue>();
                    testTaskList.add(testTask);
                }
                testTask = new TestTask();
                testTask.setService(textHandler.getTextContent(text, "="));

            }else if(text.contains("方法")){
                testTask.setMethod(textHandler.getTextContent(text, "="));
            }else if(text.contains("参数")){
                TypeValue typeValue = new TypeValue();
                typeValue.setValue(textHandler.getTextContent(text, "="));
                paramList.add(typeValue);
            }

            //处理结束符
            if(!textIt.hasNext()){
                //设定接口类型
                ServiceDetail serviceDetail = cache.getServiceCache().get(
                        testTask.getService()  + "." + testTask.getMethod());
                testTask.setClazz(serviceDetail.getClazz());
                //设定参数类型
                for(int i = 0; i < paramList.size(); i++){
                    paramList.get(i).setType(serviceDetail.getParamClazzList().get(i));
                }

                testTask.setParamList(paramList);
                testTaskList.add(testTask);
            }
        }

        return true;
    }

}
