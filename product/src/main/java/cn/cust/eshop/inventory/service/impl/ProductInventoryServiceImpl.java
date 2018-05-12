package cn.cust.eshop.inventory.service.impl;

import javax.annotation.Resource;

import cn.cust.eshop.inventory.dao.RedisDAO;
import cn.cust.eshop.inventory.mapper.ProductInventoryMapper;
import cn.cust.eshop.inventory.model.ProductInventory;
import cn.cust.eshop.inventory.service.ProductInventoryService;
import org.springframework.stereotype.Service;


/**
 * 商品库存Service实现类
 * @author Administrator
 *
 */
@Service("productInventoryService")  
public class ProductInventoryServiceImpl implements ProductInventoryService {

	@Resource
	private ProductInventoryMapper productInventoryMapper;
	@Resource
	private RedisDAO redisDAO;
	
	@Override
	public void updateProductInventory(ProductInventory productInventory) {
		productInventoryMapper.updateProductInventory(productInventory); 
	}

	@Override
	public void removeProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.delete(key);
	}
	
	/**
	 * 根据商品id查询商品库存
	 * @param productId 商品id 
	 * @return 商品库存
	 */
	@Override
	public ProductInventory findProductInventory(Integer productId) {
		return productInventoryMapper.findProductInventory(productId);
	}
	
	/**
	 * 设置商品库存的缓存
	 * @param productInventory 商品库存
	 */
	@Override
	public void setProductInventoryCache(ProductInventory productInventory) {
		String key = "product:inventory:" + productInventory.getProductId();
		redisDAO.set(key, String.valueOf(productInventory.getInventoryCnt()));  
	}

	@Override
	public ProductInventory getProductInventoryCache(Integer productId) {
		Long inventoryCnt = 0L;

		String key = "product:inventory:" + productId;
		String result = redisDAO.get(key);

		if(result != null && !"".equals(result)) {
			try {
				inventoryCnt = Long.valueOf(result);
				return new ProductInventory(productId, inventoryCnt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
