package cn.cust.eshop.inventory.dao.impl;

import cn.cust.eshop.inventory.dao.RedisDAO;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;


@Repository("redisDAO")
public class RedisDAOImpl implements RedisDAO {
	@Resource
	private Jedis jedisCluster;

	@Override
	public void set(String key, String value) {
		jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public void delete(String key) {
		jedisCluster.del(key);
	}

}
