package thread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * SynchronousQueue同步阻塞队列 生产一个消费一个 不生产不消费，不消费不生产
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        SynchronousQueue<String> queue = new SynchronousQueue();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"\t放入元素A");
                queue.put("A");

                System.out.println(Thread.currentThread().getName()+"\t放入元素B");
                queue.put("B");

                System.out.println(Thread.currentThread().getName()+"\t放入元素C");
                queue.put("C");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

        new Thread(()->{

            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t获取元素"+queue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t获取元素"+queue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"\t获取元素"+queue.take());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        },"BB").start();

    }

}
