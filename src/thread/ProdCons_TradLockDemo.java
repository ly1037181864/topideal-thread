package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消费者生产者模式传统版 Lock版
 */
public class ProdCons_TradLockDemo {
    private int i;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void add() throws InterruptedException {
        lock.lock();
        try{
            while(i!=0)
                condition.await();

            i+=1;
            System.out.println(Thread.currentThread().getName()+"\t"+i);
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void desc() throws InterruptedException {
        lock.lock();
        try{
            while(i!=1)
                condition.await();

            i-=1;
            System.out.println(Thread.currentThread().getName()+"\t"+i);
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProdCons_TradSynchDemo demo = new ProdCons_TradSynchDemo();
        new Thread(()->{
            try {
                for (int i=1;i<=5;i++)
                    demo.add();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

        new Thread(()->{
            try {
                for (int i=1;i<=5;i++)
                    demo.desc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BB").start();
    }
}
