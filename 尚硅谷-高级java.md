## 多线程

java语言的JVM允许程序运行多个线程。它通过java.lang.Thread类来实现

新建一个类继承于Thread类--重写Thread类中的run()方法，在该方法中写需要多线程执行的方法体，在main()方法中调用Thread类中的start()方法

```java
package thread;

/**
 *
 * 多线程的创建：
 * 方式一：继承于Thread类
 *（1）创建一个继承于Thread类的子类
 *（2）重写Thread类的run()--->将此线程执行的操作声明在run()中
 *（3）创建Thread类的子类的对象
 * (4）通过对象调用start()
 * @param args
 *
 * Created by KingsLanding on 2022/3/8 19:12
 */
public class ThreadTest {
    public static void main(String[] args) {
        MyThread m1 = new MyThread();//new一个Thread的子类的对象
        
        //调用Thread类中的start()方法；启动当前线程，调用当前线程的run()
        //不用等待，新建了一个线程，并行运行，输出Thread-i，即在Thread线程中执行
        m1.start();
        //一个对象只能调用一次start()
        
        //不能通过直接调用run()的方式启动线程，这种方式仍然是在主线程中运行，输出main-i
        m1.run();
        
        //以下操作任然在主线程中
        for(int i=0;i<=100;i++){
            if(i%2==0){
                //输出main-i
                System.out.println(Thread.currentThread().getName()/*获取线程的名字*/+"-"+i);
            }
        }
    }
}
//创建一个继承于Thread类的子类
class MyThread extends Thread{
   //重写Thread类的run()方法

    @Override
    public void run() {
        //遍历0-100的偶数
        for(int i=0;i<=100;i++){
            if(i%2==0){
                System.out.println(Thread.currentThread().getName()/*获取线程的名字*/+"-"+i);
            }
        }
    }
}

```

### Thread常用方法

```java
package thread;

/**
 * 测试Thread类中的常用方法：
 * 1.start()；启动当前线程，调用当前线程的方法体run()
 * 2.run();重写Thread类中的run()方法，将需要线程实现的方法体写在其中
 * 3.currentThread();静态方法，返回执行当前代码的线程
 * 4.getName()；获取当前线程的名字
 * 5.setName()；设置当前线程的名字：在调用之前设置【对象.setName("名称")】；
 * 给主线程改名【Thread.currentThread.setName("名称"）】
 * 6.yield()：释放当前CPU的执行权
 * 7.join()：掐断原本的线程，切换到其他线程，执行完后再返回调用
 * 在线程a中调用线程b的join()，此时线程a就进入阻塞状态，直到b线程执行完再执行a
 * 8.stop();已过时，执行此方法时，强制结束当前线程
 * 9.sleep(long millitime);阻塞，在指定的millitime毫秒时间内，当前线程阻塞状态
 * 10.isAlive();判断当前线程是否存活
 *
 *
 * Created by KingsLanding on 2022/3/9 13:51
 */
public class ThreadTest02 {
    public ThreadTest02() {
    }

    public static void main(String[] args) {

        Mythread m1 = new Mythread();

        m1.setName("线程-1");
        m1.start();

        //改主线程的名字
        Thread.currentThread().setName("主线程-2");
        for(int i=0;i<=100;i++){
            if(i%2==0){
                System.out.println(Thread.currentThread().getName()+"/*****/"+i);
            }
            
            //强行换路
            if(i==50){
                try {
                    m1.join();//该条件触发时，掐断原本的线程，切换到其他线程，
                             // 直到那个线程执行完成后再调用本身这个线程
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
class Mythread extends Thread{
    @Override
    public void run() {
        for(int i=0;i<=100;i++){
            if(i%2 != 0){

                //sleep阻塞线程，单位毫秒
                try {//快捷键：ctrl+alt+T
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }

                System.out.println(Thread.currentThread().getName()+"/*****/"+i);
            }
            if(i%10==0){
                yield();//当i可以除以10 线程主动停止一下，但仍然会再次调用线程
            }
        }
    }
}

```

### 多线程优先级设置

**getPriority()**:获取当前线程的优先级

**setPriority()**:设置线程的优先级

**线程的优先级**
***MAX_PRIORITY:10***   max_priority
***MIN_PRIORITY:1***
***NORM_PRIORITY:5***

```java
package thread;

/**
 * 线程的优先级
 * 1.
 * MAX_PRIORITY:10   max_priority
 * MIN_PRIORITY:1
 * NORM_PRIORITY:5
 * 2.获取和设置当前线程的优先级
 *   getPriority():获取当前线程的优先级
 *   setPriority():设置线程的优先级
 *
 * 说明：高优先级的线程要抢占低优先级线程的CPU执行权，但只是概率上的
 *       高优先级的线程比低优先级的线程被执行的概率大；而不是低优先级线程不执行
 *
 * Created by KingsLanding on 2022/3/10 18:23
 */
public class ThreadTest03 {
    public static void main(String[] args) {
        myThread m1 = new myThread();
        m1.setName("分线程");
        //设置分线程的优先级
        m1.setPriority(Thread.MAX_PRIORITY);
        m1.start();

        Thread.currentThread().setName("main线程");
        //设置主线程优先级
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        for(int i=0;i<=100;i++){
            if(i%2!=0){
                System.out.println(Thread.currentThread().getName()+"/**/"+
                                   Thread.currentThread().getPriority()+"/*****/"+i);
            }
        }
    }
}
class myThread extends Thread{

    @Override
    public void run() {
        for(int i=0;i<=100;i++){
            if(i%2 == 0){
                System.out.println(getName()+"/**/"+getPriority()+"/*****/"+i);
            }
        }
    }
}
```

### 线程安全问题

```java
package thread;

/**
 * 三个线程共同递减输出100-0的数字：多线程的安全问题
 *
 * Created by KingsLanding on 2022/3/11 11:25
 */
public class ThreadTest04 {
    public static void main(String[] args) {

        MyThread03 m1 = new MyThread03();
        MyThread03 m2 = new MyThread03();
        MyThread03 m3 = new MyThread03();
        m1.setName("分线程-1**");
        m1.start();
        m2.setName("分线程-2**");
        m2.start();
        m3.setName("分线程-3**");
        m3.start();
    }

}

class MyThread03 extends Thread{
    private static int ticket=100;//共享ticket;但是会暴露线程安全问题

    public void run(){
        while (true){
        if(ticket>0){
            System.out.println(getName()+"票号："+ticket);
            ticket--;
        }else {
            break;
        }
        }
    }
}
```

### 实现Runnable接口的方式创建多线程

- **共享数据**

```java
package thread;

/**
 * 创建多线程的方式：
 * 1.创建一个实现了Runnable接口的类（该接口已定义了）
 * 2.实现类去实现Runnable类中的run()方法
 * 3.创建实现类的对象
 * 4.将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象
 * 5.通过Thread类的对象调用start()方法
 *
 * 比较两种创建多线程方法
 * 开发中优先选择实现Runnable接口的方法
 * 1.实现方式没有类的单继承性的局限性
 * 2.实现的方式更适合来处理多个有共享数据的情况
 *
 * 存在线程安全问题;本该只出现一次的数字多次重复出现，甚至错误数字；
 *
 * Created by KingsLanding on 2022/3/11 12:10
 */
public class ThreadTest05 {
    public static void main(String[] args) {
        MyThread05 M1 = new MyThread05();//将这100封装到M1对象中
        Thread T1 = new Thread(M1);//将M1封装(传递）到Thread对象T1中；
        Thread T2 = new Thread(M1);
        T1.start();
        T2.start();

    }
}
class MyThread05 implements Runnable{
    private int ticket=100;
    @Override
    public void run() {
        while (true){
            if(ticket>0){
                System.out.println(Thread.currentThread().getName()+"票号："+ticket);
                ticket--;
            }else {
                break;
            }
        }
    }
}

```

### 线程同步（安全机制）

#### 方法一

***synchronized***(同步监视器)｛

​			//需要同步（上锁）的代码块

｝

**实现Runnable接口的方法创建的多线程**

```java
package thread;

/**
 * 1.问题：买票过程中（遍历100-1）的过程中出现了重票、错票----即线程的安全问题
 * 2.问题的原因：当某个线程操作车票的过程中，尚未操作完成时，其他线程参与了进来，
 *              前一个线程还没完全执行，后一个线程也拿到了资源
 * 3.解决方案：规定（上锁）线程a在操作共享数据ticket时，其他线程被锁住无法参与进来
 *             直到a线程操作完，拿走了ticket的数据时，其他线程才开始操作ticket。
 *             且即使a出现了阻塞，也不能改变。
 * 4.java中，通过同步机制来解决线程的安全问题
 *
 * 方式一：
 * synchronized(同步监视器）｛
 *      //需要被同步的地方
 * ｝
 * 说明：
 * 1.操作共享数据的代码块就是需要被同步的地方
 * 2.同步监视器：俗称“锁”任何一个类的对象，都可以充当锁：多个线程只能用一个锁
 *
 * 5.同步的方式能解决线程的安全问题；但是操作同步代码的只能有一个线程参与，其他线程只能
 *   在“门”外等待。相当于是一个单线程过程，效率低；
 *
 * Created by KingsLanding on 2022/3/14 10:37
 */
public class ThreadTest06 {
    public static void main(String[] args) {

        MyThread06 m1 = new MyThread06();
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m1);

        t1.start();
        t2.start();
    }
}
class MyThread06 implements Runnable{
    private int ticket=100;
    Lock L1=new Lock();//同步监视器类对象
    @Override
    public void run() {
        while (true){
            
            synchronized (L1) {//上锁
                if (ticket > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }
                    System.out.println(Thread.currentThread().getName() + "票号：" + ticket);
                    ticket--;
                } else {
                    break;
}}}}}
class Lock{//同步监视器的类

}
```

**继承Thread类的方式的多线程**

**慎用this充当同步监视器**

```java
package thread;

/**
 * 继承方式创建的多线程
 * 同步机制synchronized (同步监视器)｛
 * ｝
 *
 * 说明：这种方式中慎用this充当同步监视器，考虑用当前类来充当同步监视器
 *
 * Created by KingsLanding on 2022/3/14 19:31
 */
public class ThreadTest07 {
    public static void main(String[] args) {
        MyThread07 m1 = new MyThread07();
        MyThread07 m2 = new MyThread07();

        m1.start();
        m2.start();
    }
}
class MyThread07 extends Thread {
    //private int ticket = 100;
    private static int ticket = 100;
   // Lock01 L1=new Lock01();//这种方式依然不行，因为多个对象反复调用这个结构，并不是唯一的锁
                           //相当于其他线程有其他钥匙
    private static Lock01 L1=new Lock01();
    @Override
    public void run() {
        while (true) {
            //类也是对象
            //synchronized(ThreadTest07.class){//Class cla = ThreadTest07.class只会加载一次
            // }
            synchronized (L1) {
                //synchronized (this) {//继承方式中不能用this，因为创建了多个MyThread07对象
                if (ticket > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }
                    System.out.println(Thread.currentThread().getName() + "票号：" + ticket);
                    ticket--;
                } else {
                    break;
}}}}}
class Lock01{

}

```

#### 方法二

**同步方法**

- 1.同步方法仍然涉及到同步监视器，只是不需要显示定义
 *  2.非静态的同步方法，同步监视器就是this
 *  3.静态的同步方法中，同步监视器是：当前类本身

***同步方法解决实现Runnable接口的线程方法***

```java
package thread;

/**
 * 方式二：同步方法解决实现Runnable接口的线程方法
 * 如果操作共享数据的代码完整的声明在一个方法中，不妨将该方法声明为同步的
 *
 * Created by KingsLanding on 2022/3/16 15:48
 */
public class ThreadTest08 {
    public static void main(String[] args) {
        MyThread08 m1 = new MyThread08();
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m1);
        t1.start();
        t2.start();
    }
}
class MyThread08 implements Runnable{
    private int ticket=100;
    @Override
    public void run() {
        Run();
    }
    private synchronized void Run(){
        while (true) {
            if (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + "票号：" + ticket);
                ticket--;
}}}}
```

***同步方法处理继承Thread类的方式创建的线程的安全问题***

```java
package thread;

/**
 * 方式二：同步方法处理继承Thread类的方式创建的线程的安全问题
 *
 *  总结：同步方法
 *  1.同步方法仍然涉及到同步监视器，只是不需要显示定义
 *  2.非静态的同步方法，同步监视器就是this
 *  3.静态的同步方法中，同步监视器是：当前类本身（类本身也是对象）
 *
 * Created by KingsLanding on 2022/3/16 16:06
 */
public class ThreadTest09 {
    public static void main(String[] args) {
        MyThread09 m1 = new MyThread09();
        MyThread09 m2 = new MyThread09();

        m1.start();
        m2.start();
    }
}
class MyThread09 extends Thread {
    private static int ticket = 100;

    public void run() {
        Run();
    }
      private static synchronized void Run(){//同步监视器：MyThread09.class
    //private synchronized void Run(){//这种方式同步监视器m1,m2，而锁只能有一把
        while (true){
            if (ticket > 0){
                System.out.println(Thread.currentThread().getName() + "票号：" + ticket);
                ticket--;
            }else {
                break;
}}}}
```

#### Lock锁

- JDK5.0新特性 建议用Lock

- try{

  ​		lock.lock();//启动锁

  ​		//目标代码块

  ​       } finally {

  ​		lock.unlock();//关闭锁

     }

```java
package thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock锁：解决线程安全问题(JDK5.0新增）；传统的用synchronized;建议用Lock
 *
 * 说明：创建ReentrantLock类对象；private ReentrantLock lock = new ReentrantLock();
 *       在需要同步（上锁）代码块使用lock对象调用lock()方法
 *
 *  注意：lock对象也要保证唯一性，用继承Thread的方式创建的多线程应当使用static
 *
 * 面试题：synchronized 与 Lock 的异同
 * 同：两种方法都可以解决线程安全问题
 * 异：synchronized机制在执行完相应的代码后自动的释放同步监视器
 *     Lock需要手动的启动同步(调用lock())，手动的结束同步（调用unlock()方法）
 *
 *
 * Created by KingsLanding on 2022/3/17 16:48
 */
public class ThreadTest10 {
    public static void main(String[] args) {
        MyThread10 m1 = new MyThread10();
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m1);

        t1.start();
        t2.start();
    }
}
class MyThread10 implements Runnable{
    private int ticket=100;
    //实例化ReentrantLock
    private ReentrantLock lock=new ReentrantLock();
    @Override
    public void run() {
        while (true) {
            try{
                lock.lock();
                if (ticket > 0) {
                    //调用锁定lock()方法
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }
                    System.out.println(Thread.currentThread().getName() + "票号：" + ticket);
                    ticket--;
                } else {
                    break;
                }
            }finally{
                //调用解锁方法unlock()
                lock.unlock();
}}}}
```

#### Lock锁的基本方法

```java
1、boolean tryLock()
如果锁可用, 则获取锁, 并立即返回 true, 否则返回 false. 该方法和lock()的区别在于
1、tryLock()
只是"试图"获取锁, 如果锁不可用, 不会导致当前线程被禁用,当前线程仍然继续往下执 行代码.
2、lock()
是一定要获取到锁, 如果锁不可用, 就一直等待, 在未获得锁之前,当前线程并不继 续向下执行.
2、void unlock()：
执行此方法时, 当前线程将释放持有的锁. 锁只能由持有者释放, 如果线程并不持有 锁,却执行该方法,
可能导致异常的发生
3、getHoldCount()
查询当前线程保持此锁的次数，也就是执行此线程执行 lock 方法的次数。
4、getQueueLength（）
返回正等待获取此锁的线程估计数，比如启动 10 个线程， 1 个线程获得锁，此时返回的是 9
微信号：mahukang联系我们获取更多资源 公众号：码出宇宙 xiaomage0086
5、getWaitQueueLength（Condition condition）
返回等待与此锁相关的给定条件的线程估计 数。比如 10 个线程，
用同一个 condition 对象，并且此时这 10 个线程都执行了condition 对象的 await 方法，那么此时
执行此方法返回 10
6、hasWaiters(Condition condition)
查询是否有线程等待与此锁有关的给定条件(condition)，对于 指定contidion 对象，有多少线程执
行了 condition.await 方法
7、Condition newCondition()
才能调用该组件的 await()方法，而调用后，当前线程将缩放锁。
8、hasQueuedThread(Thread thread)
查询给定线程是否等待获取此锁
9、hasQueuedThreads()
是否有线程等待此锁
10、isFair()
该锁是否公平锁
11、sHeldByCurrentThread()
当前线程是否保持锁锁定，线程的执行 lock 方法的前后分别是 false 和true
12、isLock()
此锁是否有任意线程占用
13、lockInterruptibly（）
如果当前线程未被中断，获取锁
14、tryLock（）
尝试获得锁，仅在调用时锁未被线程占用，获得锁
15、tryLock(long timeout TimeUni unit)
如果锁在给定等待时间内没有被另一个线程保持，则获取 该锁 。
```

