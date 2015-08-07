package com.bestpay.tools.test.dubbo.reader;

import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.util.FileHandler;
import com.bestpay.tools.test.dubbo.util.GsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/1.
 */
@Component
public class JsonPlanReader extends BasePlanReader{

    @Autowired
    FileHandler fileHandler;

    public boolean read(String xmlPlanName, ClassPathXmlApplicationContext context){
        String planJson;
        planJson = fileHandler.readFileToString(xmlPlanName);

        if(null == planJson)
            return false;

        List<Object> resultList =  GsonReader.readGsonToArray(planJson, TestTask.class);

        if(null == resultList || 0 == resultList.size() )
            return false;

        testTaskList = new ArrayList<TestTask>();

        //遍历链表对象进行类型转换
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            testTaskList.add((TestTask) it.next());
        }

        return true;
    }

}
