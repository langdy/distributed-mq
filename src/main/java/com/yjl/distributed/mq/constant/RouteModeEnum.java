package com.yjl.distributed.mq.constant;


import java.util.Objects;

/**
 * 消息路由方式
 * 
 * @author zhaoyc@1109
 * @version 创建时间：2017年10月18日 上午9:49:23
 */
public enum RouteModeEnum implements BaseEnum<RouteModeEnum> {

    /**
     * 哈希
     */
    HASH("HASH", "哈希"),
    /**
     * 随机
     */
    RANDOM("RANDOM", "随机"),
    /**
     * 轮询
     */
    POLLING("POLLING", "轮询");

    /** code **/
    private String code;

    /** 注释 **/
    private String comment;

    RouteModeEnum(String code, String comment) {
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
        for (RouteModeEnum value : RouteModeEnum.values()) {
            if (value.getCode().equals(inputCode)) {
                return value.getComment();
            }
        }
        return null;
    }

    @Override
    public RouteModeEnum getEnum(final String inputCode) {
        if (Objects.isNull(inputCode)) {
            return null;
        }
        for (RouteModeEnum thisEnum : RouteModeEnum.values()) {
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