### 线程通信

#### wait();

作用：阻塞线程，必须在同步代码块或同步方法中使用（synchronized())

#### notify();

作用：唤醒被wait()阻塞的线程，必须在同步代码块或同步方法中使用（synchronized())

#### notifyAll();

作用：唤醒所有被阻塞的线程，必须在同步代码块或同步方法中使用（synchronized())

```java
package thread01;

/**
 * 线程通信
 * 创建遍历1-100；由两个线程交替打印出结果
 *
 * 涉及到的方法
 * 1.wait()：一旦执行此方法，当前线程就进入阻塞状态，并释放同步监视器
 * 2.notify()：一旦执行此方法，就会唤醒被wait()阻塞的线程。如果有多个线程被wait()；唤醒优先级别高的
 * 3.notifyAll()：一旦执行此方法，就会唤醒所有被阻塞的线程
 *
 * wait();notify();notifyAll();三个方法必须使用在同步代码块或同步方法中（synchronized）Lock不能使用这个方法
 * wait();notify();notifyAll();三个方法的调用者必须是同步代码块或同步方法中的同步监视器（就是唯一的对象）否则将出现IllegalMonitorStateException异常
 *
 * wait();notify();notifyAll();三个方法定义在java.lang.Object中
 *
 * Created by KingsLanding on 2022/3/17 17:56
 */
public class ThreadTest01 {
    public static void main(String[] args) {
        MyThread01 m1 = new MyThread01();
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m1);
        t1.setName("线程1");
        t2.setName("线程2");
        t1.start();
        t2.start();

    }
}
class MyThread01 implements Runnable{
    private int number=1;
    //Object obj = new Object();
    @Override
    public void run() {
        while(true) {

            //synchronized (obj)
        synchronized (this){
            //唤醒，
                this.notify();
                //obj.notify();
                if (number <= 100) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }

                    System.out.println(Thread.currentThread().getName() + ":" + number);
                    number++;
                    //使线程运行到此阻塞
                    try {
                        this.wait();//会释放锁
                        //obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                    }

                }else {
                    break;
}}}}}
```

#### sleep()和wait()异同

- 同：

*一旦执行方法，线程都将进入阻塞状态*

- 异：

*1.两个方法声明的位置不同；Thread类中声明sleep()，Object类中声明wait()*

*2.调用的要求的不同：sleep()可以在任何需要的场景下调用。wait()必须在同步代码块或同步方法中使用*

*3.关于是否调用同步监视器：如果两个方法都使用在同步代码块或同步方法中sleep()不会释放锁，wait()会释放锁*

### JDK5.0新增线程创建方式

#### 方式一：Callable

- 实现Callable接口call()方法

**为什么说Callable接口方法比Runnable方法更强大**

- *1.call()方法可以有返回值*

- *2.call()可以抛出异常，被外面的操作捕获，获取异常的信息*

- *3.call()是支持泛型的*


```java
package Thread02;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的方式三，实现Callable接口。---JDK5.0新增
 *
 * Created by KingsLanding on 2022/3/20 22:01
 */
public class ThreadTest01 {
    public static void main(String[] args) {
        //3.创建Callable实现类的对象
        MyThread m1 = new MyThread();

        //4.将Callable接口实现类对象最为参数传递到FutureTask构造器中，创建FutureTask对象
        FutureTask F1 = new FutureTask(m1);

        //5.将FutureTask的对象作为阐述传递到Thread构造器中，创建Thread对象
        Thread T1 = new Thread(F1);
        T1.start();

        try {
            //获取Callable中call()方法的返回值
            //get()返回值即为FutureTask构造器参数Callable实现类重写的call()的返回值
            Object sum = F1.get();
            System.out.println(sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
        }
    }
}

//1.创建实现Callable接口的实现类
class MyThread implements Callable {

    //实现call()方法，将需要执行的操作声明在call()中
    @Override
    public Object call() throws Exception {
        int sum=0;
        for(int i=1;i<=100;i++){
            if(i%2==0){
                System.out.println(i);
                sum+=i;
            }
        }
        return sum;
    }
}
```

#### 方式二：线程池

- 经常创建和销毁、使用量特别大的资源，比如并发情况下的线程,对性能影响很大

- 提前创建好多个线程，放入线程池中，使用时直接获取，使用完放回池中。可以避免频繁创建销毁、实现重复利用

- 好处:

  ➢提高响应速度(减少了创建新线程的时间)
  ➢降低资源消耗(重复利用线程池中线程，不需要每次都创建)
  ➢便于线程管理

  corePoolSize: 核心池的大小
  maximumPoolSize:最大线程数
  keepAliveTime:线程没有任务时最多保持多长时间后会终止

```java
package Thread02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建多线程的方式四：线程池
 *
 * ExecutorService:真正的线程池接口，常见子类ThreadPoolExecutor
 *
 * Executor:工具类、线程池工厂类，创建并返回不同类型的线程池
 *      Executor.newCachedThreadPool():创建一个可根据需要创建新线程的线程池
 *      Executor.newFixedThreadPool(n)：创建一个可重用固定线程数的线程池
 *      Executor.newSingleThreadExecutor():创建一个只有一个线程的线程池
 *      Executor.newScheduledThreadPool(n）：创建一个线程池，可安排在给定延迟后
 *                                           运行命令或定期执行
 *
 *
 * Created by KingsLanding on 2022/3/25 17:10
 */
public class ThreadTest02 {
    public static void main(String[] args) {
        //1.提供指定线程数量的线程池（声明线程池信息）
        ExecutorService service = Executors.newFixedThreadPool(10);

        System.out.println(service.getClass());//查看对象是哪个类造的
        
        MyThread02 m1 = new MyThread02();
        MyThread03 m2 = new MyThread03();

        //2.执行指定的线程的操作，需要提供实现Runnable接口或Callable接口实现类的对象
        service.execute(m1);//适合使用于Runnable
        service.execute(m2);
        
        service.shutdown();//关闭线程池
       // service.submit();//适合适用于Callable
    }
}
class MyThread02 implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<=100;i++){
            if(i%2==0){
                System.out.println(Thread.currentThread().getName()+"偶数"+i);
}}}}

class MyThread03 implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<=100;i++){
            if(i%2 != 0){
                System.out.println(Thread.currentThread().getName()+"奇数"+i);
}}}}
```

# 常用类

## String类

``` java
/**
 * String:字符串，""表示
 * 1.声明为final的，不可被继承
 * 2.实现了Serializable接口，表示字符串是支持序列化的；
 *      实现了Comparable<String>接口，表示String可以比较大小
 *
 * 3.String内部定义了private final char value[];用于存储字符串数据
 * 4.代表不可变的字符序列-->不可变性：
 *      对现有的字符串进行增删改查操作需要重新指定内存区域赋值，不能修改原有的字符串（final)
 *
 * 5.字符串常量池中不会存储相同内容的字符串
 * 6.通过字面量的方式（非new）给一个字符串赋值，声明在常量池中
 */
```

### 不可变性

```java
//不可变性
//字符替换
String s1="abc";
String s2 = s1.replace("a","z");
System.out.println(s1);//abc
System.out.println(s2);//zbc
```

### String实例化的方式

- 常量池
- 堆空间

``` java
/**
 * String 的实例化方式
 * 1.字面量
 * 2.new+构造器的方式
 */
    @Test
    public void test(){
        //字面量定义，这种方式s1的数据abc声明在方法区中的字符串常量池中
        String s1="abc";

        //通过new+构造器方式定义，s2,s3数据在堆空间中开辟空间对应
        String s2=new String("abc");
        //其中创建了两个对象，一个是堆空间中的new结构，另一个是char[]对应的常量池中的数据"abc"
        String s3=new String("abc");

        System.out.println(s1==s2);//false
        System.out.println(s2==s3);//false

    }
```

### 字符串的连接问题

```java
/**
 * 常量与常量的拼接结果在常量池，且常量池不存在相同内容的常量
 * 只要其中有一个是变量，结果就在堆中
 * 如果拼接的结果调用intern（）方法，返回值就在常量池中
 */
@Test
public void test2(){
        String s1="abc";
        String s2="def";

        //常量池:常量与常量连接，结果在常量池，且不存在相同内容的常量
        String s3="abcdef";
        String s4="abc"+"def";
        //堆空间：变量的参与，结果就在堆中
        String s5=s1+s2;
        String s6=s1+"def";

        System.out.println(s1==s3);//false
        System.out.println(s3==s4); //true
        System.out.println(s3==s5); //false
        System.out.println(s5==s6);//false
        System.out.println(s3==s6); //false


        String s7 = s5.intern();//返回值s7使用的常量值是常量池中的
        System.out.println(s3==s7);
    }

		//使用final在String声明前，表示常量
        @Test
        public void test9(){
            String s1="abcdef";
            String s2="abc";
            String s3=s2+"def";
            System.out.println(s1==s3);//false
            final String s4="abc";//常量
            String s5=s4+"def";
            System.out.println(s1==s5);//true
        }
```

### String类的常用方法

```java
@Test
public void test3(){
    String s1="ABc";
    System.out.println(s1.length());//3
    System.out.println(s1.charAt(2));//c返回某索引处的字符return value[index]

    System.out.println(s1.isEmpty());//判空   false

    String s2 = s1.toLowerCase();//大写--->小写
    System.out.println(s1);//不可变性   ABc
    System.out.println(s2);//abc

    System.out.println(s1.equals(s2));//false
    System.out.println(s1.equalsIgnoreCase(s2));//忽略大小写 true

    System.out.println(s1.concat("DEf")); //ABcDEf

    System.out.println(s2.compareTo(s1));//比较大小s2-s1    32

    String s3="substring测试语句";
    System.out.println(s3.substring(3));//string测试语句；不可变性，原数据没变

    System.out.println(s3.substring(3, 11));//string测试；起始于--结束到：[)

}

@Test
public void test4(){
    String s1="abc";
    //字符替换
    System.out.println(s1.replace("a", "A"));
    System.out.println(s1);//不可变性

    String s2="12345678.";
    System.out.println(s2.matches("\\d+"));
    String tel="0852-12345678";
    System.out.println(tel.matches("0852-\\d{7,8}"));

}
```

### String和基本数据类型，包装类之间的转换

``` java
@Test
public void test5(){
    /**
     * String 基本数据类型，包装类之间的转换
     * String-->基本数据类型、包装类：parseDataType()
     * 基本数据类型，包装类-->String重载的valueOf()
     */
    String s1="123";
    int i = Integer.parseInt(s1);
    System.out.println(s1+2);//1232
    System.out.println(i+4);//127

    System.out.println(String.valueOf(i)+2);//1232
}

@Test
public void test6(){
    /**
     * String-->char[]的转换 toCharArray()
     * 反之char[]-->String 调用String构造器
     */
    String s1="123abc";
    char[] chars = s1.toCharArray();

    for(int i=0;i<chars.length;i++){
        System.out.println(chars[i]);
    }

    //char[]-->String
    char[] chars1 = {'a', 'b', 'c'};
    System.out.println(new String(chars1));
}

@Test
public void test8(){
    /**
     * String 与byte[]之间的转换
     * String-->byte[]；getBytes()
     */
    String s1="abc中文编码测试";
    byte[] bytes = s1.getBytes();//默认字符集
    System.out.println(Arrays.toString(bytes));

    try {
        byte[] gbks = s1.getBytes("gbk");//指定字符集
        System.out.println(gbks);

        // byte[]--->String
        String s2 = new String(gbks);//默认字符集
        System.out.println(s2);//乱码；编码集和解码集不一致
        String gbk = new String(gbks, "gbk");
        System.out.println(gbk);

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}
```

## StringBuffer

```java
**
 * Created by KingsLanding on 2022/6/19 20:01
 */
public class StringTest2 {

    /**
     * String、StringBuffer、StringBuilder三者的异同
     * String:不可变的字符序列，底层使用char[]存储
     * StringBuffer:可变的字符序列：线程安全的，效率低，底层使用char[]存储
     * StringBuilder：可变的字符序列：JDK5.0新特性线程不安全的，效率高，底层使用char[]存储
     *
     * 分析：
     *  String str = new String();//char[] value = new char[0];
     *  String str1 = new String("abc");//char[] value = new char[]{'a','b','c'};
     *
     *  StringBuffer str2=new StringBuffer();//char[] value = new char[16];//底层创建了一个长度为16的数组
     *  str2.append('a');//value[0]='a';
     *  str2.append('b');//value[1]='b';
     *
     *  StringBuffer str3=new StringBuffer("abc");//char[] value = new char["abc".length()+16];
     *  System.out.println(str3.length());//3
     *  扩容问题：如果要添加的数据底层数组放不下，即需要扩容底层的数组，默认情况
     *  扩容为原来的2倍+2，同时将原有的数组中的元素复制到新的数组中
     *
     *  建议：开发中使用StringBuffer(int capacity)或者StringBuilder(int capacity)
     */
    @Test
    public void test(){
        String str1=new String("abc");
        System.out.println(str1.replace("a", "b"));
        System.out.println(str1);

        StringBuffer str2=new StringBuffer("abc");
        str2.setCharAt(0,'b');//没有返回值,就是修改的原来的那个数据
        System.out.println(str2);
    }
}
```

## Date类

（1）toString() 显示当前的年月日分秒
（2）getTime()  显示当前对象对应的时间戳（毫秒）

``` java
/**
 * Created by KingsLanding on 2022/6/20 13:55
 */
public class Data {
    @Test
    public void test2(){
        //返回当前时间与1970年1月1日0时0分0秒之间以毫秒为单位的时间差
        //称之为时间戳
        System.out.println(System.currentTimeMillis());
    }

    /**
     * 1.两个构造器的使用
     *      Date()构造器：创建一个当前时间的date对象
     *      指定毫秒数的Date对象
     * 2.两个方法的使用
     *      （1）toString() 显示当前的年月日分秒
     *      （2）getTime()  显示当前对象对应的时间戳（毫秒）
     *
     * 3.java.sql.Date对应数据库中的日期类型的变量
     *      如何将java.util.Date对象转换为java.sql.Date对象
     */
    @Test
    public void test1(){
        //一、Date()构造器：创建一个当前时间的date对象
        Date date = new Date();
        System.out.println(date.toString());
        System.out.println(date.getTime());

        //二、指定毫秒数的Date对象
        Date date1 = new Date(1655704905737L);
        System.out.println(date1.toString());

        //java.sql.Date()的实例化方式
        java.sql.Date date2 = new java.sql.Date(1655704905737L);
        System.out.println(date2.toString());
        //java.util.Date()-->java.sql.Date()
        java.sql.Date date3 = new java.sql.Date(date.getTime());
        System.out.println(date3);

    }
}
```

### jdk8之前的日期时间的API测试


* 1.System中的currentTimeMillis()方法
* 2.java.util.Date和java.sql.Date
* 3.SimpleDateFormat
* 4.Calendar 日历类

``` java
public class SimpleDateFormatTest {

    @Test
    public void test() throws Exception{
        //实例化SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat();
        
        //格式化：日期--->字符串
        Date date=new Date();
        System.out.println(date);
        String format = sdf.format(date);
        System.out.println(format);

        //解析：格式化的逆过程 字符串-->日期
        String s1="22-6-20 下午4:28";
        Date date2 = sdf.parse(s1);
        System.out.println(date2);

        //按照指定的方式格式化和解析，调用带参的构造器
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(sdf1.format(date));
    }

    /**
     * 练习:字符串"2022-6-20"转换为java.sql.Date
     */
    @Test
    public void test3() throws Exception{
        String s1="2022-6-20";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(s1);
        System.out.println(date);

        java.sql.Date date1 = new java.sql.Date(date.getTime());
        System.out.println(date1);
    }
    /**
     * Calendar日历类（抽象类）的使用
     */
    @Test
     public void test4(){
         //创建方式
         //一、创建其子类(GregorianCalender)的对象
         //二、调用其静态方法getInstance();
         Calendar calendar=Calendar.getInstance();

         //常用方法
         //get()
         int days = calendar.get(Calendar.DAY_OF_MONTH);//当月第几天
         System.out.println(days);
         System.out.println(calendar.get(Calendar.DAY_OF_WEEK));//当月第几周

        //set()
        calendar.set(Calendar.DAY_OF_MONTH,10);//设置当月第10天
        days=calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(days);

        //add()
        calendar.add(Calendar.DAY_OF_MONTH,2);//当月第几天+2
        days=calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(days);

        //getTime() 日历类-->Date
        Date date = calendar.getTime();
        System.out.println(date);//Sun Jun 12 18:28:50 CST 2022

        //setTime() Date-->日历类
        calendar.setTime(date);
        days=calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(days);
    }
}
```

