package com.bestpay.tools.test.dubbo.factory;

import com.bestpay.tools.test.dubbo.App;
import com.bestpay.tools.test.dubbo.listener.TestPlanListener;
import com.bestpay.tools.test.dubbo.model.TestTask;
import com.bestpay.tools.test.dubbo.reader.BasePlanReader;
import com.bestpay.tools.test.dubbo.util.GsonReader;
import com.bestpay.tools.test.dubbo.writer.ExcelPlanWriter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jiaju on 2015/8/8.
 */
@Slf4j
@ContextConfiguration(locations={"/spring/spring-context.xml"})
@Listeners(TestPlanListener.class)
public class TestTaskFactory extends AbstractTestNGSpringContextTests {
    @Getter
    @Setter
    static Object result;

    @Getter
    @Setter
    static String flag = "";

    List<String> ls = new LinkedList<String>();

    @Value("#{app.planPath}")
    String planPath ;

    @DataProvider(name = "TestTask")
    public Object[][] createTestTask() {
        List<TestTask> testTaskList = BasePlanReader.getTestTaskList();
        log.info("任务列表:{}", testTaskList);

        Object[][] result = new Object[testTaskList.size()][1];
        for (int i = 0; i < testTaskList.size(); i++) {
            result[i][0] = testTaskList.get(i);
        }

        return result;
    }

    @Test(dataProvider = "TestTask")
    public void iDubboTest(TestTask task) throws Exception {
        assert (task != null);
        log.info("任务:{}", task);

//        Object result;

        flag = "";

        if(task.getClazz()==""){
            log.info("服务类名称书写有误，请检查！");
        }

        Class clazz = Class.forName(task.getClazz());

        Object service = App.getContext().getBean(task.getService(), clazz);

        Class[] argsClass = new Class[task.getParamList().size()];
        Object[] args = new Object[task.getParamList().size()];

        //读取所有参数
        for (int j = 0; j < task.getParamList().size(); j++) {
            if (""==task.getParamList().get(j).getType()){
                log.info("服务参数类型书写有误，请检查！");
            }

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

    @AfterMethod
    public void writerResult(){
        log.info("添加测试结果到列表 : {}", ls.toString());

        if("NoN_".equals(flag) && !"NoN_".equals(result)){
            result = flag + (null != result ? result.toString() : "");
        }
        ls.add(null != result ? result.toString() : "");
    }

    @AfterClass
    public void writerExcel() throws IOException {
        ExcelPlanWriter writer = new ExcelPlanWriter();
        writer.writeResult(planPath,ls);
    }
}
