package com.yjl.distributed.mq.config;

import java.util.List;
import com.yjl.distributed.mq.common.HttpClientUtil;
import com.yjl.distributed.mq.constant.BaseConstant;
import com.yjl.fastjson.JSON;
import com.yjl.fastjson.JSONArray;
import com.yjl.fastjson.JSONObject;
import com.yjl.fastjson.TypeReference;

public class ConnectionConfigServer {

    /**
     * 从配置中心获取mq配置信息
     * 
     * @return
     */
    public static List<ConnectionConfig> getConfigs() {
        try {
            String url = BaseConstant.properties.getProperty("configServerUrl");
            String res = HttpClientUtil.doGet(url, null, null);
            JSONObject obj = JSON.parseObject(res);

            JSONArray text = obj.getJSONArray("responseContent");
            List<ConnectionConfig> configs = JSON.parseObject(text.toJSONString(),
                    new TypeReference<List<ConnectionConfig>>() {});

            return configs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
