package com.yjl.distributed.mq.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yjl.distributed.mq.factory.ActivemqPools;

/**
 * 全局常量
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月14日 下午5:24:50
 */
public class BaseConstant {

	/**
	 * 自定义参数->classespath:activemq.properties
	 */
	public static final Properties properties = new Properties();

	static {
		try {
			InputStream inStream = ActivemqPools.class.getClassLoader().getResource("activemq.properties").openStream();
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 设置连接的最大连接数
	 */
	public static final int DEFAULT_MAX_CONNECTIONS = 10;
	/**
	 * 设置每个连接中使用的最大活动会话数
	 */
	public static final int DEFAULT_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION = 300;
	/**
	 * 连接池中线程池大小
	 */
	public static final int DEFAULT_MAX_THREAD_POOL_SIZE = 10;

	/**
	 * 是否持久化消息
	 */
	public static final boolean DEFAULT_IS_PERSISTENT = true;

}
