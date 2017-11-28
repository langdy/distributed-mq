package com.yjl.distributed.mq.factory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.yjl.distributed.mq.constant.BaseConstant;

/**
 * mq连接池
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月14日 下午5:40:28
 */
public class ActivemqPoolFactory {
	// 最大连接数
	private int maxConnections;
	// 每个连接中使用的最大活动会话数
	private int maximumActiveSessionPerConnection;
	// 连接地址
	private String brokerUrl;
	// 连接用户名
	private String username;
	// 连接密码
	private String password;
	// 连接池中线程池大小
	private int maxThreadPoolSize;
	// activemq自带连接池
	private PooledConnectionFactory pooledConnectionFactory;

	/**
	 * 无密码连接
	 * 
	 * @param brokerUrl
	 */
	public ActivemqPoolFactory(String brokerUrl) {
		this(brokerUrl, ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD);
	}

	/**
	 * 默认配置连接
	 * 
	 * @param brokerUrl
	 * @param userName
	 * @param password
	 */
	public ActivemqPoolFactory(String brokerUrl, String userName, String password) {
		this(brokerUrl, userName, password, BaseConstant.DEFAULT_MAX_CONNECTIONS,
				BaseConstant.DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION, BaseConstant.DEFAULT_MAX_THREAD_POOL_SIZE);
	}

	/**
	 * 自定义配置连接
	 * 
	 * @param brokerUrl
	 * @param username
	 * @param password
	 * @param maxConnections 最大连接数
	 * @param maximumActiveSessionPerConnection 每个连接中使用的最大活动会话数
	 * @param maxThreadPoolSize 连接池中线程池大小
	 */
	public ActivemqPoolFactory(String brokerUrl, String username, String password, int maxConnections,
			int maximumActiveSessionPerConnection, int maxThreadPoolSize) {
		this.brokerUrl = brokerUrl;
		this.username = username;
		this.password = password;
		this.maxConnections = maxConnections;
		this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
		this.maxThreadPoolSize = maxThreadPoolSize;
		init();
	}

	/**
	 * 初始化连接池
	 */
	private void init() {
		// ActiveMQ的连接工厂
		ActiveMQConnectionFactory connectionFactory =
				new ActiveMQConnectionFactory(this.username, this.password, this.brokerUrl);
		if (this.maxThreadPoolSize >= 1) {
			connectionFactory.setMaxThreadPoolSize(this.maxThreadPoolSize);
		}

		// ActiveMQ中的连接池工厂
		this.pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
		this.pooledConnectionFactory.setCreateConnectionOnStartup(true);
		this.pooledConnectionFactory.setMaxConnections(this.maxConnections);
		this.pooledConnectionFactory.setMaximumActiveSessionPerConnection(this.maximumActiveSessionPerConnection);

	}


	/**
	 * 获取连接池
	 * 
	 * @return
	 */
	public PooledConnectionFactory getPooledConnectionFactory() {
		return pooledConnectionFactory;
	}

	/**
	 * 设置连接池
	 * 
	 * @param connectionFactory
	 */
	public void setPooledConnectionFactory(PooledConnectionFactory pooledConnectionFactory) {
		this.pooledConnectionFactory = pooledConnectionFactory;
	}

}
