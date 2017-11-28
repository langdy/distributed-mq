package com.yjl.distributed.mq.factory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.yjl.distributed.mq.config.ConnectionConfig;
import com.yjl.distributed.mq.config.ConnectionConfigServer;
import com.yjl.distributed.mq.constant.BaseConstant;
import com.yjl.distributed.mq.constant.RouteModeEnum;

/**
 * mq集群管理器 TODO 配置完善，释放连接池
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月16日 上午10:54:40
 */
public class ActivemqPools {

	/**
	 * 多集群连接地址
	 */
	private static List<String> brokerUrls = new CopyOnWriteArrayList<String>();

	/**
	 * 多集群连接池集合
	 */
	private static List<ActivemqPoolFactory> pools = new CopyOnWriteArrayList<ActivemqPoolFactory>();

	/**
	 * 轮询算法当前下标
	 */
	private static AtomicInteger index = new AtomicInteger(0);


	/**
	 * 获取所有集群连接池对象
	 * 
	 * @return
	 */
	public static synchronized List<ActivemqPoolFactory> getPools() {
		if (pools == null || pools.isEmpty()) {
			// 初始化连接池
			initPools();
		}

		return pools;
	}

	/**
	 * 根据路由算法获取单个集群连接池
	 * 
	 * @param tag 标签，哈希算法专用，一般可用于同一类业务的顺序消息发送，比如同一个订单
	 * @param routeMode 路由算法{@link com.yjl.distributed.mq.constant.RouteModeEnum}
	 * @return
	 */
	public static synchronized ActivemqPoolFactory getPool(String tag, String routeMode) {
		if (pools == null || pools.isEmpty()) {
			// 初始化连接池
			initPools();
		}

		if (RouteModeEnum.HASH.getCode().equals(routeMode)) {
			// 哈希路由算法实现
			int hash = hash(tag);
			return pools.get(Math.abs(hash % pools.size()));
		} else if (RouteModeEnum.RANDOM.getCode().equals(routeMode)) {
			// 随机路由算法实现
			return pools.get(new Random().nextInt(pools.size()));
		} else if (RouteModeEnum.POLLING.getCode().equals(routeMode)) {
			// 轮询路由算法实现
			int i = index.getAndIncrement();
			if (i >= pools.size()) {
				// 到达池的末尾，从头开始获取
				i = 0;
				index.set(1);
			}
			return pools.get(i);
		}

		// 默认随机路由
		return pools.get(new Random().nextInt(pools.size()));

	}


	/**
	 * 初始化连接池
	 * 
	 * @throws Exception
	 */
	private static void initPools() {

		pools = new CopyOnWriteArrayList<ActivemqPoolFactory>();
		List<ConnectionConfig> configs = ConnectionConfigServer.getConfigs();
		if (configs == null) {
			return;
		}

		for (ConnectionConfig config : configs) {

			// broker连接url
			String brokerUrl = config.getBrokerUrl();

			// 用户名密码
			String username = config.getUsername();
			username = StringUtils.isEmpty(username) ? null : username;
			String password = config.getPassword();
			password = StringUtils.isEmpty(password) ? null : password;

			// 最大连接数
			Integer maxConnections = config.getMaxConnections();
			if (maxConnections == null || maxConnections <= 0) {
				maxConnections = BaseConstant.DEFAULT_MAX_CONNECTIONS;
			}

			// 每个连接中使用的最大活动会话数
			Integer maximumActiveSessionPerConnection = config.getMaximumActiveSession();
			if (maximumActiveSessionPerConnection == null || maximumActiveSessionPerConnection <= 0) {
				maximumActiveSessionPerConnection = BaseConstant.DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION;
			}

			// 最大连接数
			Integer maxThreadPoolSize = config.getMaxThreadPoolSize();
			if (maxThreadPoolSize == null || maxThreadPoolSize <= 0) {
				maxThreadPoolSize = BaseConstant.DEFAULT_MAX_THREAD_POOL_SIZE;
			}

			pools.add(new ActivemqPoolFactory(brokerUrl, username, password, maxConnections,
					maximumActiveSessionPerConnection, maxThreadPoolSize));

		}

	}

	/**
	 * 偷用HashMap的hash算法
	 * 
	 * @param key
	 * @return
	 */
	private static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}

}
