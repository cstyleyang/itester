package com.bestpay.tools.test.dubbo.caches;

import com.bestpay.tools.test.dubbo.model.ServiceDetail;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Jiaju on 2015/8/3.
 */
@Component
@Slf4j
public class Cache {
    @Setter
    @Getter
    private Map<String, ServiceDetail> serviceCache = Maps.newHashMap();

    public void init(ClassPathXmlApplicationContext context)
    {
        serviceCache = context.getBeansOfType(ServiceDetail.class);
    }
}
