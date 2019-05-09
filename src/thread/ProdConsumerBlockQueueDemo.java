package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费模式 BlockQueue阻塞队列版
 */
public class ProdConsumerBlockQueueDemo {
    private BlockingQueue<String> blockingQueue;
    private AtomicInteger num = new AtomicInteger();
    volatile private boolean flag = true;

    public ProdConsumerBlockQueueDemo(BlockingQueue blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    public void prod() throws InterruptedException {
        String data = null;
        boolean returnValue;
        while(flag) {
            data = num.incrementAndGet()+"";
            returnValue = blockingQueue.offer(data,2L,TimeUnit.SECONDS);
            if(returnValue)
                System.out.println(Thread.currentThread().getName()+"\t 生产"+data+"成功");
            else
                System.out.println(Thread.currentThread().getName()+"\t 生产"+data+"失败");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t 生产叫停");
    }

    public void cons() throws InterruptedException {
        String data = null;
        while(flag) {
            data = blockingQueue.poll(2l,TimeUnit.SECONDS);
            if(data == null){
                flag = false;
                System.out.println(Thread.currentThread().getName()+"\t 超时2秒，没有获取到，消费退出");
                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t 消费"+data+"成功");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t 生产已停止，消费结束");
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static void main(String[] args) throws InterruptedException {
        ProdConsumerBlockQueueDemo demo = new ProdConsumerBlockQueueDemo(new ArrayBlockingQueue(10));//new SynchronousQueue());
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 生产者线程开启");
            try {
                demo.prod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"prod").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 消费者线程开启");
            try {
                demo.cons();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"cons").start();

        TimeUnit.SECONDS.sleep(5);

        demo.setFlag(false);
    }
}
