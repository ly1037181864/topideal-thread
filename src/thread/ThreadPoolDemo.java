package thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 第4种获得/使用Java多线程的方式，线程池
 * 工作中常常需要自己手写线程池，因为无论是newFixedThreadPool/newSingleThreadExecutor/newCachedThreadPool它的底层最大的队列等待默认都是Integer.MAX_VALUE
 * 这就回造成等待队列中阻塞大量的请求，从而造成系统不必要的开销造成OOM
 *
 * 一个线程池能够容纳的并发执行的线程数为maximunPoolSize+等待队列大小
 *
 * maximunPoolSize并不等于corePoolSize+等待队列大小
 * maximunPoolSize表示该线程池最大的能同时执行的线程数
 * corePoolSize表示工作线程数 可以理解为我们日常工作中满足基本需求的工作线程数
 * 等待队列大小，可以理解为在corePoolSize之余最大可以容纳的线程等待数
 * maximunPoolSize+等待队列大小 他们决定了该线程池最大支持的并发数 当然等待队列并不是越大越好，会造成等待的线程数过多，资源的消耗，造成内存益处，同时maximunPoolSize配置
 * 需要根据具体的业务需求，判定业务是属于CPU密集型还是IO密集型并根据相应的业务类型作出具体的策略处理，详见印象笔记
 *
 * 5大参数
 *
 * 4中拒绝策略
 *
 * 手写线程池
 */
public class ThreadPoolDemo {
    public static ExecutorService getPool(){
        return new ThreadPoolExecutor(2,5,1l,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }

    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                10,
                1l,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            for (int i=1;i<=20;i++) {
                final int tem = i;
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t前来办理业务" + tem);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }

    private static void test1() {
        //获取cpu硬件核数
        System.out.println(Runtime.getRuntime().availableProcessors());
//        ExecutorService threadPool = Executors.newFixedThreadPool(1);//一池5个处理线程
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();//一池1个处理线程
//        ExecutorService threadPool = Executors.newCachedThreadPool();//一池多处理线程
        ExecutorService threadPool = ThreadPoolDemo.getPool();

        //模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
        try{
            for (int i=1;i<=9;i++){
                threadPool.execute(()->{
//                    try {
                        System.out.println(Thread.currentThread().getName()+"\t 前来办理业务");
//                        Random random = new Random();
//                        int nextInt = random.nextInt(5);
//                        TimeUnit.SECONDS.sleep(nextInt+1);
                        //System.out.println(Thread.currentThread().getName()+"\t 业务办理完");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                });
            }
        }catch (Exception e){

        }finally{
            threadPool.shutdown();
        }
    }
}
