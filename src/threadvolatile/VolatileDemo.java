package threadvolatile;

import java.util.concurrent.TimeUnit;

/**
 * volatile关键字 乞丐版的synchronized
 * volatile java提供的一种轻量级的同步机制
 * volatile 保证可见性、禁止指令重排 但不保证原子性（值的改变不依赖于本身）
 * volatile和synchronized不同，他是一种轻量级的线程同步机制，修饰的是变量，而synchronized可以修饰方法，也可以用在同步代码快中
 * volatile强调的是变量在线程之间的可见性，即某一线程对变量的改变会导致其他线程立刻知晓这一变化，而synchronized强调的是线程之间的同步
 * 在多线程下会发生阻塞。
 *
 * 详见印象笔记中的volatile实现原理
 */
public class VolatileDemo {
    volatile private boolean flag = true;//true:开闸 false:关闸

    public static void main(String[] args) throws InterruptedException {
        VolatileDemo demo = new VolatileDemo();

        new Thread(()->{
            while (demo.isFlag()) {
                System.out.println(Thread.currentThread().getName()+"\t进入循环");
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"\t结束循环");

        },"AA").start();

        TimeUnit.SECONDS.sleep(2);

        System.out.println(Thread.currentThread().getName()+"\t游戏结束");
        demo.setFlag(false);
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
