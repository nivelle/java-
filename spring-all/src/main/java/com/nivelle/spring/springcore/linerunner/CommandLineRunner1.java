package com.nivelle.spring.springcore.linerunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * If you need to run some specific code once the SpringApplication has started,
 * you can implement the ApplicationRunner or CommandLineRunner interfaces.
 * Both interfaces work in the same way and offer a single run method,
 * which is called just before SpringApplication.run() completes.
 */
@Component
@Order(1)
public class CommandLineRunner1 implements CommandLineRunner {
    /**
     * 容器启动完成，SpringApplication.run()执行之前
     * @param args
     */
    @Override
    public void run(String... args) {
        System.out.println("CommandLineRunner 初始化资源 1");
    }
}