###　jdk8之后的日期API

- LocalDate;
- LocalTime;
- **LocalDateTime使用频率更高**

```java
public class DateTime {
    /**
     * LocalDate;LocalTime;LocalDateTime使用频率更高
     * 类似于Calendar
     */
    @Test
    public void test1(){
        //获取当前时间
        LocalDate now = LocalDate.now();
        System.out.println(now);//2022-06-21
        System.out.println(LocalTime.now());//14:41:43.525
        System.out.println(LocalDateTime.now());//2022-06-21T14:41:43.525

        //of()设置具体时间
        System.out.println(LocalDateTime.of(2022, 6, 21, 4, 12));//年，月，日，时，分，秒

        //getXxx():获取相关属性
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.getDayOfMonth());//获取当月历经天数
        System.out.println(localDateTime.getDayOfYear());//获取当年历经天数

        //withXxx()：设置相关属性
        //不可变性
        LocalDateTime localDateTime1 = LocalDateTime.now();
        System.out.println(localDateTime1.withDayOfMonth(23));//设置当月号数
        System.out.println(localDateTime1);
    }

    /**
     * Instant类
     */
    @Test
    public void test2(){
        //now()获取本初子午线上对应的标准时间
        Instant instant = Instant.now();
        System.out.println(instant);

        //atOffset()添加时间偏移量（北京时间）
        Instant instant1 = Instant.now();
        OffsetDateTime offsetDateTime = instant1.atOffset(ZoneOffset.ofHours(8));
        System.out.println(offsetDateTime);

        //toEpochMilli()获取自1970-1-1-0-0-0（UTC）开始的毫秒
        long milli = instant1.toEpochMilli();
        System.out.println(milli);

        //ofEpochMilli()通过给定的毫秒数，获取Instant实例；即毫秒数对应的本初子午线时间
        Instant ofEpochMilli = Instant.ofEpochMilli(1655795103358L);
        System.out.println(ofEpochMilli);
    }

    /**
     * DateTimeFormatter:格式化或解析日期，时间
     * 类似于SimpleDateFormat
     */
    @Test
    public void test3(){
        //方式一；使用预定义的
        DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;

        //格式化：日期-->字符串
        LocalDateTime localDateTime = LocalDateTime.now();
        String format = isoDateTime.format(localDateTime);
        System.out.println(format);

        //解析：字符串-->日期
        TemporalAccessor parse = isoDateTime.parse("2022-06-21T15:17:21.447");
        System.out.println(parse);

        //重点：方式二、自定义格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        //格式化
        LocalDateTime now = LocalDateTime.now();
        String format1 = dateTimeFormatter.format(now);
        System.out.println(format1);
        //解析
        TemporalAccessor parse1 = dateTimeFormatter.parse("2022-06-21 03:34:02");
        System.out.println(parse1);
    }
}
```

#### 重点	自定义格式DateTimeFormatter

```java
 //重点：方式二、自定义格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        //格式化
        LocalDateTime now = LocalDateTime.now();
        String format1 = dateTimeFormatter.format(now);
        System.out.println(format1);
        //解析
        TemporalAccessor parse1 = dateTimeFormatter.parse("2022-06-21 03:34:02");
        System.out.println(parse1);
```

## Compare类

- Comparable接口	***自然排序***

   1.像String、包装类等实现了Comparable接口，重写了compareTo()方法
   2.String、包装类重写了compareTo()方法后，进行了从小到大的排列

```java
@Test
public void test(){
    String[] strings = {"aa", "bb", "dd", "cc"};

    Arrays.sort(strings);
    System.out.println(Arrays.toString(strings));//[aa, bb, cc, dd]
}
```

- 非包装类的自定义类

  就需要重写compareTo()方法

  ```java
  /**
   * Created by KingsLanding on 2022/6/21 17:24
   */
  public class Book implements Comparable {
      private String name;
      private int price;
  
      public Book() {
      }
  
      public Book(String name, int price) {
          this.name = name;
          this.price = price;
      }
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public int getPrice() {
          return price;
      }
  
      public void setPrice(int price) {
          this.price = price;
      }
  
      @Override
      public String toString() {
          return "book{" +
                  "name='" + name + '\'' +
                  ", price=" + price +
                  '}';
      }
  
      
      @Override
      public int compareTo(Object o) {
          //instanceof关键字：判断左边对象是否是右边类的实例
          if(o instanceof Book){
              Book book= (Book)o;
              if (this.price > book.price) {
                  return 1;
              } else if (this.price < book.price) {
                  return -1;
              }else {
  
                  return this.name.compareTo(book.name);
              }
          }
  //        return 0;
          throw new RuntimeException("数据不匹配");
      }
  }
  ```

  ```java
  /**
   * 自然排序 java.lang.Comparable
   */
  @Test
  public void test1(){
      Book[] books = new Book[4];
      books[0]=new Book("三体",50);
      books[1]=new Book("全频段断阻式干扰",40);
      books[2]=new Book("诗云",30);
      books[3]=new Book("aaaa",30);
  
      Arrays.sort(books);
      System.out.println(Arrays.toString(books));//[book{name='aaaa', price=30}, book{name='bbbb', price=30}, book{name='cccc', price=40}, book{name='dddd', price=50}]
  }
  ```

- Comparator ***定制排序 ：java.util.Comparator***
    1.背景

  - 当元素的类型没有实现java.lang.Comparable接口而又不方便修改代码

  - 或者实现了，但是该接口的排序规则不适合当前的操作，就可以使用Comparator的对象来排

  2.重写compare(Object o1,Object o2)方法，比较o1和o2的大小

    	- 如果方法返回正整数，则表示o1>o2;  
    	- 如果返回0，表示相等    
    	- 如果返回负整数，表示o1<o2
  
- Comparable与Comparator使用对比

  - Comparable接口方式一旦确定，就保证Comparable接口实现类的对象在任何位置都可以比较大小

  - Comparator接口属于临时性的比较

```java
    @Test
    public void test2(){
        Book[] books = new Book[4];
        books[0]=new Book("三体",50);
        books[1]=new Book("全频段断阻式干扰",40);
        books[2]=new Book("诗云",30);
        books[3]=new Book("aaaa",30);

        Arrays.sort(books, new Comparator<Book>() {
            //指明商品比较大小的方式，按照产品名称从低到高排序，再按照价格从高到低
            @Override
            public int compare(Book o1, Book o2) {
                if(o1.getName().equals(o2.getName())){
                    return -Integer.compare(o1.getPrice(),o2.getPrice());
                }else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
        System.out.println(Arrays.toString(books));
    }
```

# 注解

-  注解的使用 Annotation	 jdk5.0新特性

- 注解（Annotation）其实就是代码中的特殊标记，这些标记可以在编译，类加载，运行时被读取
  程序员可以在不改变原有逻辑的情况下，在源文件中嵌入一些补充信息

- JavaSE中，注解的使用主要是：标记过时的功能，忽略警告等，JavaEE中注解较为重要
   用于配置程序的任何切面，代替JavaEE(JavaWeb)中的xml文件配置问题

* **框架 = 注解 + 反射机制 + 设计模式**


##  如何自定义注解
- 参照@SupperssWarnings定义
  - 注解声明：@interface
  - 内部定义成员，同城使用value表示
  - 可以指定成员的默认值，使用default定义
  - 自定义注解没有成员，表明是一个标识作用
  - 如果注解有成员，使用时必须指明成员值

 ``` java
public @interface MyAnnotation {
    String value() default "注解";
}
 ```

- **自定义注解必须配上注解的信息处理流程（使用反射）才有意义**
- 自定义注解通过都会指明两个元注解：`@Retention`、`@Target`

## 元注解

SOURCE：反编译时看不到注解信息

CLASS：反编译时注解信息同时反编译

简单来说就是注解信息会不会生成class文件

```java
/* 
 *	jdk 提供的四种元注解
 *      元注解：对现有的注解进行解释说明的注解
 *      Retention:指定所修饰的Annotation的生命周期：SOURCE\CLASS(默认行为）\RUNTIME
 *                  只有声明为RUNTIME生命周期的注解，才能通过反射获取
 *      Target：用于指定被修饰的Annotation能用于修饰哪些程序元素
 *
 *      Documented:表示修饰的注解在被javadoc解析是，将被保留
 *      Inherited:被他修饰的Annotation将具有继承行
 */

@Repeatable(MyAnnotations.class)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE,TYPE_PARAMETER,TYPE_USE})
public @interface MyAnnotation {
    String value() default "注解";
}
```

## 注解新特性	jdk8.0 

**可重复注解**

```java
   /*      jdk8.0 注解新特性:
    *
    *        可重复注解
    *       在MyAnnotation上声明 @Repeatable，成员值为 MyAnnotations.class
    *       MyAnnotation的Target和Retention等元注解与MyAnnotations相同。
    *
    *       类型注解
    *       ElementType.TYPE_PARAMETER 表示该注解能写在类型变量的声明语句中（如：泛型声明。）
    *      ElementType.TYPE_USE 表示该注解能写在使用类型的任何语句中。
    */

    public class Annotation {

        @MyAnnotation(value = "注解")
        @MyAnnotation(value = "多重注解")
        @Test
        public void test(){
            System.out.println("注解测试");
        }
    }
```

***MyAnnotation***

```java
@Repeatable(MyAnnotations.class)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE,TYPE_PARAMETER,TYPE_USE})
public @interface MyAnnotation {
    String value() default "注解";
}
```

***MyAnnotations***

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE,TYPE_PARAMETER,TYPE_USE})
public @interface MyAnnotations {
    MyAnnotation[] value();
}
```

**类型注解**

```

```



# 枚举类

 ***一、枚举类的使用***
1.枚举类：类的对象只有确定的，有限个。称此类为枚举类
2.当需要定义一组常量时，建议使用枚举类
3.如果枚举类中只有一个对象，则可以作为单例模式的实现方式

***二、枚举类的定义方式***
1.jdk5.0之前，自定义枚举类
**2.jdk5.0之后，使用enum关键字定义枚举类**

***三、Enum类的常用方法***

- values()方法，返回枚举类的对象数组，该方法可以很方便的遍历所有的枚举值
- valueOf(String str):可以把一个字符串转为对应的枚举类对象，要求字符串必须是枚举类对像
- toString()：返回当前枚举类对象常量的名称

***四、使用enum关键字定义的枚举类实现接口的情况***

- 情况一：实现接口，在enum类中实现抽象方法
- 情况二：让枚举类的对象分别实现接口中的方法


```java
//定义一个接口
interface IoTest{
     void show();
}
enum  Season1 implements IoTest{

    //1.提供当前枚举类的多个对象：public static final的
    SPRING("春","春暖花开"){
        @Override
        public void show() {
            System.out.println("春天");
        }
    },
    SUMMER("夏","艳阳高照"){
        @Override
        public void show() {
            System.out.println("夏天");
        }
    },
    AUTUMN("秋","秋风瑟瑟"){
        @Override
        public void show() {
            System.out.println("秋天");
        }
    },
    WINTER("冬","雪花漫天"){
        @Override
        public void show() {
            System.out.println("冬天");
        }
    };

    //2.声明Season对象的属性：private final修饰；表示其有限，确定
    private final String seasonName;
    private final String seasonDesc;

    //3.私有化类的构造器，并给对象属性赋值
    private Season1(String seasonName,String seasonDesc){
        this.seasonName=seasonName;
        this.seasonDesc=seasonDesc;
    }


    //4.其他诉求：获取枚举类对象的属性
    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }


//    //4.诉求：toString方法
//    @Override
//    public String toString() {
//        return "Season1{" +
//                "seasonName='" + seasonName + '\'' +
//                ", seasonDesc='" + seasonDesc + '\'' +
//                '}';
//    }
    
//    @Override
//    public void show() {
//        System.out.println("这种方式，无论是哪个枚举对象都只能调用此方法");
//    }
//
```

**枚举类的使用**

``` java
public static void main(String[] args) {
    //toString()
    Season1 summer = Season1.SUMMER;
    System.out.println(summer);

    System.out.println(Season1.class.getSuperclass());
    //values()方法，返回枚举类的对象数组，该方法可以很方便的遍历所有的枚举值
    Season1[] values = Season1.values();
    for (int i=0;i<values.length;i++){
        System.out.println(values[i]);
    }

    //valueOf(String str):根据提供的objName返回对象名是objName的对象
    //没有这个枚举值，则抛异常
    Season1 summer1 = Season1.valueOf("SUMMER");
    System.out.println(summer1);

    summer1.show();//夏天
}
```

运行结果

<img src="D:\Desktop\java\枚举类.png" style="zoom:60%;" />

# 集合

**一、集合框架**
	集合、数组都是对多个数据进行存储操作的结构，简称Java容器
	说明：这里的存储，主要指的是内存层面的存储，不涉及到持久化的存储（.txt,.jpg.数据库等）

## 集合框架

## Collection接口

Collection接口：单列集合，用来存储一个一个的对象

​      ----List接口：存储有序的、可重复的数据。 --->“动态”数组
​         	 ----ArrayList、LinkedList、Vector

​      ----Set接口：存储无序的、不可重复的数据
​         	 ----HashSet、LinkedHashSet、TreeSet

Map接口：双列集合，用来存储一对（key - value)一对的数据     --->y=f(x)
         	 ----HashMap、LinkedMap、TreeMap、Hashtable、Properties

**集合的初始化和基本使用	非泛型**

```java
public class List {
    @Test
    public void test(){
        //初始化
        Collection coll=new ArrayList();
        //add(Object e)：将元素e添加到集合coll中
        coll.add("zmj");
        coll.add(123);
        coll.add(new Date());

        //size():获取添加的元素的个数
        System.out.println(coll.size());
        System.out.println(coll);

        //将集合中的元素添加到里另一个集合中
        Collection list = new ArrayList();
        list.add("ZMJ");
        list.add(321);
        //addAll(Collection list)：将coll集合中的元素添加到当前list的集合中
        list.addAll(coll);

        System.out.println(coll);//[zmj, 123, Wed Jun 22 18:28:58 CST 2022]
        System.out.println(list);//[ZMJ, 321, zmj, 123, Wed Jun 22 18:28:58 CST 2022]

        //clear()：清空集合元素
        coll.clear();

        //isEmpty()：判断当前集合是否为空
        if(coll.isEmpty()){
            System.out.println("该集合为空");
        }else {
            System.out.println(coll);
        }
    }
}
```

## 集合元素的遍历

**集合元素的遍历操作：使用迭代器`Iterator`接口**


- 1.内部方法：hasNext()和  next()
- 2.集合对象每次调用iterator()方法都得到一个全新的迭代器对象
- 3.默认游标都在集合的第一个元素之前

```java
    /**
     * 集合元素的遍历操作：使用迭代器Iterator接口
     * 1.内部方法：hasNext()和  next()
     * 2.集合对象每次调用iterator()方法都得到一个全新的迭代器对象
     * 3.默认游标都在集合的第一个元素之前
     * 4.迭代器内部定义了remove()，可以在遍历的时候，删除集合中的元素，此方法不同于集合中的remove()方法
     */
    @Test
    public void test(){
        Collection coll = new ArrayList();
        coll.add("124");
        coll.add(123);
        coll.add(new Person("zmj",22));
        coll.add(new String("zmj2"));
        coll.add(false);

        //迭代器：iterator本身不是容器,只是一个迭代器对象
        java.util.Iterator iterator = coll.iterator();

//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());
//        System.out.println(iterator.next());

        //推荐使用方法
        //hasNext()：判断是否还有下一个元素
        while (iterator.hasNext()) {
            //next():
            //1.指针下移
            //2.将下移以后的集合位置上的元素返回
            System.out.println(iterator.next());
        }
    }
