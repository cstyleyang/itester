package com.bestpay.tools.test.dubbo.reader;

import com.bestpay.tools.test.dubbo.model.TestTask;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by Jiaju on 2015/8/1.
 */
public abstract class BasePlanReader {

    @Getter
    @Setter
    static protected List<TestTask> testTaskList;

    public abstract boolean read(String plan, ClassPathXmlApplicationContext context);
}
