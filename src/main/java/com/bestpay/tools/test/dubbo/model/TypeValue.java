package com.bestpay.tools.test.dubbo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Jiaju on 2015/8/1.
 */
@ToString
@Getter
@Setter
public class TypeValue {
    public TypeValue(){
    }

    public TypeValue(String type, Object value){
        this.type = type;
        this.value = value;
    }
    String type;

    Object value;
}