```


- 4.迭代器内部定义了remove()，可以在遍历的时候，删除集合中的元素，此方法不同于集合中的remove()方法

```java
    //测试Iterator中的remove()
    //未调用next()或者上一次调用next()方法后已经调用了remove方法，在调用remove将报异常IllegalStateException
    //相当于指针都还没有指向值，删什么？
    @Test
    public void test1(){
        Collection coll = new ArrayList();
        coll.add("124");
        coll.add(123);
        coll.add(new Person("zmj",22));
        coll.add(new String("zmj2"));
        coll.add(false);

        java.util.Iterator iterator = coll.iterator();

        while (iterator.hasNext()){
//            iterator.remove();//异常操作
            Object next = iterator.next();
            if("124".equals(next)){
                iterator.remove();
            }
        }
        System.out.println(coll);

        //一个迭代器处理一个流程
        java.util.Iterator iterator1 = coll.iterator();
        //使用新的迭代器iterator1执行遍历
        while (iterator1.hasNext()){
            System.out.println(iterator.next());
        }
    }
```

## 增强型for循环	foreach

- for(集合元素的类型   局部变量  ： 集合对象）
- 底层内部任然调用的迭代器

```java
/**
 * Created by KingsLanding on 2022/6/24 16:22
 */
public class ForEach {

    @Test
    public void test(){
        Collection coll = new ArrayList();
        coll.add("124");
        coll.add(123);
        coll.add(new Person("zmj",22));
        coll.add(new String("zmj2"));
        coll.add(false);

        //增强型for循环
        //for(集合元素的类型   局部变量  ： 集合对象）
        //底层内部任然调用的迭代器
        for(Object obj:coll){
            System.out.println(obj);
        }
    }

    @Test
    public void test2(){
        int[] ints = {1, 2, 3, 4, 5};
        for (int i:ints){
            System.out.println(i);
        }
    }

    @Test
    public void test3(){
        String[] strings = {"AA", "AA"};

//        for(int i=0;i<strings.length;i++){
//            strings[i]="BB";
//            System.out.println(strings[i]);
//        }

        //该方法将查询结果存放到了str中，所以赋值也是赋值给了str而非strings中
        for(String str : strings){
            str="BB";
            System.out.println(str);//BB
        }

        for(int i=0;i<strings.length;i++){
            System.out.println(strings[i]);//AA
        }
    }

}
```

## List接口

Collection接口：单列集合，用来存储一个一个的对象
      ----**List接口：存储有序的、可重复的数据。 --->“动态”数组,替换原有的数组**
          	----ArrayList：作为List接口的主要实现类；线程不安全，效率高；底层使用Object[] elementData存储（单链表）
         	 ----LinkedList：对于频繁的插入、删除操作，使用此类效率比ArrayList高，底层使用双向链表存储（数据结构）
          	----Vector：作为List接口的古早实现类：线程安全，效率低；底层使用Object[] elementData存储

---

 * `ArrayList`、`LinkedList`、`Vector`三者异同

   - 同：三个类都是实现了List接口，存储数据特点相同：有序可重复

   - 异：ArrayList：作为List接口的主要实现类；线程不安全，效率高；底层使用Object[] elementData存储

     ​		LinkedList：对于频繁的插入、删除操作，使用此类效率比ArrayList高，底层使用双向链表存储（数据结构）

     ​		Vector：作为List接口的古早实现类：线程安全，效率低；底层使用Object[] elementData存储

```java
/**
 * Created by KingsLanding on 2022/6/24 18:45
 *
 *  三、集合框架
 *  Collection接口：单列集合，用来存储一个一个的对象
 *      ----List接口：存储有序的、可重复的数据。 --->“动态”数组,替换原有的数组
 *          ----ArrayList：作为List接口的主要实现类；线程不安全，效率高；底层使用Object[] elementData存储（单链表）
 *          ----LinkedList：对于频繁的插入、删除操作，使用此类效率比ArrayList高，底层使用双向链表存储（数据结构）
 *          ----Vector：作为List接口的古早实现类：线程安全，效率低；底层使用Object[] elementData存储
 *
 *  面试题：
 *  ArrayList、LinkedList、Vector三者异同
 *  同：三个类都是实现了List接口，存储数据特点相同：有序可重复
 *  异： ----ArrayList：作为List接口的主要实现类；线程不安全，效率高；底层使用Object[] elementData存储
 *       ----LinkedList：对于频繁的插入、删除操作，使用此类效率比ArrayList高，底层使用双向链表存储（数据结构）
 *       ----Vector：作为List接口的古早实现类：线程安全，效率低；底层使用Object[] elementData存储
 *
 */
```

### ArrayList源码分析

- ***1、jdk7:(饿汉式）***
  ArrayList list=new ArrayList();//初始化创建了长度为10的Object[] elementData存储（单链表），
  list.add(123);// elementData[0] = new Integer(123);
   ...
  list.add(Xx);//如果此次的添加导致底层elementData数组容量不够，则要进行扩容，默认情况下，扩容为原来容量的1.5倍，即增加原容量的一半。同时将原数组里的数据复制到新的数组中

 开发中建议使用带参构造器：ArrayList list = new ArrayList(int capacity)

- ***2、jdk8（懒汉式）***
  ArrayList list=new ArrayList();//底层Object[] elementData存储（单链表），初始化化为｛｝，即没有创建初始长度

  list.add(123);//**第一次调用add()时，底层才创建长度为10的数组，并将数据添加到elementData中**
...
  list.add(Xx);//如果此次的添加导致底层elementData数组容量不够，则要进行扩容，默认情况下，扩容为原来容量的1.5倍，即增加原容量的一半。同时将原数组里的数据复制到新的数组中

  **jdk8延迟了数组的创建，节省了内存**

#### ArrayList方法测试

```java
public class aListTest {

