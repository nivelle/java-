
package com.nivelle.core.javacore.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * HashMap
 *
 * @author nivelle
 * @date 2019/06/16
 */
public class HashMapMock {

    public static void main(String[] args) throws Exception {

        /**
         * 默认无参构造函数,初始化的加载因子:0.75
         */
        HashMap hashMap = new HashMap();
        /**
         * key == null, 则 hash=0
         * tab[i = (n - 1) & hash] 所以index = 0 在数组首个桶位置
         */
        hashMap.put(null, "fuck");
        hashMap.put(null, "dl");


        hashMap.put("1", "nivelle");
        hashMap.put("2", "jessy");
        System.out.println("无参初始化HashMap" + hashMap);

        HashMap hashMapInit = new HashMap(4);
        System.out.println("获取不存在的key对应的值：" + hashMap.get("3"));
        System.out.println(hashMapInit);
        /**
         * 指定 初始化数据,默认加载因子0.75
         *
         * 1.如果目标集合为空,则除以装载因子+1,然后和 threshold比较是否需要桶扩容
         *
         * 2.如果目标集合不为空,则直接判断是否需要扩容
         */
        HashMap hashMap1 = new HashMap(hashMap);
        System.out.println("初始化参数是已经存在的HashMap" + hashMap1);

        HashMap hashMap2 = new HashMap();
        hashMap2.put("3", "xihui");
        hashMap2.put("4", "wangzheng");
        System.out.println("hashMap键值对数量:" + hashMap2.size());
        /**
         * 也就是判断size是否为0
         */
        System.out.println("判断是否为空hashMap:" + hashMap2.isEmpty());

        /**
         * hashMap的hashCode 值：
         *
         * 1. 集合元素的的hashCode之和，这个方法继承之AbstractMap
         *
         * 2. 集合元素的hashCode的具体算法是: k的hashCode异或操作v的hashCode
         */
        int hash = hashMap2.hashCode();
        System.out.println("hashMap的hashCode:" + hash);

        HashMap hashMap3 = new HashMap();
        hashMap3.putIfAbsent("1", "2");
        //hashMap3.putIfAbsent("2", "1");

        int hashMap3Key = "1".hashCode();
        int hashMap3Value = "2".hashCode();
        System.err.println("hashMap3的 元素的hashCode:" + (hashMap3Key ^ hashMap3Value));
        System.err.println("hashMap3的hashCode:" + hashMap3.hashCode());

        /**
         * 判断是否存在某个指定键/值
         */
        // 直接定位速度快
        System.out.println("是否包含指定键:" + hashMap2.containsKey("3"));
        // 挨个桶来遍历
        System.out.println("是否包含指定值:" + hashMap2.containsValue("xihui"));

        /**
         * 移除某个元素
         */
        System.out.println("移除前数据hashMap2:" + hashMap2);
        System.out.println("移除指定元素,返回关联的值1:" + hashMap2.remove("3"));
        System.out.println("移除后数据hashMap2:" + hashMap2);
        //不存在的键
        System.out.println("移除指定元素,返回关联的值2:" + hashMap2.remove("8"));

        hashMap2.put("5", "xubing");
        hashMap2.put("6", "biliang");
        /**
         * 返回value集合
         */
        System.out.println("values Collections 集合:" + hashMap2.values());
        /**
         * key集合
         */
        System.out.println("keys Set 集合:" + hashMap2.keySet());

        /**
         * EntrySet集合
         */
        System.out.println("EntrySet 集合:" + hashMap2.entrySet());

        Set<Map.Entry<String, String>> entries = hashMap2.entrySet();
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            System.out.println("entries 集合内的元素:" + iterator.next());
        }

