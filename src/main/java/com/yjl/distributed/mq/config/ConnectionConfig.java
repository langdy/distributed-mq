package com.yjl.distributed.mq.config;

import java.io.Serializable;

/**
 * 连接配置
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月16日 下午6:02:30
 */

public class ConnectionConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1620619519130349011L;

	private Long id;
	/**
	 * broker集群唯一标志
	 */
	private String brokerKey;
	/**
	 * 集群地址
	 */
	private String brokerUrl;
	/**
	 * 登录用户名
	 */
	private String username;
	/**
	 * 登录密码
	 */
	private String password;
	/**
	 * 最大连接数
	 */
	private Integer maxConnections;
	/**
	 * 每个连接中使用的最大活动会话数
	 */
	private Integer maximumActiveSession;
	/**
	 * 连接池中线程池大小
	 */
	private Integer maxThreadPoolSize;
	/**
	 * 状态:ENABLE-可用，DISABLE-不可用，DELETE-删除
	 */
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrokerKey() {
		return brokerKey;
	}

	public void setBrokerKey(String brokerKey) {
		this.brokerKey = brokerKey;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Integer getMaximumActiveSession() {
		return maximumActiveSession;
	}

	public void setMaximumActiveSession(Integer maximumActiveSession) {
		this.maximumActiveSession = maximumActiveSession;
	}

	public Integer getMaxThreadPoolSize() {
		return maxThreadPoolSize;
	}

	public void setMaxThreadPoolSize(Integer maxThreadPoolSize) {
		this.maxThreadPoolSize = maxThreadPoolSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
