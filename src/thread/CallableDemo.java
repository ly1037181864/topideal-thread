package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 实现多线程的四种方法
 * 1.继承Thread类
 * 2.实现Runnable接口
 * 3.实现Callable接口
 * 4.线程池
 * Callable和Runnable接口异同
 * 1.Callable需要返回值
 * 2.Callable需要抛出异常
 * 3.Callable需要重写call接口
 *
 * 分支合并处理思想
 *
 * 当多个线程共用一个FutureTask对象时，有且仅有一个线程进入并完成实际逻辑
 * 对Java而言相同的操作无须重复操作，而是将结果复用，如果非要如此，则必须再
 * 次新建一个新的FutureTask对象，可以共用一个CallableDemo对象，也可以新
 * 建新的CallableDemo对象
 */
public class CallableDemo implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName()+"***************** coming in doing");
        TimeUnit.SECONDS.sleep(3);
        return "1024";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableDemo demo = new CallableDemo();

        FutureTask<String> futureTask = new FutureTask(demo);
        FutureTask<String> futureTask2 = new FutureTask(demo);//new FutureTask(new CallableDemo());
        Thread t1 = new Thread(futureTask,"AAA");
        Thread t2 = new Thread(futureTask2,"BBB");
        //Thread t2 = new Thread(futureTask2,"BBB");
        t1.start();
        t2.start();

        while(!futureTask.isDone())
            ;

        //.sout 快捷打印 自动补全代码
        System.out.println(futureTask.get());//建议放在最后 get方法获取结果时，如果没有结束，则会阻塞，知道获取结果为止
    }
}
