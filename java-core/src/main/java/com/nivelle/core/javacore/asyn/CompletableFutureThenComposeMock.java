package com.nivelle.core.javacore.asyn;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 基于 thenCompose 实现一个 CompletableFuture 执行完成之后执行另外一个 CompletableFuture
 *
 * @author fuxinzhong
 * @date 2021/04/03
 */
public class CompletableFutureThenComposeMock {

    public static void main(String[] args) throws Exception {
        CompletableFuture<String> result = doSomeThingOne("fuck jessy").thenCompose(x -> doSomeThingTwo(x));
        System.out.println(result.get());
    }

    public static CompletableFuture<String> doSomeThingOne(String params) {
        return CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
                System.out.println(Thread.currentThread().getName() + "doSomeThingOne is running");
                return params + ":1";
            }
        });
    }

    public static CompletableFuture<String> doSomeThingTwo(String x) {
        return CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                System.out.println(Thread.currentThread().getName() + "doSomeThingTwo is running");
                return x + ":2";
            }
        });
    }

}
