package com.bestpay.tools.test.dubbo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by Jiaju on 2015/8/1.
 */
@ToString
@Getter
@Setter
public class TestTask {
    String service;//测试的接口名称，对应dubbo的id

    String clazz;//类名

    String method;//方法名

    List<TypeValue> paramList;//调用参数名及参数
}
