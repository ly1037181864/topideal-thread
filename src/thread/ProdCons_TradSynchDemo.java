package thread;

/**
 * 传统的消费者生产者消费模式 synchronized版 1.6及以后的jdk对synchronized进行了优化，使其性能得到大幅提升
 * 题目：一个初始值为零的变量，两个线程对其交替操作，一个加1一个减1 来5轮
 * 多线程高并发的企业及口诀
 * 上联 高内聚 低耦合的情况下 线程操作资源类
 * 下联 条件判断 阻塞 干活 改变条件 唤醒
 * 横幅 严防多线程环境下的线程虚假唤醒 所以条件判断是用while而不是if
 */
public class ProdCons_TradSynchDemo {
    private int i;

    //加
    synchronized public void add() throws InterruptedException {
        //判断
        while(i != 0)
            this.wait();

        //干活
        i+=1;
        System.out.println(Thread.currentThread().getName()+"\t"+i);
        //唤醒
        this.notifyAll();
    }

    //减
    synchronized public void desc() throws InterruptedException {
        while(i!= 1)
            this.wait();

        i-=1;
        System.out.println(Thread.currentThread().getName()+"\t"+i);
        this.notifyAll();

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
