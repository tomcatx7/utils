package util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class FileDownLoadUtil {
    /**
     * @return 根据日期生成文件路径
     */
    public static String getFilepath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY:MM:DD:HH:MM:SSS");
        String currentTime = simpleDateFormat.format(new Date());
        String[] split = currentTime.split(":");
        String Year = split[0];
        String Month = split[1];
        String Day = split[2];
        String Hour = split[3];
        String Min = split[4];
        String Sec = split[5];
        return Year + File.separator
                + Month + File.separator
                + Day + File.separator
                + Hour + Min + Sec;
    }

    /**
     * @param fileDir  文件保存目录
     * @param fileSuff 文件名后缀
     * @return 返回生成文件
     * @throws IOException
     */
    public static File getFile(String fileDir, String fileSuff) throws IOException {
        String filepath = fileDir + getFilepath();
        File file = new File(filepath + "." + fileSuff);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
//        String fileDir = filepath.substring(0, filepath.lastIndexOf("\\"));
//        String fileName = filepath.substring(filepath.lastIndexOf("\\") + 1);

//        File dir = new File(fileDir);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        File file = new File(fileDir, fileName + "." + fileSuff);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
    }

    public static void fileDown(byte[] content, String fileDir, String suff) throws IOException {
        BufferedOutputStream bos = null;
        File htmlFile = getFile(fileDir, suff);
        try {
            bos = new BufferedOutputStream(new FileOutputStream(htmlFile));
            bos.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            htmlFile.delete();
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param list    待下载文件列表
     * @param fileDir 文件保存目录
     * @param suff    文件名后缀
     * @throws IOException
     */
    public static void fileDown(HashSet<String> list, String fileDir, String suff) throws IOException {

        URL url = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream out = null;

        for (String fileUrl : list) {
            File file = getFile(fileDir, suff);
            try {
                //url = new URL("http://www.cnblogs.com/images/xml.gif");
                url = new URL(fileUrl);
                conn = (HttpURLConnection) url.openConnection();
                //设置请求超时时间
                conn.setConnectTimeout(5000);
                //防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                bis = new BufferedInputStream(conn.getInputStream());
                byte[] bytes = readInputStream(bis);
                out = new BufferedOutputStream(new FileOutputStream(file));
                out.write(bytes);
            } catch (IOException e) {
                System.out.println("download file fail...");
                file.delete();
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用 httpclient 请求数据
     * @param list
     * @param fileDir
     * @param suff
     * @throws IOException
     */
    public static void fileDown2(HashSet<String> list, String fileDir, String suff) throws IOException {

        HttpClient httpClient = null;
        GetMethod getMethod = null;
        BufferedInputStream bis = null;
        BufferedOutputStream out = null;

        for (String fileUrl : list) {
            File file = getFile(fileDir, suff);
            try {
                //url = new URL("http://www.cnblogs.com/images/xml.gif");
                if(fileUrl == null || fileUrl.equals(""))continue;
                httpClient = new HttpClient();
                httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
                httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY); // 设置浏览器相同的cookie接收策略
                getMethod = new GetMethod(fileUrl);
                getMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
                int status = httpClient.executeMethod(getMethod);
                if (status != HttpStatus.SC_OK) throw new IOException("返回码异常，请求失败");
                bis = new BufferedInputStream(getMethod.getResponseBodyAsStream());
                byte[] bytes = readInputStream(bis);
                out = new BufferedOutputStream(new FileOutputStream(file));
                out.write(bytes);
            } catch (IOException e) {
                System.out.println("download file fail...");
                file.delete();
                e.printStackTrace();
            } finally {
                if (getMethod != null) getMethod.releaseConnection();
                try {
                    if (bis != null) bis.close();
                    if (out != null) out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readInputStream(InputStream in) {
        ByteArrayOutputStream bos = null;
        byte[] bytes = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            System.out.println("read input stream fail...");
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }


    public static void main(String[] args) throws IOException {
        fileDown("hello".getBytes("utf-8"), "d:\\crawl\\html\\", "html");
        System.out.println("下载完成");

    }
}

