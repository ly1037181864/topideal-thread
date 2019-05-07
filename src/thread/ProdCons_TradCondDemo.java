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

    public void print(Cond cond) throws InterruptedException {
        lock.lock();
        try{
            int num = cond.getNum();
            while(i != num)
                cond.getCondC().await();

            int count = num == 1 ? 5 : num == 2 ? 10 :15;
            for (int i=1;i<=count;i++)
                System.out.println(Thread.currentThread().getName()+"\t"+i);

            i = num == 1 ? 2 : num == 2 ? 3 : 1;
            cond.getCondN().signal();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProdCons_TradCondDemo demo = new ProdCons_TradCondDemo();
        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(new Cond(1,demo.getC1(),demo.getC2()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(new Cond(2,demo.getC2(),demo.getC3()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BB").start();

        new Thread(()->{
            try {
                for (int i=0;i<10;i++)
                    demo.print(new Cond(3,demo.getC3(),demo.getC1()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"CC").start();
    }

    public Condition getC1() {
        return C1;
    }

    public void setC1(Condition c1) {
        C1 = c1;
    }

    public Condition getC2() {
        return C2;
    }

    public void setC2(Condition c2) {
        C2 = c2;
    }

    public Condition getC3() {
        return C3;
    }

    public void setC3(Condition c3) {
        C3 = c3;
    }
}
class Cond {
    private int num;
    private Condition condC;
    private Condition condN;

    public Cond(int i,Condition c,Condition n){
        this.num = i;
        this.condC = c;
        this.condN = n;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Condition getCondC() {
        return condC;
    }

    public void setCondC(Condition condC) {
        this.condC = condC;
    }

    public Condition getCondN() {
        return condN;
    }

    public void setCondN(Condition condN) {
        this.condN = condN;
    }
}

