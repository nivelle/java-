package com.nivelle.core.javacore.lang;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 字节 位
 *
 * @author nivelle
 * @date 2019/10/31
 */
public class ByteMock {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    // Packing and unpacking ctl
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    /**
     * 1. &表示按位与,只有两个位同时为1,才能得到1,
     * <p>
     * 0x代表16进制数,0xff 表示的数二进制 1111 1111 占一个字节.和其进行&操作的数,最低8位,不会发生变化.
     * <p>
     * 2. Java中使用补码来表示负数,具体就是除符号位之外,剩余位取反加1,符号位不变还是1（符号位0:正数，1:负数）
     */
    public static void main(String[] args) {

        Integer intToByte = 127;
        System.out.println("2的7次方减1:" + Integer.toBinaryString(intToByte));

        System.out.println("2的7次方:" + Integer.toBinaryString(128));

        Integer negativeIntToByte = -128;
        System.out.println("int的二进制长度:" + "11111111111111111111111110000000".length());
        System.out.println("二进制负数表示,128二进制取反数加1:" + Integer.toBinaryString(negativeIntToByte));


        String byteTests = "你";
        //1个字节=8位
        byte[] bytesArray = byteTests.getBytes();
        //byte类型的数据最高位是符号位,通过和0xff进行与操作,转换为int类型的正整数
        for (int i = 0; i < bytesArray.length; i++) {
            System.out.println("当前字节:" + bytesArray[i]);
            String binaryString = StringUtils.leftPad(Integer.toBinaryString(bytesArray[i] & 0xff), 8, '0');
            System.out.println("二进制展示:" + binaryString);
            System.out.println("当前字节与0xff与" + (bytesArray[i] & 0xff));
        }

        /**
         *  最大最小值
         */
        System.out.println("二进制最大值 max value:" + Byte.MAX_VALUE);
        System.out.println("二进制最小值 min value:" + Byte.MIN_VALUE);

        byte byteValue = 11;
        Byte byteToString = new Byte(byteValue);
        System.out.println(byteToString.toString());
        String stringValue = "11";
        System.out.println(Byte.valueOf(stringValue));

        System.out.println();

        /**
         * 0:就是false,1:就是true
         *
         * 与运算,两位全为1才是1
         *
         * 特殊用法:
         *
         * 1.清零:如果想要将一个单元清零,即使全部二进制为0,只要与一个各位都为零的数值相与,结果为零
         *
         * 2.读取一个数中指定位:与1进行与运算
         */

        System.out.println("0&0 is: " + (0 & 0));
        System.out.println("0&1 is: " + (0 & 1));
        System.out.println("1&0 is: " + (1 & 0));
        System.out.println("1&1 is: " + (1 & 1));

        /**
         * 二进制的表示: 0b开头+(0或1表示的二进制)
         */
        int getLow4Byte = (0b10101110 & 0b00001111);
        System.out.println("低四位整数:" + Integer.toBinaryString(getLow4Byte));
        System.out.println("取低四位二进制表示:" + Integer.toBinaryString(getLow4Byte));
        int clearByte = (0b10101110 & 0b00000000);
        System.out.println("清0所有位:" + StringUtils.leftPad(Integer.toBinaryString(clearByte), 8, '0'));
        System.out.println();

        /**
         * 或运算,只要有一个1结果就是1(1代表true)
         *
         */
        System.out.println("0｜0 is: " + (0 | 0));
        System.out.println("0｜1 is: " + (0 | 1));
        System.out.println("1｜0 is: " + (1 | 0));
        System.out.println("1｜1 is: " + (1 | 1));

        /**
         * 1.对一个数据的某位置设置为1
         */
        int set4LowByte = 0b10101110;
        int show4LowByte = (set4LowByte | 0b00001111);
        System.out.println("置低四位全为1:" + Integer.toBinaryString(show4LowByte));

        /**
         * 异或运算（两个相应位为"异"则该位结果为1,否则为0）
         *
         * 特殊用法:
         *
         * 1. 使特定位翻转: 找一个数, 对应原来要翻转的各位,该数对应的位为1,其余各位为0,此数与原来要数异或即可。
         *
         * 2. 保留原值:0与原值进行异或,保留原值
         *
         */
        System.out.println("0^0 is: " + (0 ^ 0));
        System.out.println("0^1 is: " + (0 ^ 1));
        System.out.println("1^0 is: " + (1 ^ 0));
        System.out.println("1^1 is: " + (1 ^ 1));

        /**
         * 使特定位翻转: 找一个数,对应原来要翻转的各位,该数对应的位为1,其余各位为0,此数与原来要数异或即可。
         */
        int flipByte = 0b10101110;
        int showFlipByte = (flipByte ^ 0b00001111);
        System.out.println("指定位翻转:" + Integer.toBinaryString(showFlipByte));

        int holdByte = 0b10101110;
        int showHoldByte = (holdByte ^ 0b00000000);
        System.out.println("指定位保留原值:" + Integer.toBinaryString(showHoldByte));

        /**
         *  左移 <<
         *
         *  将一个运算对象的各二进制位全部左移若干位(左边的二进制位丢弃,右边补0),
         *  若左移舍弃的高位不包含1,则左移一位相当于乘以2
         */
        System.out.println("左移1位:" + (1 << 1));


        int negativeNum = -11;
        System.out.println("-11的二进制位:" + Integer.toBinaryString(negativeNum));

        int leftNegativeNum = -11 << 1;
        System.out.println("-11的二进制位左移一位:" + Integer.toBinaryString(leftNegativeNum));

        System.out.println(leftNegativeNum / negativeIntToByte);


        /**
         * 右移 >>
         *
         * 将一个数的各二进制位全部右移若干位，正数左补0,负数左补1 然后右边丢弃.操作数每向右边移动一位,相当于该数除以2
         */

        int negativeNumRight = 0b00000100;
        System.out.println(negativeNumRight);
        System.out.println(negativeNumRight >> 1);
        System.out.println(Integer.toBinaryString(negativeNumRight >> 3));


        /**
         * 无符号右移 >>>
         *
         * 各个位向右移指定的位数。右移后左边空出的位用零来填充，移出右边的位被丢弃
         */

        int unSignRightMove = 0b00000100;

        int showUnSignRightMove = unSignRightMove >>> 2;
        System.out.println(showUnSignRightMove);


        /***********************************************************/

        /**
         * 1. 原码: 一个整数按照绝对值大小转换成的二进制数称为原码
         *
         * 2. 反码: 将二进制按位取反，所得的新二进制数称位原二进制数的反码
         *
         * 3. 补码: 反码加1称为补码
         *
         * */

        System.out.println("转换为二进制:" + Integer.toBinaryString(8));
        System.out.println("转换为八进制:" + Integer.toOctalString(8));
        System.out.println("转换为十六进制:" + Integer.toHexString(8));


        System.out.println("二进制转换为十进制:" + Integer.valueOf("1000", 2).toString());
        System.out.println("八进制转换为十进制:" + Integer.valueOf("10", 8).toString());
        System.out.println("十六进制转换为十进制:" + Integer.valueOf("8", 16).toString());


        /***********************************************************/

        /**
         * 奇偶判断,1&1才是1,末位是1，则1^0 = 1
         */
        System.out.println("奇数:" + ((3 & 1) == 1));
        System.out.println("偶数:" + ((4 & 1) == 0));

        /**
         * x的反数
         */
        System.out.println("1的反数:" + ((~1)));
        System.out.println("-2的反数:" + ((~-2)));

        System.out.println("2的反数:" + ((~2)));
        System.out.println("-3的反数:" + ((~-3)));


        /**
         * 输入2的n次方
         */
        System.out.println("1乘以2的n次方:" + (1 << 2));

        System.out.println(1 << 16);


        System.out.println("RUNNING:" + RUNNING);
        System.out.println("SHUTDOWN:" + SHUTDOWN);
        System.out.println("STOP:" + STOP);
        System.out.println("TIDYING:" + TIDYING);
        System.out.println("TERMINATED:" + TERMINATED);
        System.out.println("runStateOf:" + runStateOf(1));
        System.out.println("workerCountOf:" + workerCountOf(1));
        System.out.println("ctlOf:" + ctlOf(2, 4));
        System.out.println("COUNT_BITS:" + COUNT_BITS);
        System.out.println("CAPACITY:" + CAPACITY);

        AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        System.out.println(ctl);
        System.out.println(0 >> 2);

        Byte byte1 = new Byte("1");
        System.out.println(byte1);
        System.out.println();

    }

}