        System.out.println(hashMap2.getOrDefault("8", "不存在情况下的默认值"));
        System.out.println("不存在情况返回默认值也不会在底层数据上添加上此元素:" + hashMap2.get("8"));
        /**
         * 如果存在则不添加,同时返回旧值。
         */
        System.out.println("不存在则添加,返回旧值:" + hashMap2.putIfAbsent("6", "biliang2"));
        System.out.println("hashMap2:" + hashMap2);
        System.out.println("不存在则添加,返回旧值:" + hashMap2.putIfAbsent("8", "biliang2"));
        System.out.println("hashMap2:" + hashMap2);

        /**
         * 替换值
         */
        System.out.println("替换值:" + hashMap2.replace("8", "biliang2", "biliang3"));
        System.out.println("hashMap2:" + hashMap2);

        /**
         * 对指定的键或者值 执行操纵
         */
        System.out.println("对value值指定执行某个操作:" + hashMap2.compute("8", (k, v) -> v + "++"));
        System.out.println("hashMap2:" + hashMap2);


        /**
         * HashMap hash值的求解：
         *
         *  1. key==null 则hashCode =0 ;
         *
         *  2. key.hashCode 然后32位的hashCode值，通过与h>>>16然后让高位也参与到运算，然后再进行: (h-1)&hashCode 确定index位置
         */
        String key = "8";
        int h;
        int hashCode = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        System.out.println("计算某个key的hash值:" + (key.hashCode()));
        System.out.println("计算某个key的hash值,然后用户后面定位:" + hashCode);


        /**
         * HashMap 中指定hash值在数组中(桶)的位置求解:
         *
         * HashMap 使用的方法很巧妙，它通过 hash & (table.length -1)来得到该对象的保存位置,HashMap 底层数组的长度总是2的n次方，这是HashMap在速度上的优化。
         * 当 length 总是2的n次方时，hash & (length-1)运算等价于对 length 取模，也就是 hash%length，但是&比%具有更高的效率。
         *
         * 重点: 2^n 次方的数组长度-1 保证了length的高位都是0,低位都是1,与操作后得到了hash值低位的值且不越界
         *
         * todo hash&(length-1) = hash%length 前提是length 也就是数组长度是2的n次方
         */
        int hashCodeInt = 16;
        int tableLength = 64;
        int result1 = hashCodeInt & (tableLength - 1);
        int result2 = hashCodeInt % tableLength;
        boolean result3 = result1 == result2 ? true : false;
        System.out.println("位运算优化,前提是n是2的整次方:" + result3);


        /**
         * tableSizeFor(initialCapacity) 扩容门槛:会计算出一个initialCapacity 往上取最近的2的n次方作为threshold的初始值
         *
         * //实现原理: 大于输入参数且最近的2的整数次幂的数,最高位1后面的全部设置成1,然后+1成2的整数次幂
         * static final int tableSizeFor(int cap) {
         *         int n = cap - 1;
         *         n |= n >>> 1;
         *         n |= n >>> 2;
         *         n |= n >>> 4;
         *         n |= n >>> 8;
         *         n |= n >>> 16;
         *         return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
         *     }
         *
         */
        //我们预估HashMap要存储n个元素，它的容量就应该指定为((n/0.75f) + 1),如果这个值小于16，那就直接使用16了
        HashMap hashMap4 = new HashMap(2, 0.5F);
        hashMap4.put(1, 1);
        hashMap4.put(2, 2);
        hashMap4.put(3, 2);
        hashMap4.put(4, 2);
        hashMap4.put(5, 2);

        System.out.println("initialCapacity is 1 and loadFactor is 0.5F:" + hashMap4);
        Field filed = hashMap4.getClass().getDeclaredField("threshold");
        filed.setAccessible(true);
        System.out.println(filed.get(hashMap4));

        /**
         * 默认的遍历是无序的
         */
        HashMap hashMap5 = new HashMap();
        hashMap5.put("name1", "value1");
        hashMap5.put("name2", "value2");
        hashMap5.put("name2", "value2");
        Set<Map.Entry<String, String>> entries1 = hashMap5.entrySet();
        Iterator iterator1 = entries1.iterator();
        while (iterator1.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator1.next();
            System.out.println("key=" + entry.getKey());
        }
    }

}


