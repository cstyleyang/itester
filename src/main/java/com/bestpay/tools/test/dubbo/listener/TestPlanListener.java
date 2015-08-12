package com.bestpay.tools.test.dubbo.listener;

import com.bestpay.tools.test.dubbo.factory.TestTaskFactory;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Created by zhanglingsi on 15/8/12.
 */
@Slf4j
public class TestPlanListener extends TestListenerAdapter {


    @Override
    public void onTestFailure(ITestResult tr) {
//        this.m_allTestMethods.add(tr.getMethod());
//        this.m_failedTests.add(tr);
        TestTaskFactory.setFlag("NoN_");
        TestTaskFactory.setResult("");
        log.info("====================" + tr.getThrowable()+"====================");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        log.info("测试成功！！！！");
    }
}
