package com.nivelle.spring.springcore.aop;

import org.springframework.stereotype.Service;

/**
 * 使用AOP的方法
 *
 * @author nivelle
 * @date 2020/02/01
 */

@Service
public class MyService {


    @AopAnnotation
    public String writeLog() {
        System.out.println("被代理的方法！！！");
        return "被代理的方法返回值";
    }
}
