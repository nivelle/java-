package com.nivelle.core.javacore.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 方法句柄使用demo
 *
 * @author nivelle
 * @date 2020/03/28
 */
public class MethodHandlesMock {

    /**
     * 方法句柄
     *
     * @throws Throwable
     */
    public void invokeExact() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(String.class, int.class, int.class);
        //调用String类的 substring方法，方法参数类型：String,int,int
        MethodHandle mh = lookup.findVirtual(String.class, "substring", type);
        String str = (String) mh.invokeExact("Hello World", 1, 3);
        System.out.println(str);
    }

    /**
     * 允许使用JAVA字节码中的类型描述字符串来创建MethodType
     */
    public void generateMethodTypesFromDescriptor() {
        ClassLoader cl = this.getClass().getClassLoader();
        String descriptor = "(Ljava/lang/String;)Ljava/lang/String;";
        MethodType mt1 = MethodType.fromMethodDescriptorString(descriptor, cl);
        System.out.println(mt1);
    }

    /**
     * 参数绑定
     *
     * @throws Throwable
     */
    public void multipleBindTo() throws Throwable {

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(String.class, "indexOf", MethodType.methodType(int.class, String.class, int.class));
        mh = mh.bindTo("Hello").bindTo("l");
        System.out.println(mh.invoke(2));
    }

    public void asVarargsCollector() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(MethodHandlesMock.class, "normalMethod", MethodType.methodType(void.class, String.class, int.class, int[].class));
        /**
         * asVarargsCollector方法,把原始方法句柄对应的方法类型的最后一个数组类型的参数转换成对应类型的可变长度参数
         **/
        mh = mh.asVarargsCollector(int[].class);
        mh.invoke(this, "Hello", 2, 3, 4, 5);
        System.out.println(mh.type());
    }

    public void normalMethod(String arg1, int arg2, int[] arg3) {
    }


    public void toBeSpreaded(String arg1, int arg2, int arg3, int arg4) {
    }

    public void asSpreader() throws Throwable {

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(MethodHandlesMock.class, "toBeSpreaded", MethodType.methodType(void.class, String.class, int.class, int.class, int.class));
        mh = mh.asSpreader(int[].class, 3);
        mh.invoke(this, "Hello", new int[]{3, 4, 5});
        System.out.println(mh.type());
    }


    public void varargsMethod(String arg1, int... args) {
    }

    public void asFixedArity() throws Throwable {

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(MethodHandlesMock.class, "varargsMethod", MethodType.methodType(void.class, String.class, int[].class));
        mh = mh.asFixedArity();
        mh.invoke(this, "Hello", new int[]{2, 4});
        System.out.println(mh.type());
    }

    public static void main(String[] args) throws Throwable {
        /**
         * 方法句柄
         */
        MethodHandlesMock invokeExactDemo = new MethodHandlesMock();
        invokeExactDemo.invokeExact();
        invokeExactDemo.multipleBindTo();
        invokeExactDemo.generateMethodTypesFromDescriptor();
        invokeExactDemo.asVarargsCollector();
        invokeExactDemo.asFixedArity();
        invokeExactDemo.asSpreader();

    }


}