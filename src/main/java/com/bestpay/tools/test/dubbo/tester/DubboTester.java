package com.bestpay.tools.test.dubbo.tester;

import com.bestpay.tools.test.dubbo.caches.Cache;
import com.bestpay.tools.test.dubbo.factory.TestTaskFactory;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.reader.BasePlanReader;
import com.bestpay.tools.test.dubbo.reporter.Reporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.testng.TestNG;

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

        TestNG testng = new TestNG();
        testng.setVerbose(2);
        testng.setTestClasses(new Class[]{TestTaskFactory.class});
        testng.run();

    }
}

