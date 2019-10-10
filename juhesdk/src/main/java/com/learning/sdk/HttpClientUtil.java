package com.learning.sdk;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientUtil {
    private static final String TAG = "HTTP";

    public static JSONObject jsonPost(String strURL, Map<String, String> params) {
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            JSONObject jsonObject = new JSONObject(params);
            String s = jsonObject.toString();
            out.append(s);
            out.flush();
            out.close();
            Log.e("ad",s);

            int code = connection.getResponseCode();
            InputStream is;
            if (code == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }

            StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            try {
                JSONObject jsonObject1 = new JSONObject(sb.toString());
                return jsonObject1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            responseReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }

    public static String requestGet(String urlHost, Map<String, String> paramsMap) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = urlHost;
            if (tempParams.length() > 0) {
                requestUrl = urlHost + "?" + tempParams.toString();
            }
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(100 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(100 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            String result = null;
            // 判断请求是否成功
            Log.e("url",requestUrl);
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Get方式请求成功，result--->" + result);
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString()+" "+urlHost);
            e.printStackTrace();
            return null;
        }
    }

    public static String webviewGet(String urlStr, Context context) {
        try {
            // 新建一个URL对象
            URL url = new URL(urlStr);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Upgrade-Insecure-Requests", url.getHost());
            urlConn.addRequestProperty("User-Agent", AppUtils.getUserAgent());
            urlConn.addRequestProperty("Accept", "Keep-Alive");
            urlConn.addRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConn.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            urlConn.addRequestProperty("X-Requested-With", context.getPackageName());
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            String result = null;
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }


    //post表单请求
    public static String post(String url, Map<String, String> form) {
        HttpURLConnection conn = null;
        PrintWriter pw = null;
        BufferedReader rd = null;
        StringBuilder out = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line = null;
        String response = null;
        for (String key : form.keySet()) {
            if (out.length() != 0) {
                out.append("&");
            }
            out.append(key).append("=").append(form.get(key));
        }
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(out.toString());
            pw.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
            Log.e(TAG, "Get方式请求成功，result--->" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (rd != null) {
                    rd.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
