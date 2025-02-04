### JVM基础类型存储

#### boolean类型

- 对于 Java 虚拟机来说，它看到的 boolean 类型，早已被映射为整数类型。因此，将原本声明为 boolean 类型的局部变量，赋值为除了 0、1 之外的整数值，在 Java 虚拟机看来是“合法”的。

- 在 HotSpot 中，boolean 字段占用一字节，而 boolean 数组则直接用 byte 数组来实现。__为了保证堆中的 boolean 值是合法的，HotSpot 在存储时显式地进行掩码操作，也就是说，只取最后一位的值存入boolean 字段或数组中。__

#### 浮点数

- 以 float 为例，浮点类型通常有两个 0，+0.0F 以及 -0.0F。Java 里都是 0， 后者是符号位为 1 其他位均为 0 的浮点数，在内存中等同于十六进制整数 0x8000000（即 -0.0F 可通过
  Float.intBitsToFloat(0x8000000) 求得）。 尽管它们的内存数值不同，但是在 Java 中 `+0.0F == -0.0F `会返回真

- 正无穷就是任意正浮点数（不包括 +0.0F）除以 +0.0F 得到的值，而负无穷是任意正浮点数除以 -0.0F 得到的值。

  在 Java 中，**正无穷和负无穷是有确切的值，在内存中分别等同于十六进制整数 0x7F800000 和 0xFF800000**; 超出这个范围的是 NaN(Not-a-Number)

**NaN 有一个有趣的特性：除了“!=”始终返回 true 之外，所有其他比较结果都会返回 false。**

---

## java基本类型的大小(在局部变量表和操作数栈中)

1. 在 Java 虚拟机规范中,**局部变量区等价于一个数组,并且可以用正整数来索引**。除了 long、double 值需要用两个数组单元来存储之外,其他基本类型以及引用类型的值均占用一个数组单元。

2. __boolean、byte、char、short__ 这四种类型，在栈上占用的空间和 int、引用类型也是一样的。

3. long，double，float三种类型存入操作数栈,当成各自类型计算，与其值域吻合

4. 作为局部变量,在 32 位的 HotSpot 中,基本类型在栈上将占用 4 个字节;而在 64 位的 HotSpot 中,他们将占 8 个字节。

## java基本类型的大小(在堆中)

- 对于 byte、char 以及 short 这三种类型的字段或者数组单元，它们在堆上占用的空间分别为 __1字节、2字节、2字节__，也就是说,跟这些类型的值域相吻合

![java基本类型jvm.png](https://i.loli.net/2021/05/16/LU7Nqx4RpHIbK5O.png)

- 从前面的基本类型转换至后面的基本类型，无需强制转换。另外一点值得注意的是，__尽管他们的默认值看起来不一样，但在内存中都是 0。__

- 在这些基本类型中，boolean 和 char 是唯二的无符号类型。在不考虑违反规范的情况下，boolean 类型的取值范围是 0 或者 1。char 类型的取值范围则是[0, 65535]

- **当我们将一个 int 类型的值，存储到这些类型的字段或数组时，相当于做了一次隐式的掩码操作**

`举例来说，当我们把 0xFFFFFFFF（-1）存储到一个声明为 char 类型的字段里时，由于该字段仅占两字节，所以高两位的字节便会被截取掉，最终存入“\uFFFF

-------------

## JVM 基础类型加载

- Java 虚拟机的算数运算几乎全部依赖于操作数栈。也就是说，我们需要将堆中的boolean、byte、char 以及 short 加载到操作数栈上，而后将栈上的值当成 int 类型来运算。

- **对于 boolean、char 这两个无符号类型来说，加载伴随着零扩展。举个例子，char 的大小为两个字节。在加载时 char 的值会被复制到 int 类型的低二字节，而高二字节则会用 0 来填充。**

- 对于 byte、short 这两个类型来说，加载伴随着符号扩展。举个例子，short 的大小为两个字节。在加载时 short 的值同样会被复制到 int 类型的低二字节。如果该 short 值为非负数，即最高位为 0，那么该 int
  类型的值的高二字节会用 0 来填充，否则用 1 来填充。

- boolean的掩码处理是取低位的最后一位:
````
public class Foo {
  static boolean boolValue;
  public static void main(String[] args) {
    boolValue = true; // 将这个true替换为2或者3，再看看打印结果
    if (boolValue) System.out.println("Hello, Java!");
    if (boolValue == true) System.out.println("Hello, JVM!");
  }
}

当替换为2的时候无输出.当替换为3的时候打印HelloJava及HelloJVM
因为将boolean 保存在静态域中,指定了其类型为'Z',当修改为2时取低位最后一位为0,当修改为3时取低位最后一位为1
````

-------------

##JVM 引用类型

- 类、接口、数组类和泛型参数

- 范性参数会在编译过程被擦除；**数组类是由java虚拟机直接生成的**

- 类和接口对应字节流

### jvm java对象

- **对象头**:

  - 标记字段: java虚拟机有关该对象运行数据：**如哈希码、GC信息、锁信息**

  在 64 位的 Java 虚拟机中，对象头的标记字段占 64 位，而类型指针又占了 64 位。以 Integer 类为例，它仅有一个 int 类型的私有字段，占 4 个字节。因此，每一个 Integer 对象的额外内存开销至少是 400% 这也是为什么 Java 要引入基本类型的原因之一。

- **类型指针**：指向该对象的类

  - 压缩指针: 为了减少类型指针的内存占用，将64位指针压缩至32位，进而节约内存。之前64位寻址，寻的是字节。现在32位寻址，寻的是变量。再加上内存对齐(补齐为8的倍数)，可以每次寻变量都以一定的规则寻找，并且一定可以找得到。

#### 内存对齐:

- 默认情况下，Java 虚拟机堆中对象的起始地址需要对齐至 8 的倍数。如果一个对象用不到 8N 个字节，那么空白的那部分空间就浪费掉了。这些浪费掉的空间我们称之为对象间的填充（padding）

- 内存对齐的另一个好处是,使得CPU缓存行可以更好的实施。保证每个变量都只出现在一条缓存行中，不会出现跨行缓存。提高程序的执行效率。

- 字段重排序: 其实就是更好的执行内存对齐标准,会调整字段在内存中的分布,达到方便寻址和节省空间的目的。

#### 虚共享:

当两个线程分别访问一个对象中的不同volatile字段，理论上是不涉及变量共享和同步要求的。
但是如果两个volatile字段处于同一个CPU缓存行中，对其中一个volatile字段的写操作，会导致整个缓存行的写回和读取操作，进而影响到了另一个volatile变量，也就是实际上的共享问题

**@Contented注解** :该注解就是用来解决虚共享问题的，被该注解标识的变量，会独占一个CPU缓存行。但也因此浪费了大量的内存空间。
