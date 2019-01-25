package page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.CharsetDetector;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Page {
    private String url;
    private String contentType;
    private byte[] responseBody;
    private String charSet = "utf-8";
    private String html;
    private Document doc;

    public Page(String url, String contentType, byte[] responseBody) {
        this.url = url;
        this.contentType = contentType;
        this.responseBody = responseBody;
    }

    public Document getDoc() {
        if (doc != null){
            return doc;
        }
        try {
            doc = Jsoup.parse(getHtml(), url);
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCharSet() {
       // charSet = CharsetDetector.guessCharset(responseBody);
        return charSet;
    }

    public String getHtml() {
        if (html != null) {
            return html;
        }
        if (responseBody == null) {
            return null;
        }
        try {
            html = new String(responseBody, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("getHtml fail...");
            e.printStackTrace();
        }
        return html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Page{" +
                "url='" + url + '\'' +
                ", contentType='" + contentType + '\'' +
                ", responseBody=" + Arrays.toString(responseBody) +
                '}';
    }
}
