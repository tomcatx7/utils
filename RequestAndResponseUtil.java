package page;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestAndResponseUtil {

    public static Page getPage(String url) {
        Page page = null;
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY); // 设置浏览器相同的cookie接收策略
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            int status = httpClient.executeMethod(getMethod);
            if (status != HttpStatus.SC_OK) {
                System.out.println("connection fail..." + getMethod.getStatusLine());
                return null;
            }
            byte[] responseBody = readInputStream(getMethod.getResponseBodyAsStream());
            String contentType = getMethod.getResponseHeader("Content-Type").getValue();
            page = new Page(url, contentType, responseBody);
        } catch (IOException e) {
            System.out.println("get responsebody fail");
            e.printStackTrace();
            return null;
        } finally {
            getMethod.releaseConnection();
        }
        return page;
    }

    public static byte[] readInputStream(InputStream is) {
        BufferedInputStream bin = null;
        ByteArrayOutputStream bout = null;
        byte[] result = null;
        try {
            bin = new BufferedInputStream(is);
            int len = 0;
            byte[] buff = new byte[1024];
            bout = new ByteArrayOutputStream();
            while ((len = bin.read(buff)) != -1) {
                bout.write(buff, 0, len);
            }
            result = bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) bin.close();
                if (bout != null) bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
