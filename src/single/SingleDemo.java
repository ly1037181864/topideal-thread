package single;

import java.util.concurrent.TimeUnit;

/**
 * 多线程环境下的单例模式
 * 懒加载模式下的线程安全问题
 * 方式1在多线程环境下，由于并发的存在 导致多次生成对象demo
 *
 * 方式2在多线程环境下，由于条件满足，而同步代码快只是在赋值过程中，导致即使其他线程生产demo对象后，获取锁的当前线程仍然会重新生成demo对象
 *
 * 方式3解决了重复生成demo对象的问题，但引来了一个新的问题
 * demo = new SingleDemo(name,age);
 * 实际上虚拟机将这一步骤一分为三
 * 分配内存空间
 * 初始化对象属性
 * 将内存空间指向对象
 * 但由于三个步骤见不存在数据依赖关系就有可能演变为 分配内存空间 将内存空间指向对象 初始化对象属性 这个属性将导致在多线程的环境下，对象只生产一个
 * 但获取对象时，是空有其名而无其实，即只有对象本身，但属性尚未初始化。故而需要在申明变量时，加volatile修饰，表示禁止指令重排
 */
public class SingleDemo {
    private String name;
    private int age;
    volatile private static SingleDemo demo;
    private SingleDemo(){

    }

    private SingleDemo(String name,int age){
        System.out.println(Thread.currentThread().getName()+"\t"+name);
        System.out.println(Thread.currentThread().getName()+"\t 初始化对象");
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"\t"+age);
    }

    //方式1
    public static SingleDemo getDemo(){
        if(demo == null)
            demo = new SingleDemo();
        return demo;
    }

    //方式2
    public static SingleDemo getDemo2(){
        if(demo == null) {
            synchronized (SingleDemo.class){
                demo = new SingleDemo();
            }
        }
        return demo;
    }

    //方式3 DCL 双端检测机制
    public static SingleDemo getDemo3(String name,int age){
        if(demo == null) {
            synchronized (SingleDemo.class){
                if(demo == null)
                    demo = new SingleDemo(name,age);
            }
        }
        return demo;
    }

    @Override
    public String toString() {
        return this.name+","+this.age;
    }

    public static void main(String[] args) {
        int count = 10;
        for (int i=0;i<count;i++){
            new Thread(()->{
                SingleDemo demo = SingleDemo.getDemo3("张三", 28);
                System.out.println();
            },i+"").start();
        }
    }
}
