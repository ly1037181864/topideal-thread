package thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 死锁案例
 * 线程A持有锁A 试图获取锁B
 * 线程B持有锁B 试图获取锁A
 *
 * 产生死锁的原因
 * 系统资源的不足
 * 资源分配的不当
 * 线程运行推进的
 *
 * 解决两个命令 jps -l
 * jstack 进程号
 *
 * synchronized和Lock 效果一样
 *
 */
public class DealLockDemo {
    private Object lA;
    private Object lB;
    public DealLockDemo(Object lA,Object lB){
        this.lA = lA;
        this.lB = lB;
    }

    public DealLockDemo(){
    }

    private Lock lockA = new ReentrantLock();
    private Lock lockB = new ReentrantLock();

    public void methodA(){
        lockA.lock();
        try{
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println(Thread.currentThread().getName()+"\t 自己持有锁A，尝试获取锁B");
            methodB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lockA.unlock();
        }
    }

    public void methodB(){
        lockB.lock();
        try{
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println(Thread.currentThread().getName()+"\t 自己持有锁B，尝试获取锁A");
            methodA();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lockB.unlock();
        }
    }

    public void method(){
        synchronized (lA) {
            System.out.println(Thread.currentThread().getName()+"\t 自己持有"+lA+",\t尝试获取"+lB);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lB) {
                //
               // System.out.println(Thread.currentThread().getName()+"\t 自己持有"+lA+",\t尝试获取"+lB);
            }
        }
    }


    public static void main(String[] args) {
        DealLockDemo demo = new DealLockDemo();
        new Thread(()->{
            demo.methodA();
        },"AA").start();

        new Thread(()->{
            demo.methodB();
        },"BB").start();

    }

    private static void test() {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(()->{
            new DealLockDemo(lockA,lockB).method();
        },"AA").start();
        new Thread(()->{
            new DealLockDemo(lockB,lockA).method();
        },"BB").start();
    }
}
