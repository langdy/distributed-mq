package com.yjl.distributed.mq.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.yjl.distributed.mq.config.ConnectionConfig;
import com.yjl.fastjson.JSON;
import com.yjl.fastjson.JSONArray;
import com.yjl.fastjson.JSONObject;
import com.yjl.fastjson.TypeReference;

/**
 * 
 * @author zhaoyc
 * 
 */
public class HttpClientUtil {
    /**
     * 
     * @param url
     * @param querys 参数
     * @param headers 请求头
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> querys, Map<String, String> headers)
            throws Exception {

        url = buildUrl(url, querys);// 把参数加到url后面

        HttpClient client = HttpClients.createDefault();// 创建HttpClient对象

        HttpGet request = new HttpGet(url);// 创建GET请求（在构造器中传入URL字符串即可）

        // 把请求头加上
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        // 调用HttpClient对象的execute方法获得响应
        HttpResponse response = client.execute(request);

        // 调用HttpResponse对象的getEntity方法得到响应实体
        HttpEntity httpEntity = response.getEntity();

        // 使用EntityUtils工具类得到响应的字符串表示
        String result = EntityUtils.toString(httpEntity, "utf-8");

        return result;
    }


    /**
     * url参数封装
     * 
     * @param url
     * @param querys
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String buildUrl(String url, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(url);

        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    /**
     * 
     * @param url
     * @param parameters
     * @return
     * @throws Exception
     */
    public static String doPost(String url, List<NameValuePair> parameters) throws Exception {
        return doPost(url, parameters, null);
    }

    /**
     * 
     * @param url
     * @param parameters
     * @param headers 请求头
     * @throws Exception
     */
    public static String doPost(String url, List<NameValuePair> parameters,
            Map<String, String> headers) throws Exception {
        HttpClient client = HttpClients.createDefault();// 创建HttpClient对象

        HttpPost post = new HttpPost(url); // 创建POST请求

        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                post.setHeader(header.getKey(), header.getValue());
            }
        }

        // 向POST请求中添加消息实体
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));

        // 得到响应并转化成字符串
        HttpResponse response = client.execute(post);
        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity, "utf-8");
        return result;
    }

    public static void main(String[] args) throws Exception {

        // 2.doGet,带json解析
        String url = "http://10.41.1.2:28868/connection-configs/list";
        String res = HttpClientUtil.doGet(url, null, null);
        JSONObject obj = JSON.parseObject(res);

        JSONArray text = obj.getJSONArray("responseContent");
        List<ConnectionConfig> configs = JSON.parseObject(text.toJSONString(),
                new TypeReference<List<ConnectionConfig>>() {});
        for (ConnectionConfig config : configs) {
            System.out.println(config.getBrokerUrl());
        }


    }
}
