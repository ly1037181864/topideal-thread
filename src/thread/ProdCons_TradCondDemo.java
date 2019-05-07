package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消费者生产者模式传统版 多Condition版
 * 实现A->B->C三个线程启动
 * 要求
 * AA打印5次,要求BB打印10次,要求CC打印15次
 * 紧接着
 * AA打印5次,要求BB打印10次,要求CC打印15次
 * 。。。
 * 来10轮
 */
public class ProdCons_TradCondDemo {
    private int i = 1;
    private Lock lock = new ReentrantLock();
    private Condition C1 = lock.newCondition();
    private Condition C2 = lock.newCondition();
    private Condition C3 = lock.newCondition();
    private static ThreadLocal<Cond> threadLocal = new ThreadLocal();

    public void print(int num) throws InterruptedException {
        Cond cond = init(num);
        lock.lock();
        try{
            //条件
            while(i != num)
                cond.getCondC().await();

            //干活
            int count = num == 1 ? 5 : num == 2 ? 10 :15;
            for (int i=1;i<=count;i++)
                System.out.println(Thread.currentThread().getName()+"线程\t 打印第"+i+"次");

            //改变条件
            i = num == 1 ? 2 : num == 2 ? 3 : 1;
            //唤醒
            cond.getCondN().signal();
        }finally {
            lock.unlock();
        }
    }

    public Cond init(int num){
        Cond cond = threadLocal.get();
        if(cond == null) {
            Condition c = num == 1 ? C1 : num == 2 ? C2 :C3;
            Condition n = num == 1 ? C2 : num == 2 ? C3 :C1;
            cond = new Cond(c,n);
            threadLocal.set(cond);
        }
        return cond;
    }

    public static void main(String[] args) {
        ProdCons_TradCondDemo demo = new ProdCons_TradCondDemo();
        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BB").start();

        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"CC").start();
    }
}
class Cond {
    private Condition condC;//当前Condition
    private Condition condN;//下一个Condition

    public Cond(Condition c,Condition n){
        this.condC = c;
        this.condN = n;
    }

    public Condition getCondC() {
        return condC;
    }

    public Condition getCondN() {
        return condN;
    }
}

