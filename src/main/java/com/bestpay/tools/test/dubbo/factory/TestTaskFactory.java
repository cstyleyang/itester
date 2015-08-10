package com.bestpay.tools.test.dubbo.factory;

import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.tester.IDubboTester;
import com.bestpay.tools.test.dubbo.reader.BasePlanReader;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Factory;

import java.util.List;

/**
 * Created by Jiaju on 2015/8/8.
 */
@Slf4j
public class TestTaskFactory {

    @Factory
    public Object[] createTestTask() {

        List<TestTask> testTaskList = BasePlanReader.getTestTaskList();
        log.info("任务列表:{}", testTaskList);

        Object[] result = new Object[testTaskList.size()];
        for (int i = 0; i < testTaskList.size(); i++) {
            result[i] = new IDubboTester(testTaskList.get(i));
        }

        return result;
    }
}
