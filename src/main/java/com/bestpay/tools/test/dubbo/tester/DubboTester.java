package com.bestpay.tools.test.dubbo.tester;

import com.bestpay.tools.test.dubbo.caches.Cache;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.reader.BasePlanReader;
import com.bestpay.tools.test.dubbo.reporter.Reporter;
import com.bestpay.tools.test.dubbo.util.GsonReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/1.
 */
@Slf4j
@Component
public class DubboTester {

    @Value("#{app.planPath}")
    String planPath;

    @Autowired
    BasePlanReader planReader;

    @Autowired
    Reporter reporter;

    @Autowired
    Cache cache;

    public void Test(ClassPathXmlApplicationContext context) {
        //初始化报告
        reporter.init();
        //读取服务详情
        cache.init(context);

        //读取任务
        planReader.read(planPath, context);
        List<TestTask> testTaskList = planReader.getTestTaskList();
        log.info("任务列表:{}", testTaskList);

        if (testTaskList != null && testTaskList.size() != 0) {
            Iterator<TestTask> taskIt = testTaskList.iterator();
            TestTask task = null;
            Object result = null;
            int i = 1;

            while (taskIt.hasNext())  {
                try {
                    task = taskIt.next();
                    log.info("执行任务{} :{}", i, task);
                    Class clazz = Class.forName(task.getClazz());
                    Object service = context.getBean(task.getService(), clazz);

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
                    Method method = clazz.getMethod(task.getMethod(), argsClass);

                    //调用接口进行测试
                    result = method.invoke(service, args);
                    log.info("测试结果:{}", result);
                } catch (Exception e) {
                    result = null;
                    log.error("测试发生异常:{}", e);
                }finally {
                    //记录测试报告
                    reporter.add(i, task.getService(), task.getMethod(), task.getParamList(), result);
                    i++;
                }
            }
        } else {
            log.error("测试任务读取失败");
        }

        //写入报告文件
        reporter.report();
    }
}