    /**
     * ArrayList方法测试
     *
     * 常用方法：
     * 增删改查
     * add(Object obj)、remove(int index)\remove(Object obj)、
     * set(int index,Object ele)、get(int index)
     * 插：add(int index,Object ele)
     * 长度：size()
     * 遍历：(1)foreach增强
     *     (2)Iterator迭代器
     *     (3)普通循环
     */
    @Test
    public  void test2(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(123);
        list.add("124");
        list.add(new Person("zmj",22));
        list.add(new String("zmj2"));
        list.add(false);

        //遍历方式：迭代器
        java.util.Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        //遍历方式：foreach
        System.out.println("*************");
        for(Object obj:list){
            System.out.println(obj);
        }

        //遍历方式：普通；size()返回此列表中的元素数。get()返回此列表中指定位置的元素
        System.out.println("************");

        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }

    }
    @Test
    public void test(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(123);
        list.add("124");
        list.add(new Person("zmj",22));
        list.add(new String("zmj2"));
        list.add(false);

        System.out.println(list);

        List<Integer> integers = Arrays.asList(1, 2, 3);

        list.addAll(integers);//将integer中的所有元素复制给list集合

        System.out.println(list);

        System.out.println(list.size());//返回此列表中的元素数

        //返回指定的fromIndex （含）和toIndex之间的列表部分的视图;返回列表中的非结构性更改
        System.out.println(list.subList(2, 4));//[Person{name='zmj', age=22}, zmj2]

        //替换指定位置的元素
        list.set(3, new String("zmj"));
        System.out.println(list);

    }

    /**
     * 区分List中remove(int index)和remove（Object obj)
     */
    @Test
    public void test3(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(123);
        list.add("124");
        list.add(new Person("zmj",22));
        list.add(new String("zmj2"));
        list.add(false);

        updateList(list);
        System.out.println(list);//[124, Person{name='zmj', age=22}, zmj2, false]
    }
    private void updateList(List list){
        //默认是remove(int index),移除第0位的数据
        //list.remove(0);
        //指定remove(Object obj)，移除数据为obj的
        list.remove(new Integer(123));
    }

}
```

### LinkedList的源码分析

```java
*      //初始化：内部声明了Node类型的first和last属性，默认值为null
*      LinkedList linkedList = new LinkedList();
*      linkedList.add(123);//将123封装到Node中，创建了Node对象
*
*    //其中Node定义位：体现了LinkedList的双向链表
*    private static class Node<E> {
*         E item;
*         Node<E> next;
*         Node<E> prev;
*
*         Node(Node<E> prev, E element, Node<E> next) {
*             this.item = element;
*             this.next = next;
*             this.prev = prev;
*      }
*     }
```

### Vector源码分析

jdk7和jdk8中Vector()构造器创建对象时，底层都创键了长度为10的数组扩容默认扩容为原来的 数组长度的2倍

## Set接口

Collection接口：单列集合，用来存储一个一个的对象
  	 ----**Set接口：存储无序的、不可重复的数据**
          	----HashSet:作为Set接口的主要实现类；线程不安全；可以存储null值
         	 ----LinkedHashSet:作为HashSet的子类；遍历其内部数据时，可以按照添加的顺序遍历
                  	在添加数据的同时，每个数据还维护了两个引用， 记录此数据前一个数据和后一个数据
                  	优点：对于频繁的遍历操作，LinkedHashSet效率高于HashSet
          	----TreeSet:可以按照添加对象的指定属性进行排序

---

***Set:存储无序的、不可重复的数据***

- 无序性：不等于随机性。存储的数据在底层数组中并非按照数组索引的顺序添加， 而是根据数据的哈希值决定的
- 不可重复性：尽量保证哈希值不同，如果哈希值相同，那就需要保证添加的元素按照equals()判断时，不能返回true,**即相同元素只能添加一个**

---

***添加元素的过程：以HashSet为例：***

```java
* 二、添加元素的过程：以HashSet为例：
*      我们向HashSet中添加元素a，首先调用元素a所在类的hashCode()方法，计算元素a的哈希值，
*      此哈希值接着通过某种算法计算出在HashSet底层数组中的存放位置（即：索引位置），判断
*      此数组此位置上是否已经有元素：
*          如果此位置上没有其他元素，则元素a添加成功。--->
*          如果此位置上有其他元素b(或以链表形式存在的多个元素），则比较元素a与元素b的hash值：
*              如果hash值不同，这元素a添加成功。--->
*              如果hash值相同，进而需要调用元素a所在类的equals()方法：
*                  equals()方法返回true,元素a添加失败
*                  equals()方法返回false,元素a添加成功。--->
*
*  对于添加成功的情况2和情况3而言：元素a 与已经存在指定索引位置上数据以链表的方式存在
*  jdk7：元素a放到数组中，指向原来的元素
*  jdk8：原来的元素在数组中，指向元素a
*  (七上八下）
```

### HashSet测试

``` java
/**
 * HashSet底层：数组+链表的结构
 */
@Test
public void test(){
    Set<Object> set = new HashSet<>();
    set.add(123);
    set.add(123);
    set.add(new String("zmj"));
    set.add(new Person("zmj2",22));
    set.add(new Person("zmj2",22));//在Person类中重写equals()方法，以及 hashCode()方法，才能判断其哈希值
    set.add("124");
    System.out.println(set);//[Person{name='zmj2', age=22}, 124, zmj, 123]
}
```

### LinkedHashSet的使用测试

```java
/**
 LinkedHashSet的使用测试
 LinkedHashSet作为HashSet的子类，在添加数据的同时，每个数据还维护了两个引用，
        记录此数据前一个数据和后一个数据
 优点：对于频繁的遍历操作，LinkedHashSet效率高于HashSet
 */
@Test
public void test2(){
    LinkedHashSet<Object> set = new LinkedHashSet<>();
    set.add(123);
    set.add(123);
    set.add(new String("zmj"));
    set.add(new Person("zmj2",22));
    set.add(new Person("zmj2",22));//在Person类中重写equals()方法，以及 hashCode()方法，才能判断其哈希值
    set.add("124");

    for(Object obj : set){
        System.out.println(obj);
    }
}
```

### TreeSet

**向TreeSet中添加的数据，要求是相同类型的对象。**

```java
/**
 1.向TreeSet中添加的数据，要求是相同类的对象。
 2.两种排序方式：自然排序（实现Comparable借口）和定制排序(在Person类中重写CompareTo(Object o)方法）
 3.自然排序中，比较两个对象是否相同的标准为：compareTo()返回0  不再是equals()
 */
@Test
public void test3(){
    TreeSet<Object> set = new TreeSet<>();

    //不能添加不同类型的对象
    set.add(123);
    set.add(124);
    //set.add("zmj");
    set.add(12);
    set.add(-14);
    System.out.println(set);
}

@Test
public void test4(){
    //定制排序:按照年龄从小到大排序
    Comparator com=new Comparator(){
        @Override
        public int compare(Object o1, Object o2) {
            if(o1 instanceof Person && o2 instanceof Person){
                Person p1 = (Person)o1;
                Person p2 = (Person)o2;
                return Integer.compare(p1.getAge(),p2.getAge());
            }else {
                throw new RuntimeException("输入数据不匹配");
            }
        }
    };
    TreeSet<Object> set = new TreeSet<>(com);
    set.add(new Person("zmj",22));
    set.add(new Person("lnx",21));
    set.add(new Person("sbh",22));
    set.add(new Person("sbh",23));


    /**
     *  自然排序:通过在Person类里重写compareTo(Object o)方法实现
     @Override
     public int compareTo(Object o) {
     if(o instanceof Person){
     Person person=(Person)o;
     int compare = -this.name.compareTo(person.name);
     if(compare != 0){
     return compare;
     }else {
     return Integer.compare(this.age,person.age);
     }
     }else {
     throw new RuntimeException("类型不匹配");
     }
     }
     */

    for(Object obj : set){
        System.out.println(obj);
    }
}
```

## Person类

``` java
package collection;

import java.util.Objects;

/**
 * Created by KingsLanding on 2022/6/24 13:48
 */
public class Person implements Comparable{
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        return name != null ? name.equals(person.name) : person.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }

    //TreeSet自然排序
    @Override
    public int compareTo(Object o) {
        if(o instanceof Person){
            Person person=(Person)o;
            int compare = -this.name.compareTo(person.name);
            if(compare != 0){
                return compare;
            }else {
                return Integer.compare(this.age,person.age);
            }
        }else {
            throw new RuntimeException("类型不匹配");
        }
    }
}
```

## map接口

**----Map:双列数据，存储key-value对的数据    --类似于：y=f(x)**
      ----`HashMap`:作为Map的主要实现类：线程不安全，效率高；可以存储null的`key`和`value`
          	----`LinkedHashMap`:保证在遍历map元素时，***可以按照添加的顺序实现遍历***
              	原理：在原有的HashMap底层结构基础上，添加了一对指针，指向前一个和后一个元素
              	对于频繁的遍历操作，此类执行效率高于HashMap
      ----`TreeMap`:保证添加的key-value对进行排序，实现排序遍历，此时考虑key的自然排序或定制排序
                  		底层使用红黑树
      ----`Hashtable`:古早实现类；线程安全的，效率低，不能存储null的key和value
          	----properties:常用来处理配置文件（数据库链接配置文件）。key和value都是String类型

HashMap的底层：数组+链表 （jdk7)
								数组+链表+红黑树 （jdk8)

**Map结构的理解**

     - **Map中的key:**无序的，不可重复的，***使用Set存储所有的key***    --->key所在的类要重写equals()和HashCode()方法
   -  **Map中的value:**无序的，可重复的，***使用Collection存储所有的value***  --->value所在的类要重写equals()方法
      一个键值对：`key-value`构成了一个`Entry`对象。
     -  **Map中的entry:**无序的，不可重复的，***使用Set存储所有的entry***

### HashMap的底层实现原理

``` java
* HashMap的底层实现原理。（以jdk7为例）
*      HashMap map = new HashMap();
*      在实例化后，底层创建了长度为16的一维数组Entry[] table
*      ...可能已经执行过多次put...
*      map.put(key1,value1);
*      首先，调用key1所在的类的hashCode()方法计算key的哈希值，此哈希值经过某种算法计算以后，得到在Entry数组中
*      的存放位置
*      如果此位置上的数据为空，此时key1-value1添加成功。    --->1
*      如果此位置上的存在数据，（意味着此位置上存在一个或多个数据（以链表形式存在），那么就需要比较key1
*      和已经存在的一个或多个数据的哈希值：
*          如果key1的哈希值与已经存在的数据的哈希值都不同，此时key1-value1添加成功 --->2
*          如果key1的哈希值与已经存在的某一个数据(key2-value2)的哈希值相同，则需要继续比较：
*              调用key1所在类的equals(key2)方法:
*                  如果equals()返回false:此时key1-value1添加成功 --->3
*                  如果equals()返回true:此时key1-value1中的value1将替换原有与key1相同的key2对应的value2
*
*     关于情况2和情况3：此时key1-value1和原来的数据以链表的方式存储
*     在不断的添加过程中，会涉及到扩容问题，当超出临界值（12）且要存放的位置非空时，扩容；默认的扩容方式：扩容为原来容量的2倍，并将原有的数据复制过来
*
*     jdk8 相较于jdk7在底层实现方面的不同之处：
*          1.new HashMap();初始化时底层没有创建一个长度为16的数组（懒汉式）
*          2.jdk8底层的数组是：Node[],而不是Entry[]
*          3.首次调用put()方法时，底层创建长度为16的数组
*          4.jdk7底层结构只有：数组+链表。
*            七上八下：jdk7中新的元素指向旧的元素。jdk8中旧的元素指向新的元素
*     jdk8底层结构：数组+链表+红黑树；当数组的某一个索引位置上的元素以链表的形式存在的数据个数 > 8
*     且当前数组长度 > 64时，此时索引位置上的所有数据改为使用红黑树存储（二叉树链表，查询遍历效率更高）
*
*      DEFAULT_ INITIAL_ CAPACITY : HashMap的默认容量，16
*      DEFAULT_ LOAD FACTOR: HashMap的默认加载因子: 0.75
*      threshold:扩容的临界值，=容量*填充因子: 16 * 0.75 => 12
*      TREEIFY_THRESHOLD: Bucket 中链表长度大于该默认值，转化为红黑树:8
*      MIN_TREEIFY_CAPACITY:桶中的Node 被树化时最小的hash表容量:64
*
```


### LinkedHashMap的底层实现原理（了解）
```java
*      源码：
*      static class Entry<K,V> extends HashMap.Node<K,V> {
*         Entry<K,V> before, after;//记录添加元素的先后顺序
*         Entry(int hash, K key, V value, Node<K,V> next) {
*             super(hash, key, value, next);
*         }
*     }
```

```java
*     总结：常用方法
 *     添加：put(Object key,Object value)
 *     删除：remove(Object key)\clear()
 *     修改：put(Object key,Object value)
 *     查询：get(Object key)
 *     长度：size()
 *     遍历：keySet()\values()\entrySet()
 */
public class MapTest {

    /**
        方法测试
        添加、删除、修改
        Object put(Object key,Object value):将指定key-value添加（或修改）当前map对象中
        void putAll(Map m):将m中的所有key-value对存放到当前的map中
        Object remove(Object key):移除指定key的key-value对，并返回value
        void clear():清空当前map中的所有数据
     */
    @Test
    public void test(){
        Map map = new HashMap();
        LinkedHashMap linkMap = new LinkedHashMap();

        //添加
        map.put("AA",123);
        map.put("BB",123);
        map.put(123,123);
        //修改
        map.put("AA",124);
        System.out.println(map);

        Map map1 =new HashMap();

        map1.put("CC",123);
        map1.put("DD",123);

        //将集合map1中的所有元素复制给map
        map.putAll(map1);
        System.out.println(map);

        //remove(Object key)
        Object value = map.remove("DD");
        System.out.println(value);

        System.out.println(map);

        //clear()
        map.clear();
        System.out.println(map);
    }

    public void test2(){
        /**
         方法测试：元素查询操作
         Object get(Object key):获取指定key对应的value
         boolean containsKey(Object key):是否包含指定的key
         boolean containsValue(Object key):是否包含指定的value
         int size():返回map中key-value对的个数
         boolean isEmpty():判断map是否为null
         boolean equals(Object obj):判断当前map和指定的集合对象是否相等
         */
        Map map = new HashMap();
        map.put("AA",123);
        map.put("BB",123);
        map.put(123,123);
        System.out.println(map);

        System.out.println(map.get("AA"));

        boolean bool = map.containsKey("BB");
        System.out.println(bool);

        boolean bool1 = map.containsValue("123");
        System.out.println(bool1);

        System.out.println(map.size());

        System.out.println(map.isEmpty());

        Map map1 =new HashMap();

        map1.put("CC",123);
        map1.put("DD",123);

        System.out.println(map.equals(map1));

    }

    @Test
    public void test3(){
        /**

         元视图的遍历操作
         Set keySet();返回所有key构成的Set集合
         Collection values();返回所有value构成的collection集合
         set entrySet();返回所有key-value对构成的Set集合
         */
        Map map = new HashMap();
        map.put("AA",123);
        map.put(22,125);
        map.put("BB",124);

        //遍历所有的key
        Set set = map.keySet();

        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("**********");
        //遍历所有的value
        Collection values = map.values();

        for(Object obj:values){
            System.out.println(obj);
        }

        //遍历所有的key-value
        //方式一、entrySet()
        Set entrySet = map.entrySet();
        Iterator iterator1 = entrySet.iterator();
        while (iterator1.hasNext()){
            //entrySet集合中的元素都是entry
            Object obj = iterator1.next();
            System.out.println(obj);
            //将entry强转为map,分别拿到key和value
            Map.Entry entry= (Map.Entry) obj;
            System.out.println(entry.getKey() + "===>" + entry.getValue());
        }

        //遍历所有的key-value
        //方式二、分别遍历key和value，然后显示
        Set set1 = map.keySet();
        Collection values1 = map.values();
        Iterator iterator2=set.iterator();
        Iterator iterator3=values.iterator();
        while (iterator2.hasNext()&&iterator3.hasNext()){
            System.out.println(iterator2.next() + "===>" + iterator3.next());
        }
    }
}
```
## Collections工具类

常用方法测试

```java
/**
 * Created by KingsLanding on 2022/6/28 17:57
 */
public class CollectionsTest {

    @Test
    public void test(){
        List list=new ArrayList();
        list.add(10);
        list.add(12);
        list.add(-33);
        list.add(0);

        System.out.println(list);
        //reverse(list)：反转List中的元素顺序
        Collections.reverse(list);
        System.out.println(list);
        //shuffle(list):对list集合元素进行随机排序
        Collections.shuffle(list);
        System.out.println(list);
        //sort(list,Comparator);根据元素的自然顺序对指定的list集合元素按升序排序
        Collections.sort(list);
        System.out.println(list);
        //swap(list,int ,int);将指定的list集合中的i处的元素和j处的元素进行交换
        Collections.swap(list,2,3);
        System.out.println(list);

        //copy(list dest,list src);将src中的内容复制到dest中

        /*
        源码中要求srcSize > dest.size()，否则报异常
        if (srcSize > dest.size())
            throw new IndexOutOfBoundsException("Source does not fit in dest");
         */
//        List dest = new ArrayList();
//        Collections.copy(dest,list);
        //正确方法
        List dest = Arrays.asList(new Object[list.size()]);
        System.out.println(dest.size());
        Collections.copy(dest,list);
        System.out.println(dest);

        /*
        Collections实现类中提供了多个synchronizedXxx()方法
        该方法可以使指定的集合包装成线程同步的集合，从而可以解决多线程并发访问集合是的线程安全问题
         */
        List list1 = Collections.synchronizedList(list);
        //返回的list1就是线程安全的list集合
    }
}
```

# 泛型

 **泛型的使用**

- 1.`jdk5.0`新增特性

- 2.在集合中使用泛型

        - 1、集合接口或集合类在jdk5.0时都修改为带泛型的结构    
        - 2、在实例化集合类时，指明具体的泛型类型 
     - 3、指明类型后，在集合类或接口中凡是定义类或接口时，内部结构（方法、构造器、属性）等使用泛型的位置都指定为实例化的泛型类型
             ***比如：add(E e) --->实例化后add(Integer e)***
        -  4、注意：泛型必须是类，不能是基本数据类型。需要用到基本数据类型的位置，使用包装类替换 
        - 5.如果实例化时，没有指明泛型的类型。默认类型为Object类型

  ## 在集合中不使用泛型

```java
    @Test
    //在集合中不使用泛型的情况
    public void test(){
        ArrayList list = new ArrayList();
        list.add(123);
        list.add(23);
        list.add(245);
        //问题一：类型不安全
        list.add("zmj");

        //遍历
        for(Object obj : list){
            //问题二：强转时，可能出现异常ClassCastException
            Integer integer = (Integer) obj;
            System.out.println(integer);
        }
    }
```

## 在集合中使用泛型

- 指明泛型后，非泛型指定类型直接会在编译时进行类型检查，保证数据的安全

```java
@Test
public void test1(){
    ArrayList<Integer> list = new ArrayList<>();
    list.add(123);
    list.add(23);
    list.add(245);
    //指明泛型后，非泛型指定类型直接会在编译时进行类型检查，保证数据的安全
    //list.add("zmj");

    //遍历
    for(Integer integer : list){
        System.out.println(integer);
    }
}

    @Test
    public void test3(){

        //在集合中使用泛型的情况：以HashMap为例
        HashMap<String, Integer> map = new HashMap<>();
        map.put("AA",123);
        map.put("CC",125);
        map.put("BB",124);

        //遍历所有的key
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("**********");
        //遍历所有的value
        Collection<Integer> values = map.values();

        for(Integer integer:values){
            System.out.println(integer);
        }

        //遍历所有的key-value
        //方式一、entrySet()
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator1 = entrySet.iterator();
        while (iterator1.hasNext()){
            //entrySet集合中的元素都是entry
            Map.Entry<String, Integer> entry = iterator1.next();

            System.out.println(entry.getKey() + "===>" + entry.getValue());
        }

//        //遍历所有的key-value
//        //方式二、分别遍历key和value，然后显示
//        Set set1 = map.keySet();
//        Collection values1 = map.values();
//        Iterator iterator2=set.iterator();
//        Iterator iterator3=values.iterator();
//        while (iterator2.hasNext()&&iterator3.hasNext()){
//            System.out.println(iterator2.next() + "===>" + iterator3.next());
//        }
//
    }

```

## 泛型类

- 静态方法中不能使用类的泛型
- 异常类不能使用泛型

```java
/**
 * Created by KingsLanding on 2022/6/29 15:03
 */
public class Order<T> {
    String orderName;
    int orderId;

    //类的内部结构就可以使用类的泛型
    T orderT;

    public Order() {
    }

    public Order(String orderName, int orderId, T orderT) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.orderT = orderT;
    }

    public T getOrderT() {
        return orderT;
    }

    public void setOrderT(T orderT) {
        this.orderT = orderT;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderName='" + orderName + '\'' +
                ", orderId=" + orderId +
                ", orderT=" + orderT +
                '}';
    }
    //静态方法中不能使用类的泛型
//    public static void show(T orderT){
//
//    }
    //异常类不能使用泛型

    //泛型方法：在方法中出现的泛型结构，泛型参数和类的泛型参数没有任何关系
    //泛型方法所属的类是不是泛型类都无所谓
    //泛型方法可以声明为静态的。原因：泛型参数是在调用方法是才确定的，并非在实例化时确定
    public <E> List<E> copyFromArrayToList(E[] arr){
        ArrayList<E> list = new ArrayList<>();
        for(E e : arr){
            list.add(e);
        }
        return list;
    }
    
}
```

### 泛型类的实例化

```java
public void test(){
    //如果定义了泛型类，实例化没有指明类的泛型，则默认此泛型类型为Object类型
    //要求：如果定义了类是带泛型的，建议在实例化时要指明类的泛型
    Order order = new Order();
    order.setOrderT(12);
    order.setOrderT("zmj");

    //指明实例化时类的泛型
    Order<String> order1 = new Order<String>("zmj",12,"ZMJ");
    
  //  Order<String> order1 = new Order<String>("zmj",12,20);//指定了泛型，这里就不能设置为除String之外的数据了
}
```

## 泛型方法

```java
//泛型方法：在方法中出现的泛型结构，泛型参数和类的泛型参数没有任何关系
//泛型方法所属的类是不是泛型类都无所谓
//泛型方法可以声明为静态的。原因：泛型参数是在调用方法是才确定的，并非在实例化时确定
public <E> List<E> copyFromArrayToList(E[] arr){
    ArrayList<E> list = new ArrayList<>();
    for(E e : arr){
        list.add(e);
    }
    return list;
}
```
### 方法调用

```java
@Test
public void test2(){
    Order<String> order = new Order<>();
    Integer[] arr = new Integer[]{1,2,3,4};
    //泛型方法在调用是，指明泛型参数的类型
    List<Integer> list = order.copyFromArrayToList(arr);
    System.out.println(list);
}
```

## 泛型在继承中的体现

### 非泛型类继承泛型类

```java
public class SubOrder extends Order<Integer>{//SubOrder不是泛型类
    
}
```
### 泛型类之间的继承

```java
/**
 * Created by KingsLanding on 2022/6/29 15:14
 */
public class SubOrder1<T> extends Order<T> {//SubOrder1<T>仍然是泛型类

}
```

实例化

```java
public void test1(){
    SubOrder subOrder = new SubOrder();
    //由于子类在继承带泛型的父类时，指明了泛型类型，则实例化子类对象时，不需要指明泛型，只能遵循父类的要求
    subOrder.setOrderT(12);

    SubOrder1<String> stringSubOrder1 = new SubOrder1<>();
    stringSubOrder1.setOrderT("zmj");
}
```

-  虽然类A是B的父类，但是G<A> 和G<B>二者不具备子父类关系，二者是并列关系
- 补充：**类A是类B父类，A<G> 是B<G>的父类**

```java
    /*
    1.泛型在继承方面的体现

        虽然类A是B的父类，但是G<A> 和G<B>二者不具备子父类关系，二者是并列关系

        补充：类A是类B父类，A<G> 是B<G>的父类
     */

    public void test(){
        Object obj=null;
        String str=null;
        obj=str;

        //没有子父类关系，编译不通过
//        Date date=null;
//        str=date;

        Object[] arr1=null;
        String[] arr2=null;
        arr1=arr2;

        List<Object> list=null;
        List<String> list1 =null;
        //此时的list和list1的类型不具有子父类关系，编译不通过
//        list= list1;

    }

    public void test2(){

        List<String> list1=null;
        ArrayList<String> list2=null;
		//此时的list1和list1的类型具有子父类关系，编译通过
        list1 = list2;
    }
```

## 通配符

- **通配符：？**

通配符的使用

```java
    @Test
    public void test3(){
        /*
        2.通配符的使用
          通配符：？
         */
        List<Object> list=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        list.add(123);
        list1.add("zmj");

        List<?> list2=null;
        list2=list;
        list2=list1;

        //添加：对于List<?>不能向其内部添加数据
        //除了添加null之外
//        list2.add("除了null都添加不了");
        list2.add(null);
        //获取：允许读取数据，读取的数据类型为Object
        Object o = list2.get(0);
        System.out.println(o);

        //通配符使用：同一个方法可以传不同类型的集合
        test4(list);
        test4(list1);
    }

    public void test4(List<?> list){

        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()){
            Object obj = iterator.next();
            System.out.println(obj);
        }
    }
```

有限制条件的通配符

- ？extends A:
  G<? extends A>  可以作为G<A>和G<B>的父类，其中B是A的子类 <=
  G<? super A>    可以作为G<A>和G<B>的父类，其中B是A的父类 >=

父类

```java
/**
 * Created by KingsLanding on 2022/6/29 19:42
 */
public class Person {
}
```

子类

```java
/**
 * Created by KingsLanding on 2022/6/29 19:42
 */
public class Student extends Person {
}
```

```java
	/*
    3.有限制条件的通配符的使用
        ？extends A:
        G<? extends A>  可以作为G<A>和G<B>的父类，其中B是A的子类 <=
        G<? super A>    可以作为G<A>和G<B>的父类，其中B是A的父类 >=
     */
    public void test5(){

       List<Student> student = new ArrayList<>();
       List<Person> person = new ArrayList<>();
       List<Object> obj = new ArrayList<>();


       List<? extends Person> list=null;
       List<? super Person> list1=null;

       list = student;
       list = person;
       //编译不通过，<=
//       list = obj;

        //编译不通过，>=
//        list1=student;
        list1=person;
        list1=obj;

        //读取数据
        Person person1 = list.get(0);
        //编译不通过
//        Student stu = list.get(0);

        Object object = list1.get(0);
        //编译不通过
//        Person pson = list1.get(0);

        //写入数据
        //编译不通过
//        list.add(new Student());
//        list.add(new Person());

        //编译通过
        list1.add(new Person());
        list1.add(new Student());
    }
