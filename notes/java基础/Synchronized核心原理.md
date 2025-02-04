## 管程

- Java 采用的是管程技术，synchronized 关键字及 wait()、notify()、notifyAll() 这三个方法都是管程的组成部分

- 管程和信号量是等价的，所谓等价指的是用管程能够实现信号量，也能用信号量实现管程

- 管理共享变量以及对共享变量的操作过程，让他们支持并发。翻译为 Java 领域的语言，就是管理类的成员变量和成员方法，让这个类是线程安全的

## MESA 模型

### 互斥

- 管程解决互斥问题的思路很简单，就是将共享变量及其对共享变量的操作统一封装起来

### 同步

- wait()、notify()、notifyAll() 实现 __条件等待队列__

### synchronized 和 lock 的区别

类别 | synchronized | Lock
---| ---|---
存在层次| java 关键字，在jvm层面实现 | java实现，底层依赖cpu指令,是个接口
锁的释放| 执行完毕同步代码或者抛出异常 | 主动的unlock锁
锁的获取 | 实例方法（this），静态方法(class)，代码块| lock()【等待类似synchronized】,tryLock()【不等待】
锁的状态 | 不可获知 | tryLcok()
锁的类型 | 非公平锁，不能区分读写| ReentrantLock 默认非公平锁，可设置未公平锁。通过sync 抽象内部类【底层是AQS】
中断特性 | synchronized 会导致线程无限等待，不可中断 | lockInterruptibly



