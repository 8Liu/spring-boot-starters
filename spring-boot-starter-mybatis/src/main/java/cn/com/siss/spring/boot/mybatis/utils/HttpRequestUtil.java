package cn.com.siss.spring.boot.mybatis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/1/23.
 */
@Slf4j
public class HttpRequestUtil {

    protected HttpRequestUtil() {

    }

    /**
     * 连接时间
     */
    private static int connectTimeout = 30000;

    /**
     * 响应时间
     */
    private static int readTimeout = 60000;


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param)
            throws Exception {
        return sendPost(url, param, "application/x-www-form-urlencoded");
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数
     * @param contentType 媒体类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String contentType)
            throws Exception {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", contentType);
        return sendPost(url, param, connectTimeout, readTimeout, headerMap);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url            发送请求的 URL
     * @param param          请求参数
     * @param contentType    媒体类型
     * @param connectTimeout 连接时间
     * @param readTimeout    响应时间
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,
                                  String contentType,
                                  int connectTimeout,
                                  int readTimeout)
            throws Exception {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", contentType);
        return sendPost(url, param, connectTimeout, readTimeout, headerMap);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url            发送请求的 URL
     * @param param          请求参数
     * @param connectTimeout 连接时间
     * @param readTimeout    响应时间
     * @param headerMap      请求头
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url,
                                  String param,
                                  int connectTimeout,
                                  int readTimeout,
                                  Map<String, String> headerMap)
            throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (null != headerMap && !headerMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    if (!StringUtils.isEmpty(entry.getValue())) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置编码 防止到服务端出现中文乱码
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out = new PrintWriter(osw, true);
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            //使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("sendPost method close PrintWriter or BufferedReader " + ex);
            }
        }
        return result;
    }

}
