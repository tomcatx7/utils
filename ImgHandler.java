package handler;

import filter.Links;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.FileDownLoadUtil;

import java.io.File;
import java.io.IOException;

public class ImgHandler extends AbstractHanlder {
    private Links imgLinks = new Links();

    protected void preHandler(Document doc) {
        String cssSelector = "img";
        Elements elements = doc.select(cssSelector);
        for (Element element : elements) {
            System.out.println(element);
            if (element.hasAttr("href")) {
                imgLinks.addVisitedUrl(element.attr("abs:href"));
            }
            if (element.hasAttr("src")) {
                imgLinks.addVisitedUrl(element.attr("abs:src"));
            }
            if (imgLinks.visitedUrlQueue.size() >= 1000) break;
        }
    }

    protected void doHandler() throws IOException {
        FileDownLoadUtil.fileDown2(imgLinks.visitedUrlQueue,"D:\\img\\","png");
    }

    public static void main(String[] args) throws IOException {
        String htmlFilepath = "D:\\crawl\\html\\2019\\01\\24";
        File htmlFile = new File(htmlFilepath);
        if (htmlFile.exists() && htmlFile.isDirectory()) {
            new ImgHandler().handler(htmlFile);
        }
    }
}
