package com.nivelle.base.jdk.annotion.myannotion;

/**
 * 注解测试
 **/
public class MyAnnotationApplication {

    public static void main(String[] args) {
        UserLogin userLogin = ParseAnnotationContainer.getBean();
        userLogin.loginTest();
    }
}

