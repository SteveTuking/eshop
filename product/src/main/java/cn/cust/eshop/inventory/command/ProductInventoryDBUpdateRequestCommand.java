package cn.cust.eshop.inventory.command;


import cn.cust.eshop.inventory.model.ProductInventory;
import cn.cust.eshop.inventory.service.ProductInventoryService;

/**
 * 比如说一个商品发生了交易，那么就要修改这个商品对应的库存
 * 
 * 此时就会发送请求过来，要求修改库存，那么这个可能就是所谓的data update request，数据更新请求
 * 
 * cache aside pattern
 * 
 * （1）删除缓存
 * （2）更新数据库
 *
 *
 *
 */
public class ProductInventoryDBUpdateRequestCommand implements RequestCommand {

	/**
	 * 商品库存
	 */
	private ProductInventory productInventory;
	/**
	 * 商品库存Service
	 */
	private ProductInventoryService productInventoryService;
	
	public ProductInventoryDBUpdateRequestCommand(ProductInventory productInventory,
												  ProductInventoryService productInventoryService) {
		this.productInventory = productInventory;
		this.productInventoryService = productInventoryService;
	}
	
	@Override
	public void process() {
		// 删除redis中的缓存
		productInventoryService.removeProductInventoryCache(productInventory); 
		// 修改数据库中的库存
		productInventoryService.updateProductInventory(productInventory);  
	}
	
}
