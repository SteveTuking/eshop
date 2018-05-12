package cn.cust.eshop.inventory.command;

/**
 * Created by zhangbing on 2018/5/10.
 */
public interface RequestCommand {

    public Integer getProductId();
    public void process();
}
