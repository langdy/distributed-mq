package com.yjl.distributed.mq.constant;

import java.util.Objects;

/**
 * 状态:ENABLE-可用，DISABLE-不可用，DELETE-删除
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年10月18日 上午9:49:23
 */
public enum StatusEnum implements BaseEnum<StatusEnum> {

	/**
	 * 可用
	 */
	ENABLE("ENABLE", "可用"),
	/**
	 * 不可用
	 */
	DISABLE("DISABLE", "不可用"),
	/**
	 * 删除
	 */
	DELETE("DELETE", "删除");

	/** code **/
	private String code;

	/** 注释 **/
	private String comment;

	StatusEnum(String code, String comment) {
		this.code = code;
		this.comment = comment;
	}

	@Override
	public boolean isEnumCode(final String inputCode) {
		return Objects.nonNull(getEnum(inputCode));
	}

	@Override
	public boolean isNotEnumCode(final String inputCode) {
		return !isEnumCode(inputCode);
	}

	@Override
	public String getCodeComment(final String inputCode) {
		if (Objects.isNull(inputCode)) {
			return null;
		}
		for (StatusEnum value : StatusEnum.values()) {
			if (value.getCode().equals(inputCode)) {
				return value.getComment();
			}
		}
		return null;
	}

	@Override
	public StatusEnum getEnum(final String inputCode) {
		if (Objects.isNull(inputCode)) {
			return null;
		}
		for (StatusEnum thisEnum : StatusEnum.values()) {
			if (thisEnum.getCode().equals(inputCode)) {
				return thisEnum;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


}
