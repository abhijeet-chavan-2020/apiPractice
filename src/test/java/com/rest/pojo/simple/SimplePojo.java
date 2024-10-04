package com.rest.pojo.simple;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SimplePojo {

    public SimplePojo(){}
    public SimplePojo(String k1, String k2){
        this.key1= k1;
        this.key2= k2;
    }
    private  String key1;
    private  String key2;
}