```

# File类

- File类的使用
  - 1.File类的一个对象，代表一个文件或一个目录（文件夹）
  - 2.File类声明在java.io下
  - 3.File类中涉及到关于文件或文件目录的创建、删除、重命名、修改时间、文件大小等方法。并未涉及到写入或读取文件内容的操作，如果需要读取或写入文件内容，必须使用IO流
  - 4.后续File类的对象常会作为参数传递到流的构造器中，指明读取或写入的终点

## 1.如何创建File类的实例
​    File(String filePath)
​    File(String parentPath,String childPath)
​    File(File parentFile,String childPath)

- IDEA中:
     如果使用`JUnit`中的单元测试方法测试，相对路径即为当前`Module`下。
     如果使用`main`测试，相对路径即为当前的`Project`下。
-  Eclipse中:
     不管使用单元测试方法还是使用`main`测试，相对路径都是当前的`Project`下。

***路径分隔符：Windows:\\\;unix:/***

```java
public void test(){
    //构造器1
    File file = new File("D:\\Pictures");
    System.out.println(file);

    //构造器2
    File file1 = new File("D:\\Pictures", "zmj.jpg");
    System.out.println(file1);

    //构造器3
    File file2 = new File(file, "zmj.jpg");
    System.out.println(file2);

}
```

## 基本方法

```java
/*
    public String getAbsolutePath(): 获取绝对路径
    public String getPath() :获取路径
    public String getName() :获取名称
    public String getParent(): 获取上层文件目录路径。若无，返回null
    public Long length() :获取文件长度(即:字节数) .不能获取目录的长度。
    public Long lastModified() :获取最后一次的修改时间，毫秒值
    下面两个方法使用于文件目录
    public String[] List() :获取指定目录下的所有文件或者文件目录的名称数组
    public File[] listFiles() :获取指定目录下的所有文件或者文件目录的File数组
 */
@Test
public void test1(){
    File file1 = new File("hello");
    File file2 = new File("E:\\IDEA 2018.2.4\\workspace\\2022jun\\src\\fileIoTest\\hello");

    //getAbsolutePath(): 获取绝对路径
    System.out.println(file1.getAbsoluteFile());
    //getPath() :获取路径
    System.out.println(file1.getPath());
    //getName() :获取名称
    System.out.println(file1.getName());
    //getParent(): 获取上层文件目录路径。若无，返回null
    System.out.println(file1.getParent());
    //Long length() :获取文件长度(即:字节数) .不能获取目录的长度
    System.out.println(file1.length());

    System.out.println("*********");

    //getAbsolutePath(): 获取绝对路径
    System.out.println(file2.getAbsoluteFile());
    //getPath() :获取路径
    System.out.println(file2.getPath());
    //getName() :获取名称
    System.out.println(file2.getName());
    //getParent(): 获取上层文件目录路径。若无，返回null
    System.out.println(file2.getParent());
    //Long length() :获取文件长度(即:字节数) .不能获取目录的长度
    System.out.println(file2.length());
    //Long lastModified() :获取最后一次的修改时间，毫秒值
    System.out.println(file2.lastModified());
    System.out.println(new Date(file2.lastModified()));
}

@Test
public void test2(){
    /*
    public boolean renameTo(File dest):把文件重命名为指定的文件路径
        file1.renameTo(file2)
        返回true前提，需要在file1在硬盘中存在，且file2不能事先存在
     */
    File file1 = new File("hello.txt");
    File file2 = new File("E:\\IDEA 2018.2.4\\workspace\\2022jun\\src\\hi.txt");

    boolean b = file2.renameTo(file1);
    System.out.println(b);

}

@Test
public void test3(){
    /*
    //String[] List() :获取指定目录下的所有文件或者文件目录的名称数组
     */
    File file2 = new File("E:\\IDEA 2018.2.4\\workspace\\2022jun\\src");

    String[] list = file2.list();
    for (String s:list){
        System.out.println(s);
    }

    /*
    File[] listFiles() :获取指定目录下的所有文件或者文件目录的File数组
     */
    File[] files = file2.listFiles();
    System.out.println(files);
    for (File f:files){
        System.out.println(f);
    }

}

/*
public boolean isDirectory(): 判断是否是文件目录
public boolean isFile() :判断是否是文件
public boolean exists() :判断是否存在
public boolean canRead() :判断是否可读
public boolean canWrite() :判断是否可写
public boolean isHidden() :判断是否隐臧
 */
@Test
public void test4(){
    File file2 = new File("E:\\IDEA 2018.2.4\\workspace\\2022jun\\src");
    System.out.println(file2.isDirectory());//true
    System.out.println(file2.isFile());//false
    System.out.println(file2.exists());//true
    System.out.println(file2.canRead());//true
    System.out.println(file2.canWrite());//true
    System.out.println(file2.isHidden());//false
}

/*
创建硬盘中对应的又件或文件目录
public boolean createNewFile() :创建文件。若文件存在，则不创建，返回false
public boolean mkdir() :创建文件目录。如果此文件目录存在，就不创建了。如果此文件目录的上层目录不存在，也不创建
public boolean mkdirs() :创建文件目录。如果上层文件目录不存在，一并创建
删除磁盘中的文件或文件目录
public boolean delete():删除文件或者文件夹
删除注意事项:
Java中的删除不走回收站。
 */

@Test
public void test5() throws Exception{
    //createNewFile() :创建文件。若文件存在，则不创建，返回false
    File file = new File("zmj.txt");
    boolean newFile = file.createNewFile();
    System.out.println(newFile);

    //public boolean mkdir() :创建文件目录。如果此文件目录存在，就不创建了。
                             // 如果此文件目录的上层目录不存在，也不创建
    File file1 = new File("d:\\Desktop\\io\\io");
    boolean mkdir = file1.mkdir();
    System.out.println(mkdir);
    //public boolean mkdirs() :创建文件目录。如果上层文件目录不存在，一并创建
    File file2 = new File("d:\\Desktop\\io\\io");
    boolean mkdirs = file2.mkdirs();
    System.out.println(mkdirs);
}

/*

    1.利用File构造器，new 一个文件目录file
    1)在其中创建多个文件和目录
    2)编写方法，实现删除fle中指定文件的操作

    2.判断指定目录下是否有后缀名为.jpg的文件，如果有，就输出该文件名称

    3.遍历指定目录所有文件名称，包括子文件目录中的文件。
    拓展1:并计算指定目录占用空间的大小
    拓展2:删除指定文件目录及其下的所有文件
 */