![synchronizedandlock](https://i.loli.net/2020/04/12/IQrZ7HtVxqo2hF3.jpg)


---

## synchronized实现原理：

- 对象监视器锁（monitorenter/monitorexit）被占用时就获取了锁
   
- 两个指令的执行是JVM通过调用操作系统的互斥原语 __mutex__ 来实现，被阻塞的线程会被挂起、等待重新调度，会导致“用户态和内核态”两个态之间来回切换，对性能有较大影响。

- 对象头里面 Mark Word 就是synchronized所使用的锁

![synchronized底层实现](https://i.loli.net/2020/04/11/K1AgkXVqjBusywp.jpg)

## java对象监视器各种锁的特点

### 自旋锁：

（1）前提：线程的阻塞和唤醒需要CPU从用户态转为核心态，频繁的阻塞和唤醒对CPU来说是一件负担很重的工作，势必会给系统的并发性能带来很大的压力。
同时我们发现在许多应用上面，对象锁的锁状态只会持续很短一段时间，为了这一段很短的时间频繁地阻塞和唤醒线程是非常不值得的

（2）定义：就是指当一个线程尝试获取某个锁时，如果该锁已被其他线程占用，就一直循环检测锁是否被释放，而不是进入线程挂起或睡眠状态。

（3）场景：**自旋锁适用于锁保护的临界区很小的情况，临界区很小的话，锁占用的时间就很短**。

### 偏向锁 (偏向锁主要用来优化同一线程多次申请同一个锁的竞争)

（1）前提：在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，为了让线程获得锁的代价更低，引进了偏向锁。

（2）场景：偏向锁是在单线程执行代码块时使用的机制，如果在多线程并发的环境下（即线程A尚未执行完同步代码块，线程B发起了申请锁的申请），则一定会转化为轻量级锁或者重量级锁。

（3）目的：为了在没有多线程竞争的情况下尽量减少不必要的轻量级锁执行路径。因为轻量级锁的加锁解锁操作是需要依赖多次CAS原子指令的，而偏向锁只需要在置换ThreadID的时候依赖一次CAS原子指令

（4）定义：一旦线程第一次获得了监视对象，之后让监视对象“偏向”这个线程，之后的多次调用则可以避免CAS操作，说白了就是置个变量，如果发现为true则无需再走各种加锁/解锁流程。

 偏向锁的作用就是，当一个线程再次访问这个同步代码或方法时，该线程只需去对象头的 Mark Word 中去判断一下是否有偏向锁指向它的 ID，无需再进入 Monitor 去竞争对象了。
当对象被当做同步锁并有一个线程抢到了锁时，锁标志位还是 01，“是否偏向锁”标志位设置为 1，并且记录抢到锁的线程 ID，表示进入偏向锁状态。

### 轻量级锁

（1）场景：当关闭偏向锁功能或者多个线程竞争偏向锁导致偏向锁升级为轻量级锁，则会尝试获取轻量级锁

（2）目的：引入轻量级锁的主要目的是在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗

（3）前提：“对于绝大部分的锁，在整个生命周期内都是不会存在竞争的”，如果打破这个依据则除了互斥的开销外，还有额外的CAS操作，因此在有多线程竞争的情况下，轻量级锁比重量级锁更慢。

 当有另外一个线程竞争获取这个锁时，由于该锁已经是偏向锁，当发现对象头 Mark Word 中的线程 ID 不是自己的线程 ID，就会进行 CAS 操作获取锁，如果获取成功，直接替换 Mark Word 中的线程 ID 为自己的 ID，该锁会保持偏向锁状态；
 如果获取锁失败，代表当前锁有一定的竞争，偏向锁将升级为轻量级锁。


### 重量级锁

（1）前提：Synchronized 是通过对象内部的一个叫做 __监视器锁__ Monitor来实现的。 但是监视器锁本质又是依赖于底层的操作系统的 __Mutex
Lock__ 来实现的。而操作系统实现线程之间的切换这就需要从用户态转换到核心态，这个成本非常高，状态之间的转换需要相对比较长的时间，这就是为什么Synchronized效率低的原因。因此，这种依赖于操作系统Mutex
Lock所实现的锁我们称之为 “重量级锁”。

---
#### 锁升级

![锁升级过程.png](https://i.loli.net/2020/04/11/vwiacrWs65eLd3m.png)

### java对象头

- Mark Word用于存储对象自身的运行时数据，如：哈希码（HashCode）、GC分代年龄、锁状态标志、线程持有的锁、偏向线程 ID、偏向时间戳等。

  ![MarkWord.jpg](https://i.loli.net/2020/04/11/TMvoOelwNzFu519.jpg)

- 对象头的Mark Word中的**Lock Word指向 Lock Record的起始地址**,同时Lock Record中有一个Owner字段存放拥有该锁的**线程的唯一标识**（或者object mark word），表示该锁被这个线程占用

- CAS是靠硬件实现的，JVM只是封装了汇编调用，那些AtomicInteger类便是使用了这些封装后的接口。

- 轻量级锁是为了在线程交替执行同步块时提高性能，而偏向锁则是在只有一个线程执行同步块时进一步提高性能。

- JDK1.5中，synchronized是性能低效的。因为这是一个重量级操作，它对性能最大的影响是阻塞的是实现，挂起线程和恢复线程的操作都需要转入内核态中完成，这些操作给系统的并发性带来了很大的压力。
  相比之下使用Java提供的Lock对象，性能更高一些。多线程环境下，synchronized的吞吐量下降的非常严重，而ReentrankLock则能基本保持在同一个比较稳定的水平上。
  到了JDK1.6，发生了变化，对synchronize加入了很多优化措施，有自适应自旋，锁消除，锁粗化，轻量级锁，偏向锁等等。导致在JDK1.6上synchronize的性能并不比Lock差。
  官方也表示，他们也更支持synchronize，在未来的版本中还有优化余地，所以还是提倡在synchronized能实现需求的情况下，优先考虑使用synchronized来进行同步。

### 其他详细比较

![synchronizedVSReentrantCondition](https://s1.ax1x.com/2020/05/01/JOyBrj.png)


----

## synchronized关键字 问题

（1）synchronized的特性？

（2）synchronized的实现原理？

（3）synchronized是否可重入？

（4）synchronized是否是公平锁？

（5）synchronized的优化？

（6）synchronized的五种使用方式？

## 简介

synchronized关键字是Java里面最基本的同步手段，它经过编译之后，会在同步块的前后分别**生成 monitorenter 和 monitorexit 字节码指令**，这两个字节码指令都需要一个引用类型的参数来指明要锁定和解锁的对象。

### 实现原理

在学习Java内存模型的时候，我们介绍过两个指令：lock 和 unlock。

lock，锁定，作用于主内存的变量，它把主内存中的变量标识为一条线程独占状态。

unlock，解锁，作用于主内存的变量，它把锁定的变量释放出来，释放出来的变量才可以被其它线程锁定。

但是这两个指令并没有直接提供给用户使用，而是提供了两个更高层次的指令 monitorenter 和 monitorexit 来隐式地使用 lock 和 unlock 指令。

而 synchronized 就是使用 monitorenter 和 monitorexit 这两个指令来实现的。

根据JVM规范的要求，在执行monitorenter指令的时候，首先要去尝试获取对象的锁，如果这个对象没有被锁定，或者当前线程已经拥有了这个对象的锁，就把锁的计数器加1，相应地，在执行monitorexit的时候会把计数器减1，当计数器减小为0时，锁就释放了。

我们还是来上一段代码，看看编译后的字节码长啥样来学习：

```java
public class SynchronizedTest {

    public static void sync() {
        synchronized (SynchronizedTest.class) {
            synchronized (SynchronizedTest.class) {
            }
        }
    }

    public static void main(String[] args) {

    }
}
```

我们这段代码很简单，只是简单地对SynchronizedTest.class对象加了两次synchronized，除此之外，啥也没干。

编译后的sync()方法的字节码指令如下，为了便于阅读，彤哥特意加上了注释：

```java
// 加载常量池中的SynchronizedTest类对象到操作数栈中
0 ldc #2 <com/coolcoding/code/synchronize/SynchronizedTest>
// 复制栈顶元素
2 dup
// 存储一个引用到本地变量0中，后面的0表示第几个变量
3 astore_0
// 调用monitorenter，它的参数变量0，也就是上面的SynchronizedTest类对象
4 monitorenter
// 再次加载常量池中的SynchronizedTest类对象到操作数栈中
5 ldc #2 <com/coolcoding/code/synchronize/SynchronizedTest>
// 复制栈顶元素
7 dup
// 存储一个引用到本地变量1中
8 astore_1
// 再次调用monitorenter，它的参数是变量1，也还是SynchronizedTest类对象
9 monitorenter
// 从本地变量表中加载第1个变量
10 aload_1
// 调用monitorexit解锁，它的参数是上面加载的变量1
11 monitorexit
// 跳到第20行
12 goto 20 (+8)
15 astore_2
16 aload_1
17 monitorexit
18 aload_2
19 athrow
// 从本地变量表中加载第0个变量
20 aload_0
// 调用monitorexit解锁，它的参数是上面加载的变量0
21 monitorexit
// 跳到第30行
22 goto 30 (+8)
25 astore_3
26 aload_0
27 monitorexit
28 aload_3
29 athrow
// 方法返回，结束
30 return
```

字节码比较简单，我们的synchronized锁定的是SynchronizedTest类对象，可以看到它从常量池中加载了两次SynchronizedTest类对象，分别存储在本地变量0和本地变量1中，解锁的时候正好是相反的顺序，先解锁变量1，再解锁变量0，实际上变量0和变量1指向的是同一个对象，所以synchronized是可重入的。


## 原子性、可见性、有序性

前面讲解Java内存模型的时候我们说过内存模型主要就是用来解决缓存一致性的问题的，而缓存一致性主要包括原子性、可见性、有序性。

那么，synchronized关键字能否保证这三个特性呢？

还是回到Java内存模型上来，synchronized关键字底层是通过monitorenter和monitorexit实现的，而这两个指令又是通过lock和unlock来实现的。

而lock和unlock在Java内存模型中是必须满足下面四条规则的：

（1）一个变量同一时刻只允许一条线程对其进行lock操作，但lock操作可以被同一个线程执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才能被解锁。

（2）如果对一个变量执行lock操作，将会清空工作内存中此变量的值，在执行引擎使用这个变量前，需要重新执行load或assign操作初始化变量的值；

（3）如果一个变量没有被lock操作锁定，则不允许对其执行unlock操作，也不允许unlock一个其它线程锁定的变量；

（4）对一个变量执行unlock操作之前，必须先把此变量同步回主内存中，即执行store和write操作；

通过规则（1），我们知道对于lock和unlock之间的代码，同一时刻只允许一个线程访问，所以，synchronized是具有原子性的。

通过规则（1）（2）和（4），我们知道每次lock和unlock时都会从主内存加载变量或把变量刷新回主内存，而lock和unlock之间的变量（这里是指锁定的变量）是不会被其它线程修改的，所以，synchronized是具有可见性的。

通过规则（1）和（3），我们知道所有对变量的加锁都要排队进行，且其它线程不允许解锁当前线程锁定的对象，所以，synchronized是具有有序性的。

综上所述，synchronized是可以保证原子性、可见性和有序性的。

## 公平锁 VS 非公平锁

通过上面的学习，我们知道了synchronized的实现原理，并且它是可重入的，那么，它是否是公平锁呢？

直接上菜：

```java
public class SynchronizedTest {

    public static void sync(String tips) {
        synchronized (SynchronizedTest.class) {
            System.out.println(tips);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->sync("线程1")).start();
        Thread.sleep(100);
        new Thread(()->sync("线程2")).start();
        Thread.sleep(100);
        new Thread(()->sync("线程3")).start();
        Thread.sleep(100);
        new Thread(()->sync("线程4")).start();
    }
}
```

在这段程序中，我们起了四个线程，且分别间隔100ms启动，每个线程里面打印一句话后等待1000ms，如果synchronized是公平锁，那么打印的结果应该依次是 线程1、2、3、4。

但是，实际运行的结果几乎不会出现上面的样子，所以，synchronized是一个非公平锁。

## 锁优化

Java在不断进化，同样地，Java中像synchronized这种古老的东西也在不断进化，比如ConcurrentHashMap在jdk7的时候还是使用ReentrantLock加锁的，在jdk8的时候已经换成了原生的synchronized了，可见synchronized有原生的支持，它的进化空间还是很大的。

那么，synchronized有哪些进化中的状态呢？

我们这里稍做一些简单地介绍：

（1）偏向锁，是指一段同步代码一直被一个线程访问，那么这个线程会自动获取锁，降低获取锁的代价。

（2）轻量级锁，是指当锁是偏向锁时，被另一个线程所访问，偏向锁会升级为轻量级锁，这个线程会通过自旋的方式尝试获取锁，不会阻塞，提高性能。

（3）重量级锁，是指当锁是轻量级锁时，当自旋的线程自旋了一定的次数后，还没有获取到锁，就会进入阻塞状态，该锁升级为重量级锁，重量级锁会使其他线程阻塞，性能降低。

## 总结

（1）synchronized在编译时会在同步块前后生成monitorenter和monitorexit字节码指令；

（2）monitorenter和monitorexit字节码指令需要一个引用类型的参数，基本类型不可以哦；

（3）monitorenter和monitorexit字节码指令更底层是使用Java内存模型的lock和unlock指令；

（4）synchronized是可重入锁；

（5）synchronized是非公平锁；

（6）synchronized可以同时保证原子性、可见性、有序性；

（7）synchronized有三种状态：偏向锁、轻量级锁、重量级锁；

## 彩蛋——synchronized的五种使用方式

通过上面的分析，我们知道synchronized是需要一个引用类型的参数的，而这个引用类型的参数在Java中其实可以分成三大类：类对象、实例对象、普通引用，使用方式分别如下：

```java
public class SynchronizedTest2 {

    public static final Object lock = new Object();

    // 锁的是SynchronizedTest.class对象
    public static synchronized void sync1() {

    }

    public static void sync2() {
        // 锁的是SynchronizedTest.class对象
        synchronized (SynchronizedTest.class) {

        }
    }

    // 锁的是当前实例this
    public synchronized void sync3() {

    }

    public void sync4() {
        // 锁的是当前实例this
        synchronized (this) {

        }
    }

    public void sync5() {
        // 锁的是指定对象lock
        synchronized (lock) {

        }
    }
}
```

在方法上使用synchronized的时候要注意，会隐式传参，分为静态方法和非静态方法，静态方法上的隐式参数为当前类对象，非静态方法上的隐式参数为当前实例this。

另外，多个synchronized只有锁的是同一个对象，它们之间的代码才是同步的，这一点在使用synchronized的时候一定要注意。

## 推荐阅读

1. [死磕 java同步系列之JMM（Java Memory Model）](https://mp.weixin.qq.com/s/jownTN--npu3o8B4c3sbeA)

2. [死磕 java同步系列之volatile解析](https://mp.weixin.qq.com/s/TROZ4BhcDImwHvhAl_I_6w)



