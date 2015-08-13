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
        TestTaskFactory.setFlag("NoN_");
        TestTaskFactory.setResult("");
        log.info("本次测试执行发生异常：{}" , tr.getThrowable());
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        log.info("测试成功！！！！");
    }
}
