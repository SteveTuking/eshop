package cn.cust.eshop.inventory.controller;

import javax.annotation.Resource;

import cn.cust.eshop.inventory.command.ProductInventoryCacheRefreshRequestCommand;
import cn.cust.eshop.inventory.command.ProductInventoryDBUpdateRequestCommand;
import cn.cust.eshop.inventory.command.RequestCommand;
import cn.cust.eshop.inventory.model.ProductInventory;
import cn.cust.eshop.inventory.service.ProductInventoryService;
import cn.cust.eshop.inventory.service.RequestAsyncProcessService;
import cn.cust.eshop.inventory.vo.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 商品库存Controller
 * @author Administrator
 * 
 * 大家考虑一下，我要模拟的场景：
 * 
 *（1）一个更新商品库存的请求过来，然后此时会先删除redis中的缓存，然后模拟卡顿5秒钟
 *（2）在这个卡顿的5秒钟内，我们发送一个商品缓存的读请求，因为此时redis中没有缓存，就会来请求将数据库中最新的数据刷新到缓存中
 *（3）此时读请求会路由到同一个内存队列中，阻塞住，不会执行
 *（4）等5秒钟过后，写请求完成了数据库的更新之后，读请求才会执行
 *（5）读请求执行的时候，会将最新的库存从数据库中查询出来，然后更新到缓存中
 *
 * 如果是不一致的情况，可能会出现说redis中还是库存为100，但是数据库中也许已经更新成了库存为99了
 * 
 * 现在做了一致性保障的方案之后，就可以保证说，数据是一致的
 */
@Controller
public class ProductInventoryController {

	@Resource
	private RequestAsyncProcessService requestAsyncProcessService;
	@Resource
	private ProductInventoryService productInventoryService;


	/**
	 * 更新商品库存
	 */
	@RequestMapping("/updateProductInventory")
	@ResponseBody
	public Response updateProductInventory(ProductInventory productInventory) {
		Response response = null;

		try {
			RequestCommand requestCommandt = new ProductInventoryDBUpdateRequestCommand(
					productInventory, productInventoryService);
			requestAsyncProcessService.process(requestCommandt);
			response = new Response(Response.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = new Response(Response.FAILURE);
		}

		return response;
	}

	/**
	 * 获取商品库存
	 */
	@RequestMapping("/getProductInventory")
	@ResponseBody
	public ProductInventory getProductInventory(Integer productId) {
		ProductInventory productInventory = null;

		try {
			RequestCommand requestCommand = new ProductInventoryCacheRefreshRequestCommand(
					productId, productInventoryService);
			requestAsyncProcessService.process(requestCommand);

			// 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
			// 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
			long startTime = System.currentTimeMillis();
			long endTime = 0L;
			long waitTime = 0L;

			// 等待超过200ms没有从缓存中获取到结果
			while(true) {
				if(waitTime > 200) {
					break;
				}

				// 尝试去redis中读取一次商品库存的缓存数据
				productInventory = productInventoryService.getProductInventoryCache(productId);

				// 如果读取到了结果，那么就返回
				if(productInventory != null) {
					System.out.println("===========日志===========: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
					return productInventory;
				}

				// 如果没有读取到结果，那么等待一段时间
				else {
					Thread.sleep(20);
					endTime = System.currentTimeMillis();
					waitTime = endTime - startTime;
				}
			}

			// 直接尝试从数据库中读取数据
			productInventory = productInventoryService.findProductInventory(productId);
			if(productInventory != null) {
				//没有串行化
				productInventoryService.setProductInventoryCache(productInventory);
				return productInventory;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ProductInventory(productId, -1L);
	}
	
}
