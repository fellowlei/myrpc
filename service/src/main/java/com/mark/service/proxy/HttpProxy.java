package com.mark.service.proxy;

import com.alibaba.fastjson.JSONArray;
import com.mark.service.util.KryoTool;
import com.mark.service.util.PropertiesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Method;
import java.util.*;

public class HttpProxy {

    private static final Log LOG = LogFactory.getLog(HttpProxy.class);

    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    private static List<String> httpIps = new ArrayList<String>();
    private static String host;

    static {
        host = PropertiesUtil.getPropertyValue("mockRpc.host");
        String ips = PropertiesUtil.getPropertyValue("mockRpc.ips");
        for (String s : ips.split(",")) {
            if (s != null) httpIps.add(s.trim());
        }
    }

    public static Object invokeHttpProxy(String type, Method method, Object[] args) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("p", "123456");
        data.put("bean", type);
        data.put("method", method.getName());
        data.put("args", KryoTool.encode(args));
        String text = sendHttpRequest(data);
        JSONArray array = JSONArray.parseArray(text);
        if(array == null || array.isEmpty()) {
            return null;
        }
        try {
            Object decode = KryoTool.decode(array.getString(1), Class.forName(array.getString(0)));
            return decode;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found", e);
        }
    }

    public static String sendHttpRequest(Map<String, String> data) {
        for (int i = 0; i < 3; i++) {
            try {
                String ip = httpIps.get(new Random().nextInt(httpIps.size()));
                HttpPost httpPost = new HttpPost("http://" + ip + "/call");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                httpPost.setHeader("Host", host);
                CloseableHttpResponse response2 = httpclient.execute(httpPost);

                try {
                    HttpEntity entity2 = response2.getEntity();
                    return EntityUtils.toString(entity2);
                } finally {
                    response2.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Mock Error");
    }
}
