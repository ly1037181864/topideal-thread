package thread;

/**
 * 消费者生产者模式传统版 volatile版
 * Thread.yield()只是让线程回到了可运行状态，让出cpu执行时间，该方法不会释放锁
 * volatile的有序性和可见性能够保证两个线程之间进行交替执行，而不会产生线程安全问题
 */
public class ProdCons_TradVolatileDemo {
    volatile private int i;

    public void add(){
        while(i!=0)
            Thread.yield();

        i+=1;
        System.out.println(Thread.currentThread().getName()+"\t"+i);
    }

    public void desc(){
        while(i!=1)
            Thread.yield();
        i-=1;

        System.out.println(Thread.currentThread().getName()+"\t"+i);
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
