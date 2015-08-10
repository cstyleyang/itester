package com.bestpay.tools.test.dubbo.tester;

import com.bestpay.tools.test.dubbo.App;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.util.GsonReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Created by Jiaju on 2015/8/8.
 */
@Slf4j
public class IDubboTester {

    TestTask task;

    public IDubboTester(TestTask task) {
        this.task = task;
    }

    @Test
    public void iDubboTest() throws Exception {
        assert (task != null);
        log.info("任务:{}", task);

        Object result;

        Class clazz = Class.forName(task.getClazz());
        Object service = App.getContext().getBean(task.getService(), clazz);

        Class[] argsClass = new Class[task.getParamList().size()];
        Object[] args = new Object[task.getParamList().size()];

        //读取所有参数
        for (int j = 0; j < task.getParamList().size(); j++) {
            argsClass[j] = Class.forName(task.getParamList().get(j).getType());
            args[j] = GsonReader.readGsonToObject(
                    task.getParamList().get(j).getValue().toString(), argsClass[j]);
            log.info("测试参数{}:{}", j + 1, args[j]);
        }
        //获取接口方法
        Reporter.log(task.toString());
        log.info("测试报告写入");
        Method method = clazz.getMethod(task.getMethod(), argsClass);

        //调用接口进行测试
        result = method.invoke(service, args);

        //记录日志
        Reporter.log("测试结果" + result.toString());
        log.info("测试结果{}:", result);
    }
}
