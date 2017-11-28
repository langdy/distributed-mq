package com.yjl.distributed.mq.producer;

/**
 * 生产者消息体
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年11月15日 下午1:27:28
 */
public class Message {

	/**
	 * 路由标志
	 */
	private String tag;

	/**
	 * 消息内容
	 */
	private Object msg;

	public Message(String tag, Object msg) {
		this.tag = tag;
		this.msg = msg;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}


}
