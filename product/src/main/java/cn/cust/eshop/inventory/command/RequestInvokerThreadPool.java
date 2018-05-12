package cn.cust.eshop.inventory.command;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangbing on 2018/5/10.
 */
public class RequestInvokerThreadPool {

    private ExecutorService poll= Executors.newFixedThreadPool(10);

    public RequestInvokerThreadPool() {
        RequestCommandQueue requestCommandQueue = RequestCommandQueue.getInstance();

        for(int i = 0; i < 10; i++) {
            ArrayBlockingQueue<RequestCommand> queue = new ArrayBlockingQueue<RequestCommand>(100);
            requestCommandQueue.addQueue(queue);
            poll.submit(new CommandInvokerThread(queue));
        }
    }

    private static class Singleton{


        private static RequestInvokerThreadPool requestInvokerThreadPool;

        static {
            requestInvokerThreadPool = new RequestInvokerThreadPool();
        }

        public static RequestInvokerThreadPool getInstance(){
            return requestInvokerThreadPool;
        }
    }

    public static RequestInvokerThreadPool getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }


}
