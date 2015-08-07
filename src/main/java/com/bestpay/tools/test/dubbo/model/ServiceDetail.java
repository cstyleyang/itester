package com.bestpay.tools.test.dubbo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by Jiaju on 2015/8/3.
 */

@ToString
@Getter
@Setter
public class ServiceDetail {

    //方法的类名
    String clazz;

    //参数的类名
    List<String>  paramClazzList;
}
