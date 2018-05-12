package cn.cust.eshop.inventory.service;


import cn.cust.eshop.inventory.command.RequestCommand;

/**
 * 请求异步执行的service
 * @author Administrator
 *
 */
public interface RequestAsyncProcessService {

	void process(RequestCommand requestCommand);
	
}
