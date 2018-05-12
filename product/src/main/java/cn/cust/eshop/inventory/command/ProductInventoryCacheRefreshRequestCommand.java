package cn.cust.eshop.inventory.command;


import cn.cust.eshop.inventory.model.ProductInventory;
import cn.cust.eshop.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 * @author Administrator
 *
 */
public class ProductInventoryCacheRefreshRequestCommand implements RequestCommand {

	/**
	 * 商品id
	 */
	private Integer productId;
	/**
	 * 商品库存Service
	 */
	private ProductInventoryService productInventoryService;
	
	public ProductInventoryCacheRefreshRequestCommand(Integer productId,
													  ProductInventoryService productInventoryService) {
		this.productId = productId;
		this.productInventoryService = productInventoryService;
	}

	@Override
	public Integer getProductId() {
		return productId;
	}

	@Override
	public void process() {
		// 从数据库中查询最新的商品库存数量
		ProductInventory productInventory = productInventoryService.findProductInventory(productId);
		// 将最新的商品库存数量，刷新到redis缓存中去
		productInventoryService.setProductInventoryCache(productInventory); 
	}
	
}
