package cn.cust.eshop.inventory.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by zhangbing on 2018/5/10.
 */
public class RequestCommandQueue {

    /**
     * 内存队列
     */
    private static List<ArrayBlockingQueue<RequestCommand>> commands = new ArrayList<>();

    /**
     * 使用静态内部类来做初始化，
     * 不需要
     */
    private static class Singleton{
        private static RequestCommandQueue requestCommandQueue;

        static{
            requestCommandQueue = new RequestCommandQueue();
        }

        public static RequestCommandQueue getInstance() {
            return requestCommandQueue;
        }

    }

    /**
     * 类的初始化
     *  1、invokeStatic、new、putStatic、getStatic等关键字。
     *  2、反射的时候
     *  3、检查到有main方法的时候
     *  4、初始化子类父类没有被初始化的时候
     *  5、动态绑定语言的支持
     *
     *  初始化的时候<cinit>会保证类中的静态代码块(静态常量、静态代码块、静态类、、、、、)顺序执行，
     *  jvm来保证线程安全性
     * @return
     */
    public static RequestCommandQueue getInstance(){
        return Singleton.getInstance();
    }

    public void addQueue(ArrayBlockingQueue<RequestCommand> command){
        commands.add(command);
    }

    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize() {
        return commands.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<RequestCommand> getQueue(int index) {
        return commands.get(index);
    }
}

