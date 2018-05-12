package cn.cust.eshop.inventory.command;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 命令的执行线程
 * Created by zhangbing on 2018/5/10.
 */
public class CommandInvokerThread implements Callable<Boolean> {

    private ArrayBlockingQueue<RequestCommand> commands ;

    public CommandInvokerThread(ArrayBlockingQueue<RequestCommand> commands) {
        this.commands = commands;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while(true){
                RequestCommand request = commands.take();
                // 执行这个request操作
                request.process();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
