package cn.cust.eshop.inventory;

import cn.cust.eshop.inventory.command.RequestCommandQueue;
import cn.cust.eshop.inventory.listener.InitListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import redis.clients.jedis.Jedis;

import java.io.Serializable;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
@MapperScan("cn.cust.eshop.inventory.mapper")
public class Application{
 
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new DataSource();
    }
    
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
 
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
//    @Bean
//	public JedisCluster JedisClusterFactory() {
//		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//		jedisClusterNodes.add(new HostAndPort("47.93.23.61", 6379));
////		jedisClusterNodes.add(new HostAndPort("192.168.31.19", 7004));
////		jedisClusterNodes.add(new HostAndPort("192.168.31.227", 7006));
//		JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);
//		jedisCluster.auth("cn.cust");
//		return jedisCluster;
//	}

    /**
     * 注册监听器
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean =
                new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new InitListener());
        return servletListenerRegistrationBean;
    }


    @Bean
    public Jedis jedisClusterFactory() {
        Jedis jedis = new Jedis("47.93.23.61",6379);
        jedis.auth("cn.cust");
        return jedis;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
 
}