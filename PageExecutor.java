package page;

import filter.Links;
import handler.Handler;
import handler.ImgHandler;
import util.FileDownLoadUtil;
import util.PageUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class PageExecutor {

    public static void exe(String[] urls, Handler handler) throws IOException {
        Links htmlLinks = new Links();
        //将源url添加到未访问url队列
        for (String url : urls) {
            htmlLinks.addUnvisitedUrl(url);
        }
        //控制爬取数量在1000以内
        while (!htmlLinks.unVisitedUrlQueue.isEmpty() && htmlLinks.visitedUrlQueue.size() <= 100) {
            //从待访问url队列中取出url
            String visitedUrl = htmlLinks.getUnvisitedUrl();
            if (visitedUrl == null) continue;
            Page page = RequestAndResponseUtil.getPage(visitedUrl);
            htmlLinks.addVisitedUrl(visitedUrl);
            //请求page失败，放弃本次url处理
            if (page == null) continue;
            String aSelector = "a";
            HashSet<String> a_links = PageUtil.getLinks(page, aSelector);
            if (a_links.size() != 0) {
                for (String a_link : a_links) {
                    htmlLinks.addUnvisitedUrl(a_link);
                    System.out.println("新增爬取连接：" + a_link);
                }
            }
            System.out.println("===================================");
            try {
                //将爬取的页面保存在磁盘中
                FileDownLoadUtil.fileDown(page.getResponseBody(), "D:\\crawl\\html\\", "html");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("下载html文件失败...");
            }
        }

        //对本地html文件进行处理
        String htmlFilepath = "D:\\crawl\\html\\2019\\01\\24";
        File htmlFile = new File(htmlFilepath);
        if (htmlFile.exists() && htmlFile.isDirectory()) {
            handler.handler(htmlFile);
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("start....");
        String[] urls = {"https://www.sina.com.cn/"};
        exe(urls, new ImgHandler());
        System.out.println("爬取完毕");
        long endTime = System.currentTimeMillis();
        System.out.println("一共花费时间:"+(endTime - startTime));
    }
}
