package cn.cust.eshop.inventory.listener;

import cn.cust.eshop.inventory.command.RequestInvokerThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zhangbing on 2018/5/12.
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RequestInvokerThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