@Test
public void test6(){
    // 2.判断指定目录下是否有后缀名为.jpg的文件，如果有，就输出该文件名称
    File file = new File("E:\\IDEA 2018.2.4\\workspace\\2022jun\\src\\fileIoTest");
    String[] list = file.list();
    for (int i=0;i<list.length;i++){
        if(list[i].endsWith(".jpg")){
            System.out.println("存在，该文件为："+list[i]);
        }
    }
}
```

# IO流

## 一、流的分类:
- 1.操作数据单位:***字节流、字符流***
- 2.数据的流向:***输入流、输出流***
- 3.流的角色: ***节点流、处理流***

## 二、流的体系结构

```
抽象基类       	 节点流(或文件流)                                        缓冲流(处理流的一种)
InputStream     FileInputStream ---> (read(byte[] buffer))    <---   BufferedInputStream
OutputStream    FileOutputStream --->(write(byte[] buffer,0,len))<---BufferedOutputStream
Reader          FileReader     --->  (read(char[] chars))      <---  BufferedReader
Writer          FileWriter    --->   (write(char[] chars,0,len) <--- BufferedWriter
```

## 节点流：字符流

**适用于文本文件的读写(.txt .java .c)**

`FileReader`
`FileWriter`

### read()

---

- 从该输入流读取一个字节的数据,返回读入的一个字符，如果到达文件末尾，返回-1
- 异常处理：为保证资源一定可以执行关闭操作。需要使用try-catch-finally
- 读入的文件一定要存在，否则就会会FileNotFoundException异常


```java
   @Test
    public void test() {
        /*
            read():从该输入流读取一个字节的数据,返回读入的一个字符，如果到达文件末尾，返回-1
            异常处理：为保证资源一定可以执行关闭操作。需要使用try-catch-finally
            读入的文件一定要存在，否则就会会FileNotFoundException异常
         */

        FileReader fs = null;
        try {
            //1.File类的实例化
            File file = new File("zmj.txt");
            System.out.println(file.getAbsolutePath());
            //2.FileReader流的实例化
            fs = new FileReader(file);

            //3.读入操作
            int read = fs.read();
            while (read != -1){
                System.out.print((char)read);
                read = fs.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //4、流的关闭操作
                if(fs != null)
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

### read(char[] cbf)：更高效

```java
	@Test
    public void test2() {
        FileReader fr = null;
        try {
            File file = new File("zmj.txt");
            fr = new FileReader(file);

            //read(char[] cbf);返回每次读入cbf数组中的字符个数。如果达到文件末尾，返回-1
            char[] chars = new char[3];
            int read;
            while ( (read= fr.read(chars))!=-1){
                //方式一
//                for (int i=0;i<read;i++){
//                    System.out.println(chars[i]);
//                }
            //方式二
            String str = new String(chars, 0, read);
            System.out.print(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fr != null)
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

###  write()

---

**从内存中写出数据到硬盘的文件里**

说明：

- 1.输出操作，对应的File可以不存在，不会报异常

- 2.

  ***File对应的硬盘文件如果不存在：***在输出的过程中，会自动创建该文件
  ***File对应的硬盘文件如果存在：***
  	如果流使用的构造器是：FileWriter(file,true);写的内容将在原文件的基础上追加
  	如果流使用的构造器是：FileWriter(file,false);写的内容将覆盖原文件的内容
  	构造器FileWriter(file)默认为false

```java
@Test
public void test3()throws Exception{
    //1.初始化文件类，指明要写到的文件
    File file = new File("zmj1.txt");
    //2.初始化FileWriter流对象，用于数据的写出
    FileWriter fw = new FileWriter(file);

    //3.写出操作
    fw.write("zmj123!\n");
    fw.write("hello!");

    //4.关闭流文件
    fw.close();
}

/*
    文件的复制操作
 */
@Test
public void test4(){
    FileReader fr = null;
    FileWriter fw = null;
    try {
        File zmj1File = new File("zmj1.txt");
        File zmj2File = new File("zmj2.txt");

        fr = new FileReader(zmj1File);
        fw = new FileWriter(zmj2File);

        //3.数据的读入和写出操作
        char[] chars = new char[5];
        int len;
        while ((len=fr.read(chars)) != -1){
            fw.write(chars,0,len);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        //4.关闭流
        try {
            if(fr!=null)
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fw!=null)
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 节点流：字节流

**适用于非文本文件(.jpg  .png  .mp3  .doc  .ppt)**

`FileInputStream`
`FileOutputStream`

- 使用字节流处理文本文件 有可能有会导致字符乱码：因为其使用的`byte[]`存储数据而非`char[]`

```java
//使用字节流处理文本文件测试
@Test
public void test() {
    FileInputStream fis = null;
    try {
        File file = new File("zmj2.txt");
        fis = new FileInputStream(file);
        byte[] bytes = new byte[5];
        int len;
        while ((len=fis.read(bytes)) != -1){
            for (int i=0;i<len;i++){
                System.out.print((char) bytes[i]);//hello!￤ﾸﾭ￦ﾖﾇ￤ﾹﾱ￧ﾠﾁ
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if(fis!=null)
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 非文本文件的复制

```java
//非文本文件的复制操作
@Test
public void test2(){
    long start = System.currentTimeMillis();
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
        File file = new File("zmj.jpg");
        File file1 = new File("zmj1.jpg");

        fis = new FileInputStream(file);
        fos = new FileOutputStream(file1);

        byte[] bytes = new byte[5];
        int len;
        while ((len=fis.read(bytes)) != -1){
            fos.write(bytes,0,len);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if(fis!=null)
                fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fos!=null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    long end = System.currentTimeMillis();
    System.out.println("花费时间："+(end-start));//6800毫秒
}


public void copyFile(String srcPath,String destPath){

    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
        File file = new File(srcPath);
        File file1 = new File(destPath);

        fis = new FileInputStream(file);
        fos = new FileOutputStream(file1);

        byte[] bytes = new byte[1024];
        int len;
        while ((len=fis.read(bytes))!=-1){
            fos.write(bytes,0,len);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if(fis!=null)
                fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(fos!=null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void test3(){
    long start = System.currentTimeMillis();

    String srcPath="被复制视频.mp4";
    String destPath="生成目标视频.mp4";
    copyFile(srcPath,destPath);

    long end = System.currentTimeMillis();

    System.out.println("执行时间："+(end-start));
}
```

## 处理流：缓冲流

***处理流，就是“套接”在已有流的基础之上的流***


- 非文本文件缓冲流

  `BufferedInputStream`
  `BufferedOutputStream`

- 文本文件缓冲流

  `BufferedReader`
  `BufferedWriter`

### 缓冲流作用：

---

- 提供流的读取，写入的速度
- 提高读写速度的原因，内部提供了一个缓冲区

复制非文本文件：同样的两张图片：使用缓冲流比不使用复制快了100倍

```java
    /*
        复制非文本文件：缓冲流
     */
    @Test
    public void test(){
        long start = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //1.实例化文件构造器
            File file = new File("zmj.jpg");
            File file1 = new File("zmj1.jpg");

            //2.实例化流
            //2.1实例化节点流
            fis = new FileInputStream(file);
            fos = new FileOutputStream(file1);

            //2.2实例化缓冲流
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            //3.复制操作
            byte[] bytes = new byte[10];
            int len;
            while ((len=bis.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭流
            if(bis!=null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(bos!=null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //关闭外层流的同时，内层流也会自动进行关闭，所以可以省略内层流的关闭操作
//            fis.close();
//            fos.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("使用缓冲流的处理时间："+(end-start));//65毫秒
    }
```

复制文本文件

```java
    //文本文件缓冲流复制操作
    public void test1(){

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(new File("zmj.txt")));
            bw = new BufferedWriter(new FileWriter(new File("zmj2.txt")));

            //方式一
//        char[] buffer = new char[1024];
//        int len;
//        while ((len=br.read(buffer))!=-1){
//            bw.write(buffer,0,len);
//        }

            //方式二 readLine()  -->字符流缓冲流的方法
            String date;
            while ((date=br.readLine()) != null){
    //            bw.write(date);//data中不包含换行符
                bw.write(date);
                bw.newLine();//换行方法
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br!=null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bw!=null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

## 处理流：转换流

`InputStreamReader`：**将一个字节的输入流转换为字符的输入流**

`OutputStreamWriter`：**将一个字符的输出流转换为字节的输出流**

### 转换流作用

- 提供字节流与字符流之间的转换

  解码:字节、字节数组--->字符数组、字符串
  編码:字符数组、字符串---> 字节、字节数组

```java
    @Test
    public void test2(){
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            //使用字节流读取文本文件
            fis = new FileInputStream("zmj2.txt");
            //使用转换流对字节流进行转换 --字符
            //参数2指明了字符集，具体使用哪个字符集，取决于文件保存是使用的字符集编码格式
            isr = new InputStreamReader(fis, "utf-8");

            char[] chars = new char[5];
            int len;
            while ((len=isr.read(chars))!=-1){
//                for(int i=0;i<len;i++){
//                    System.out.println(chars[i]);
//                }
                String str = new String(chars, 0, len);
                System.out.print(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr!=null)
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fis!=null)
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

## 标准的输入输出流

- `System.in`：标准的输入流，默认从键盘输入
- `System.out`：标准的输出流，默认从控制台输出
- System类的`setIn(InputStream is)`/`setOut(PrintStream ps)`方式重新指定输入和输出的方式位置

---

练习：从键盘输入字符串，要求将读取到的整行字符串转成大写输出，然后继续进行输入操作，输入e退出

方法：使用System.in实现。System.in ---> 转换流 ---> BufferedReader的readLine()


```java
public class IoInAndOut {

    public static void main(String[] args) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);

            while (true){
                System.out.println("输入字符串");
                String str = br.readLine();
                if("e".equals(str)){
                    System.out.println("程序退出");
                    break;
                }
                //toUpperCase()将所有在此字符 String使用默认语言环境的规则大写
                String s = str.toUpperCase();
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(isr!=null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (br!=null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## 数据流

作用：**用于读取或写出基本数据类型的变量或字符串**

`DataInputStream`

`DataOutputStream`

---

练习：将内存中的字符串，基本数据类型的变量写出到文件中

```java
@Test
public void test(){
    DataOutputStream dos = null;
    try {
        dos = new DataOutputStream(new FileOutputStream("data.txt"));

        dos.writeUTF("张明均");
        dos.flush();
        dos.writeInt(22);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {

        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 对象流

- 作用：用于存储和读取基本数据类型数据或对象的处理流。他的强大之处就是**可以把java中的对象写入到数据源中，也能把对象从数据源中还原回来**

`ObjectInputStream`

`ObjectOutputStream`

- **java对象可序列化需要满足响应条件，见Person. java**

- 序列化机制:

  - 对象序列化机制允许把内存中的Java对象转换成平台无关的二进制流，从而允许把这种二进制流持久地保存在磁盘上，或通过网络将这种二进制流传输到另一个网络节点。
- 当其它程序获取了这种二进制流，就可以恢复成原来的Java对象。
  - 谈谈你对对象序列化机制的理解。
    		序列化过程: 4
      反序列化过程:
  - 对象要想实现序列化，需要满足哪几个条件。
    		1.实现接口: Serializable\标识接口+
      2.对象所在的类提供常量:序列版本号
      3.要求对象的属性也必须是可序列化的。(基本数据类型、String: 本身就已经是可序列化)

---

可序列化的类Person

```java
/**
 * Created by KingsLanding on 2022/7/2 17:28
 *
 * Person需要满足如下要求，才能序列化
 *  1.需要实现接口Serializable:标识性接口
 *  2.当前类需要提供一个全局常量：serialVersionUID    相当于对象的身份标识，兼容版本控制
 *  3.除了当前Person类需要实现Serializable接口之外，也必须保证其内部所有属性也必须是可序列化的
 *  （默认情况下，基本数据类型是可序列化的）,如果有内部类的话该内部类也要可序列化设置
 *
 *  ObjectOutputStream和ObjectInputStream不能序列化static和transient修饰的成员变量
 */
public class Person implements Serializable {

    public static final long serialVersionUID = 42123354653L;

    String name;
    int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

测试

```java
public class ObjectStream {

    /*
    序列化过程：将内存中的java对象保存到磁盘中或通过网络传输出去
    使用ObjectOutputStream实现
     */
    @Test
    public void test(){
        ObjectOutputStream obs = null;
        try {
            //1.实例化对象流
            obs = new ObjectOutputStream(new FileOutputStream("object.dat"));
            //2.写入对象数据
            obs.writeObject(new String("对像流测试"));
            obs.flush();//刷新操作
            obs.writeObject(new Person("张明均",20));
            obs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                obs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    反序列化过程：将磁盘文件中的对象还原为内存中的一个java对象
    使用ObjectOutputStream进行实现
     */

    @Test
    public void test1(){

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("object.dat"));

            Object obj = ois.readObject();
            String str = (String)obj;
            Person person =(Person)ois.readObject();

            System.out.println(str);
            System.out.println(person);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## NIO

**1.NIO的使用说明:**
		Java NIO (New I0, Non-Blocking I0) 是从Java 1. 4版本开始引入的一套新的I0 API， 可以替代标准的Java I0 API。
		NIO与原来的I0同样的作用和目的，但是使用的方式完全不同，**NIO支持面向缓冲区的(IO是面向流的)、基于通道的I0操作。**
		***NIO将以更加高效的方式进行文件的读写操作。***

​		随着JDK 7的发布，Java对NIO进行了极大的扩展，**增强了对文件处理和文件系统特性的支持**，以至于我们称他们为***NIO2*** 。

---


## 随机存储流RandomAccessFile

- `RandomAccessFile`的使用
  1.RandomAccessFile直接继承于`java. lang. object`类， 实现了`DataInput`和`DataOutput`接口
  2.RandomAccessFile 既可以作为一个输入流，又可以作为一个输出流
  3.如果RandomAccessFile作为输出流时， 写出到的文件如果不存在，则在执行过程中自动创建

***如果写出的文件存在，则会对原有文件内容进行覆盖。(默认情况下， 从头覆盖)***

- 可以通过操作，实现`RandomAccessFile`“插入”的效果，使用`seek()`定位，将该位置后面的所有字符转移到内存中,"插入"完成后再将内存中的字符复写到原文件中;**存在内存占用过多的问题**

``` java
public class Random {
    @Test
    public void test(){
        RandomAccessFile raf1 = null;
        RandomAccessFile raf2 = null;
        try {
            raf1 = new RandomAccessFile(new File("zmj.jpg"),"r");//可读
            raf2 = new RandomAccessFile(new File("zmj2.jpg"),"rw");//可读写

            byte[] bytes = new byte[1024];
            int len;
            while ((len=raf1.read(bytes))!=-1){
                raf2.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                raf1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                raf2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2()throws Exception{

        RandomAccessFile rw = new RandomAccessFile(new File("zmj.txt"), "rw");
        rw.seek(3);
        rw.write(123);
        rw.close();
    }
}
```

URL:统一资源定位符

URL url = new URL("http://")

# 网络编程

- **网络编程中有两个主要的问题:**
  - 1.如何准确地定位网络上-台或多台主机;定位主机上的特定的应用
  - 2.找到主机后如何可靠高效地进行数据传输

- **网络编程中的两个要素:**
  - 1.对应问题一: IP和端口号
  - 2.对应问题二:提供网络通信协议: TCP/IP参考模型(应用层、传输层、网络层、物理+数据链路层）

- **通信要素一: IP和端口号**
  - IP:唯一的标识Internet. 上的计算机(通信实体)   
  - 2.在Java中使用`InetAddress`类代表IP
    
  -  3.IP分类: IPv4和IPv6 ;万维网和局城网
  - 4.域名：www.baidu.com、www.jd.com  
  - 5.本地回路地址:127.0.0.1 对应：`localhost`

- **端口号*:正在计算机上运行的进程。***
      - 要求:不同的进程有不同的端口号
      - 范围:被规定为一个16位的整数`0-65535`。

***端口号与IP地址的组合得出一个网络套接字：`Socket`***

## 实例化InetAddress

- 实例化`InetAddress`:两个方法：`getByName(String host)`、`getLocalHost()`

- 两个常用方法：`getHostName()`获取域名，`getHostAddress()`获取IP地址

```java
public class IpTest {

    @Test
    public void test() throws Exception{

        System.out.println(InetAddress.getByName("38.143.11.116"));
        InetAddress inet1 = InetAddress.getByName("www.wanmeikk.me");
        System.out.println(inet1);
        //获取本地ip
        InetAddress inet2 = InetAddress.getLocalHost();
        System.out.println(inet2);

        //getHostName()获取域名
        String hostName = inet1.getHostName();
        System.out.println(hostName);
        //getHostAddress()获取IP地址
        String hostAddress = inet1.getHostAddress();
        System.out.println(hostAddress);
    }
}
```

## TCP编程

### 消息发送

``` java
/**
 * Created by KingsLanding on 2022/7/4 20:38
 */
public class TCPTest {

    //客户端
    @Test
    public void client(){
        Socket socket = null;
        OutputStream os = null;
        try {
            //1.创建Socket对象，指明服务器端的ip和端口号
            InetAddress inet = InetAddress.getByName("192.168.222.1");
            socket = new Socket(inet, 6699);

        /*
        返回此套接字的输出流。
        如果此套接字具有相关联的通道，则生成的输出流将其所有操作委派给通道。
        如果通道处于非阻塞模式，则输出流的write操作将抛出IllegalBlockingModeException 。
        */
            //2.获取一个输出流，用于输出数据
            os = socket.getOutputStream();
            //3.写出数据的操作
            os.write("你好，这是客户端".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭资源
            try {
                if(os!=null)
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(socket!=null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //服务器端
    @Test
    public void server() {
        ServerSocket serverSocket = null;
        Socket accept = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            //1.创建服务器端的ServerSocket,指明自己的端口号
            serverSocket = new ServerSocket(6699);
            //2.调用accept(),表示接收来自客户端的socket
            accept = serverSocket.accept();
            //3.获取输入流
            is = accept.getInputStream();
            //中文可能乱码
//        byte[] bytes = new byte[1024];
//        int len;
//        while ((len = is.read(bytes)) != -1) {
//            String str = new String(bytes, 0, len);
//            System.out.println(str);
//        }
            //4.读取输入流中的数据
            baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes,0,len);
            }
            System.out.println(baos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            try {
                if (baos!=null)
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is!=null)
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (accept!=null)
                accept.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (serverSocket!=null)
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 文件发送

**客户端向服务器发送文件，服务端保存文件到本地**

```java
/**
 * Created by KingsLanding on 2022/7/5 11:30
 *
 * 客户端向服务器发送文件，服务端保存文件到本地
 */
public class TCPTest2 {

    //客户端
    @Test
    public void client(){
        Socket socket = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            //1.IP地址
            InetAddress inet = InetAddress.getByName("127.0.0.1");
            //2.Socket
            socket = new Socket(inet, 6699);
            //3.获取输出流
            os = socket.getOutputStream();
            //4.获取要发送的文件
            File file = new File("zmj.jpg");
            //5.文件输入流
            fis = new FileInputStream(file);

            //6.写入到socket输出流中
            byte[] bytes = new byte[1024];
            int len;
            while ((len=fis.read(bytes))!=-1){
                os.write(bytes,0,len);
            }
            System.out.println("发送成功");
            //7.关闭数据的输出
            socket.shutdownOutput();
            //8.接收来自服务器端的数据
            InputStream is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes1 = new byte[5];
            int len1;
            while ((len1=is.read(bytes1))!=-1){
                baos.write(bytes1,0,len1);
            }
            System.out.println(baos.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //9.关闭资源
                if (fis!=null)
                fis.close();
                if (os!=null)
                os.close();
                if (socket!=null)
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //服务器端
    @Test
    public void server(){
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //1.定义服务器socket
            serverSocket = new ServerSocket(6699);
            //2.允许接收
            socket = serverSocket.accept();
            //3.获取输入流
            is = socket.getInputStream();
            //4.输出流（写出到--）
            fos = new FileOutputStream(new File("TCPTest.jpg"));
            //5.输出操作
            byte[] bytes = new byte[1024];
            int len;
            while ((len=is.read(bytes))!=-1){
                fos.write(bytes,0,len);
            }
            System.out.println("接收成功");

            //6.向客户端反馈
            OutputStream os = socket.getOutputStream();
            os.write("服务器已收到！".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //7.资源关闭
                if (fos!=null)
                fos.close();
                if (is!=null)
                is.close();
                if (socket!=null)
                socket.close();
                if (serverSocket!=null)
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

# 反射

**关于java. Lang. Class类的理解**

- 1.类的加载过程: :
  		程序经过`javac. exe`命令以后，会生成一个或多个字节码文件(.class结尾)。接着我们使用`java.exe`命令对某个字节码文件进行解释运行。相当于将某个字节码文件加载到内存中。此过程就称为**类的加载**。加载到内存中的类，我们就称为**运行时类**，此运行时类，就作为Class的一 个实例。
- 2.换句话说，***Class的实例就对应着一个运行时类***。
- 3.加载到内存中的运行时类会缓存一定的时间。在此时间之内，我们可以通过不同的方式、来获取此运行时类

---

**反射机制与面向对象中的封装性是不是矛盾的?如何看待两个技术?**

- **不矛盾**

  封装相当于是限制被封装的属性的使用，而不是完全不能使用，这是一种约定的编码规范，而在使用反射的时候并不是在破坏这种规范，反射就像一个工具，这个工具能破坏规范，**但是它产生的目的不是为了破坏规范**。

- 反射是站在我们不知道对象的内部结构但是又不得已非得用到他的时候，比如在各大框架的编写中我们能发现，运用到了很多了反射知识。

- ***目的:为了能动态的产生用户需要的对象***，也正是反射才为我们提供了这么多便捷的框架来供我们开发。这也是反射的真实用处和意义所在。

---

Person测试类

```java
package reflection;

import Annotation.MyAnnotation;

/**
 * Created by KingsLanding on 2022/7/5 17:05
 */
@MyAnnotation
public class Person extends Creature<String>{
    private String name;
    public int age;

    public Person() {
    }

    private Person(String name){
        this.name=name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void show(){
        System.out.println("public方法测试");
    }

    private String test(String str) {
        System.out.println("来自："+str);
        return str;
    }

    @MyAnnotation
    public String eat(String food){
        System.out.println("吃东西");
        return food;
    }

    public static void test2(){
        System.out.println("静态方法测试");
    }
}
```

Creature测试类

```java
package reflection;

import Annotation.MyAnnotation;
import reflection.annotation.interfaceTest;

/**
 * Created by KingsLanding on 2022/7/6 15:52
 */
public class Creature<T> implements interfaceTest {
    private String tiger;
    public int number;

    public Creature() {
    }

    private Creature(String tiger){
        this.tiger=tiger;
    }

    public Creature(String tiger, int number) {
        this.tiger = tiger;
        this.number = number;
    }
    @MyAnnotation
    public String eat(String food){
        System.out.println("吃东西");
        return food;
    }

    private void call(){
        System.out.println("老虎咆哮");
    }

    @Override
    public void song() {
        System.out.println("唱歌");
    }
}
```

接口interfaceTest测试类

```java
/**
 * Created by KingsLanding on 2022/7/7 13:08
 */
public interface interfaceTest {

    public void song();
}
```

注解MyAnnotation测试类

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE,TYPE_PARAMETER,TYPE_USE})
@interface MyAnnotation {
    String value() default "注解";
}
```

## 获取Class的实例的方式

```java
/**
 * Created by KingsLanding on 2022/7/5 21:02
 *
 *      加载到内存中的运行时类，会缓存一定的时间。在此时间之内，我们可以通过不同的方式、
 * 来获取此运行时类
 */
public class ReflectionTest2 {
    //获取Class的实例的方式
    @Test
    public void test() throws Exception {
        //1.方式一、调用运行时类的属性：.class
        Class clazz1 = Person.class;
        System.out.println(clazz1);

        //2.方式二、通过运行时类的对象，调用getClass()方法
        Person person = new Person();
        Class clazz2 = person.getClass();
        System.out.println(clazz2);

        //3.方式三、调用Class的静态方法;froName(String classPath)
        Class clazz3 = Class.forName("reflection.Person");
        System.out.println(clazz3);

        //4.方式四、类的加载器：ClassLoader
        ClassLoader classLoader = ReflectionTest2.class.getClassLoader();
        Class clazz4 = classLoader.loadClass("reflection.Person");
        System.out.println(clazz4);

        System.out.println(clazz1 == clazz2);//true
        System.out.println(clazz1 == clazz3);//true
        System.out.println(clazz1 == clazz4);//true
    }
 }
```

### Class实例可以是哪些结构

```java
@Test
//Class实例可以是哪些结构
public void test4() {
    Class c1 = Object.class;
    Class c2 = Comparable.class;
    Class c3 = String[].class;
    Class c4 = int[][].class;
    Class c5 = ElementType.class;
    Class c6 = Override.class;
    Class c7 = int.class;
    Class c8 = void.class;
    Class c9 = Class.class;
    int[] a = new int[10];
    int[] b = new int[100];
    Class c10 = a.getClass();
    Class c11 = b.getClass();
    //只要元素类型与维度-样，就是同一个Class
    System.out.println(c10 == c11);
}
```

## 使用反射后：对于Person类的操作

```java
public class ReflectionTest1 {

    //未涉及反射之前
    public void test(){
        //1.创建Person类对象
        Person p1 = new Person("zmj", 22);
        //2.通过对象，调用其内部的属性、方法
        p1.age=20;

        p1.show();
        //在Person类的外部，不可以通过Person类的对象调用其内部私有结构：name 、test()、私有构造器

    }
    //反射之后，对于Person的操作
    @Test
    public void test2()throws Exception{
        Class clazz  = Person.class;
        //1.通过反射，创建Person类的对象
        Constructor cons = clazz.getConstructor(String.class, int.class);
        Object obj = cons.newInstance("zmj1", 22);
        Person p = (Person)obj;
        System.out.println(p.toString());
        //2.通过反射，调用对象的指定的属性
        Field age = clazz.getDeclaredField("age");
        age.set(p,21);
        System.out.println(p.toString());

        //3.通过反射，调用方法
        Method show = clazz.getDeclaredMethod("show");
        show.invoke(p);

        /*
        通过反射，可以调用Person类的私有结构：私有属性，似有方法，私有构造器
         */
        System.out.println("********************");

        //调用私有构造器
        Constructor dcons = clazz.getDeclaredConstructor(String.class);
        dcons.setAccessible(true);
        Object obj1 = dcons.newInstance("zmj2");
        Person p1 =(Person)obj1;
        System.out.println(p1);

        //调用私有属性
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(p1,"zmj3");
        System.out.println(p1);

        //调用私有方法
        Method test = clazz.getDeclaredMethod("test", String.class);
        test.setAccessible(true);
        test.invoke(p1,"中国");
        System.out.println(test);

    }
}
```

## 通过反射创建对应的运行时类对象

```java
/**
 * Created by KingsLanding on 2022/7/6 13:37
 *
 * 通过反射创建对应的运行时类对象
 */
public class ReflectionTest5  {
    @Test
    public void test() throws Exception{
        Class<Person> clazz = Person.class;
        /*
        newInstance():调用此方法，创建对应的运行时类的对象，内部调用了运行时类的空参构造器
        此方法能正常创建运行时类的对象的前提
        1.运行类必须提供空参构造器
        2.空参构造器的访问权限必须是可访问的，一般设置为public

        javabean中要求提供public的空参构造器的原因：
        1.便于用过反射，创建运行时类的对象
        2.便于子类继承此运行时类时，默认调用super()时，保证父类有此构造器
         */
        Person person = clazz.newInstance();
        System.out.println(person);
    }

    //反射的动态性演示
    @Test
    public void test2(){
        //生成0-2的随机整数
        for (int i=0;i<10;i++){
            int num = new Random().nextInt(3);
            System.out.println(num);
            String classPath="";
            switch (num){
                case 0:
                    classPath="java.util.Date";
                    break;
                case 1:
                    classPath="reflection.Person";
                    break;
                case 2:
                    classPath="java.lang.Object";
                    break;
            }
            Object instance = null;
            try {
                instance = getInstance(classPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(instance);
        }
    }

    public Object getInstance(String classPath) throws Exception{

        Class clazz = Class.forName(classPath);
        Object o = clazz.newInstance();
        return o;
    }
}
```

## 通过反射获取当前运行时类的各种结构

```java
/**
 * Created by KingsLanding on 2022/7/6 15:45
 */
public class MethodTest {
    @Test
    public void test(){
        Class<Person> pc = Person.class;
        //获取属性结构
        //getFields();获取当前运行时类及其父类中声明为public访问权限的属性
        Field[] fields = pc.getFields();
        for (Field f : fields){
            System.out.println(f);
        }

        System.out.println();
        //获取当前运行时类中声明的所有属性（不包含父类中声明的属性）
        Field[] declaredFields = pc.getDeclaredFields();
        for (Field f:declaredFields){
            System.out.println(f);
        }
    }

    //获取方法
    @Test
    public void test2() throws Exception{
        Class clazz = Class.forName("reflection.Person");
        //getMethods();获取当前运行时类及其所有父类中声明为public权限的方法
        Method[] method = clazz.getMethods();
        for (int i=0;i<method.length;i++){
            System.out.println(method[i]);
        }

        System.out.println("*********");
        //getDeclaredMethods()方法获取当前运行时类中声明的所有方法（不包含父类中声明的方法）
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m : declaredMethods){
            System.out.println(m);
        }
    }

    /*
    通过反射获取方法的各个部分
     */
    @Test
    public void test3(){
        //权限修饰符、返回值类型、方法名（参数类型1 参数名1，……）throws XxxException{}

        Class pc = Person.class;

        Method[] declaredMethods = pc.getDeclaredMethods();
        for (Method m : declaredMethods){
            //1.获取方法声明的注解
            Annotation[] annotations = m.getAnnotations();
            for (Annotation a : annotations){
                System.out.println(a);
            }

            //2.获取权限修饰符
            int modifiers = m.getModifiers();
            String str = Modifier.toString(modifiers);
            System.out.print(str+"\t");
            //3.获取返回值类型
            Class returnType = m.getReturnType();
            System.out.print(returnType.getName()+"\t");
            //4.获取方法名
            System.out.print(m.getName());
            //5.获取形参列表
            System.out.print("(");
            Class[] parameterTypes = m.getParameterTypes();
            if(!(parameterTypes==null||parameterTypes.length == 0)){
                for (Class c : parameterTypes){
                    System.out.print(c.getName());
                }
//                for (int i=0;i<parameterTypes.length;i++){
//                    if(i==parameterTypes.length-1){
//                        System.out.print(parameterTypes[i].getName()+"args_"+i);
//                        break;
//                    }
//                    System.out.print(parameterTypes[i].getName()+"args_"+i+",");
//                }
            }
            System.out.print(")"+"\t");

            /*
            获取抛出的异常
            */
            Class<?>[] exceptionTypes = m.getExceptionTypes();
            if (!(exceptionTypes==null||exceptionTypes.length==0)){
                System.out.println("throws");
                for (int i=0;i<exceptionTypes.length;i++){
                    if(i==exceptionTypes.length-1){
                        System.out.print(exceptionTypes[i].getName()+"args_"+i);
                        break;
                    }
                    System.out.print(exceptionTypes[i].getName()+"args_"+i+",");
                }
            }

            System.out.println();
        }
    }

    /*
    获取构造器
     */
    @Test
    public void test4(){
        Class pc = Person.class;
        //getConstructors()获取当前运行时类中声明为public的构造器
        Constructor[] constructors = pc.getConstructors();
        for (Constructor c : constructors){
            System.out.println(c);
        }
        System.out.println("**********");
        //getDeclaredConstructors()获取当前运行时类中声明的所有构造器
        Constructor[] declaredConstructors = pc.getDeclaredConstructors();
        for (Constructor c : declaredConstructors){
            System.out.println(c);
        }
    }


    @Test
    public void test5(){
        Class clazz = Person.class;
        /*
        获取运行时类的带泛型的父类
         */
        Class superclass = clazz.getSuperclass();
        System.out.println(superclass);
        /*
        获取运行时类的带泛型的父类的泛型
         */
        Type genericSuperclass = clazz.getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        Type[] actualTypeArguments = paramType.getActualTypeArguments();
        System.out.println(actualTypeArguments[0].getTypeName());
    }

    @Test
    public void test6(){
        /*
        获取运行时类实现的接口
         */
        Class pc = Person.class;
        Class[] interfaces = pc.getInterfaces();
        for (Class c : interfaces){
            System.out.println(c);
        }

        /*
        获取运行时类父类实现的接口
         */
        Class[] interfaces1 = pc.getSuperclass().getInterfaces();
        for (Class c : interfaces1){
            System.out.println(c);
        }
    }

    @Test
    public void test7() throws ClassNotFoundException {
        /*
        获取运行时类声明的注解
         */
        Class<?> pe = Class.forName("reflection.Person");
        Annotation[] annotations = pe.getAnnotations();
        for (Annotation a:annotations){
            System.out.println(a);
        }
    }
}
```

## 通过反射操作运行时类中指定的结构

```java
/**
 * Created by KingsLanding on 2022/7/7 13:36
 */
public class FieldTest {

    @Test
    public void test()throws Exception{

        //弃用
        Class pc = Person.class;
        //创建运行时类的对象
        Person p = (Person)pc.newInstance();

        Field name = pc.getField("age");

        name.set(p,20);

        System.out.println(name.get(p));
    }

    @Test
    public void test2() throws Exception {
        /*
        如何操作运行时类中指定的属性--->重要
         */
        Class<Person> pc = Person.class;
        //1.创建运行时类的对象
        Person person = pc.newInstance();
        //2.getDeclaredField():获取运行时类中指定变量名的属性
        Field name = pc.getDeclaredField("name");

        //3.确保当前属性时可访问的
        name.setAccessible(true);

        //4.获取、设置指定对象的此属性值
        name.set(person,"zmj");

        System.out.println(name.get(person));

    }

    /*
    操作运行时类中的方法 --->重点
     */
    @Test
    public void test3() throws Exception {
        Class<Person> pc = Person.class;

        //创建运行时类的对象
        Person person = pc.newInstance();
        //getDeclaredMethod():参数1：指明获取的方法名称 参数2：指明获取方法的形参列表
        Method test = pc.getDeclaredMethod("test", String.class);
        //确保当前方法是可访问的
        test.setAccessible(true);
        //invoke()：参数1：方法的调用者 参数2：给方法形参赋值的实参
        //invoke():返回值即是对应运行时类方法的返回值
        Object invoke = test.invoke(person,"中国");
        System.out.println(invoke);

        /*
        如何调用静态方法
         */
        Method test2 = pc.getDeclaredMethod("test2");

        test2.setAccessible(true);

        //运行时类该方法没有返回值，则此invoke()返回null
        Object invoke1 = test2.invoke(person);
        System.out.println(invoke1);//null
    }

    /*
    如何调用运行时类指定的构造器
     */
    @Test
    public void test4() throws Exception {
        Class<Person> pc = Person.class;
        //getDeclaredConstructor();获取指定类型的构造器参数列表；参数：指明构造器的参数列表
        Constructor<Person> constructor = pc.getDeclaredConstructor(String.class);

        constructor.setAccessible(true);
        //调用此构造器创建运行类的对象
        Person zmj = constructor.newInstance("zmj");
        System.out.println(zmj);

    }
}
```

# 类加载器ClassLoader

- 引导类加载器，JVM自带的类加载器，负责Java平台核心库，**装载核心类库该加载器无法直接获取**
- 扩展类加载器：负责`jre/lib/ext`目录下的`jar`包中类的加载
- 系统类加载器：负责`java-classpath`或`java.class.path`所指的目录下的类、自定义类的加载，最常用的类加载器

```java
public class ReflectionTest3 {

    @Test
    public void test(){
        //自定义类，使用系统类加载器加载
        ClassLoader classLoader = ReflectionTest2.class.getClassLoader();
        System.out.println(classLoader);
        //调用系统类加载器的getParent():获取扩展类加载器
        ClassLoader classLoader1 = classLoader.getParent();
        System.out.println(classLoader1);
        //调用扩展类加载器的getParent():无法获取引导类加载器
        //引导类加载器主要负责加载java核心类库，无法自定义加载
        ClassLoader classLoader2 = classLoader1.getParent();
        System.out.println(classLoader2);//null

        ClassLoader classLoader3 = String.class.getClassLoader();
        System.out.println(classLoader3);//null

    }
}
```

### 使用ClassLoader加载配置文件

```java
/**
 * Created by KingsLanding on 2022/7/6 12:20
 *
 * 使用ClassLoader加载配置文件
 */
public class ReflectionTest4 {

    @Test
    public void test()throws Exception{
        //此时文件默认在当前module下
        //传统方式读取配置文件
        Properties properties = new Properties();
//        FileInputStream fis = new FileInputStream("jdbc.properties");
//        properties.load(fis);

        //使用ClassLoader读取配置文件的方式
        //此时配置文件默认路径为当前module的src下
        ClassLoader classLoader = ReflectionTest4.class.getClassLoader();
        InputStream rs = classLoader.getResourceAsStream("jdbc.properties");
        properties.load(rs);

        String name = properties.getProperty("name");
        String password = properties.getProperty("password");

        System.out.println(name+"---->"+password);
    }
}
```

# 动态代理

## 静态代理

``` java
/**
 * Created by KingsLanding on 2022/7/7 20:18
 *
 * 静态代理
 *
 *  代理类和被代理类在编译期间就确定下来了
 */
public class proxyStaticTest {

    public static void main(String[] args) {
        //创建被代理类对象
        Server server = new Server();
        //创建代理类对象
        Proxy proxy = new Proxy(server);
        proxy.run();
    }
}

interface proxyTest{

    void run();
}

//被代理类
class Server implements proxyTest{
    @Override
    public void run() {
        System.out.println("奔跑");
    }
}

//代理类
class Proxy implements proxyTest{
    private proxyTest pt;
    //构造器
    public Proxy(proxyTest pt){
        this.pt=pt;
    }
    @Override
    public void run() {
        System.out.println("代理工厂做了些准备工作");
        pt.run();
        System.out.println("收尾工作");
    }
}
```

## 动态代理

```java
/**
 * Created by KingsLanding on 2022/7/8 12:31
 *
 *
 * 要想实现动态代理，需要解决的问题?
 *      问题一:如何根据加载到内存中的被代理类，动态的创建一个代理类及其对象。
 *      问题二:当通过代理类的对象调用方法时，如何动态的去调用被代理类中的同名方法。
 *
 * 动态代理步骤：
 *  1.创建一个实现接口InvocationHandler的类，它必须实现invoke方法
 *  2.创建被代理的类以及接口
 *  3.通过Proxy的静态方法
 *  newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h)创建一个代理
 *  4.通过代理调用方法

 */

interface Human {
    String getBelief();

    void eat(String food);
}

//被代理类
class SuperMan implements Human {

    @Override
    public String getBelief() {
        return "I believe I can fly!";
    }

    @Override
    public void eat(String food) {
        System.out.println("I like eat " + food);
    }
}

/*
要想实现动态代理，需要解决的问题？
问题一：如何根据加载到内存中的被代理类，动态的创建一个代理类及其对象。
问题二：当通过代理类的对象调用方法a时，如何动态的去调用被代理类中的同名方法a。
*/

//创建实现了InvocationHandler接口的类
class MyInvocationHanlder implements InvocationHandler {
    private Object obj;//需要使用被代理类的对象进行赋值

    public void bind(Object obj) {
        this.obj = obj;
    }
    //当我们通过代理类的对象，调用方法a时，就会自动的调用如下的方法：invoke()
    //将被代理类要执行的方法a的功能就声明在invoke()中
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //method:即为代理类对象调用的方法，此方法也就作为了被代理类对象要调用的方法
        //obj:被代理类的对象
        System.out.println("调用之前测试");
        Object returnValue = method.invoke(obj, args);
        System.out.println("调用之后测试");
        //上述方法的返回值就作为当前类中的invoke()的返回值。
        return returnValue;
    }
}

class ProxyFactory {
    //调用此方法，返回一个代理类的对象。解决问题一
    public static Object getProxyInstance(Object obj) {
        MyInvocationHanlder hanlder = new MyInvocationHanlder();
        hanlder.bind(obj);
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),hanlder);

    }
}

//测试动态代理
public class DynamicProxy {
    public static void main(String[] args) {
        SuperMan superMan = new SuperMan();
        //proxyInstance:代理类的对象
        Human proxyInstance = (Human) ProxyFactory.getProxyInstance(superMan);
        //当通过代理类对象调用方法时，会自动的调用被代理类中同名的方法
        String belief = proxyInstance.getBelief();
        System.out.println(belief);
        proxyInstance.eat("火锅");
    }
}
```

``` java
/**
 * Created by KingsLanding on 2022/7/8 21:18
 */

interface Person{

    String Name(String name);

    void eat(String food);
}

//目标实现类（被代理类）
class Student implements Person{
    @Override
    public String Name(String name) {
        System.out.println("我是"+name);
        return name;
    }

    @Override
    public void eat(String food) {
        System.out.println("我喜欢吃"+food);
    }
}

/**
 * 调用处理器实现类
 * 每次生成动态代理类对象时都需要指定一个实现了该接口的调用处理器对象
 */
class StudentImpl implements InvocationHandler {

    /**
     * 这个就是我们要代理的真实对象
     */
    private Object obj;
    /**
     * 构造方法，给我们要代理的真实对象赋初值
    */
    public StudentImpl(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 该方法负责集中处理动态代理类上的所有方法调用。
         * 调用处理器根据这三个参数进行预处理或分派到委托类实例上反射执行
         */
        System.out.println("在调用代理类之前--测试");
        //调用代理类方法
        //当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        Object returnValue = method.invoke(obj, args);

        System.out.println("在调用结束之后--测试");
         return returnValue;
    }

}

public class Test {

    public static void main(String[] args) {
        //目标类（代理类对象）
        Student student = new Student();
        /**
        * InvocationHandlerImpl 实现了 InvocationHandler 接口，并能实现方法调用从代理类到委托类的分派转发
        * 其内部通常包含指向委托类实例的引用，用于真正执行分派转发过来的方法调用.
        * 即：要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法
        */
        StudentImpl handler = new StudentImpl(student);
        ClassLoader classLoader = student.getClass().getClassLoader();
        Class<?>[] interfaces = student.getClass().getInterfaces();
        /**
         * 该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例
         */
        Person person = (Person)Proxy.newProxyInstance(classLoader, interfaces, handler);
        System.out.println(person.getClass().getName());
        person.eat("麻辣烫");
        String zmj = person.Name("zmj");
        System.out.println(zmj);

    }

}
```


