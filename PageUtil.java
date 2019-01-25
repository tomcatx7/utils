package util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import page.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class PageUtil {

    public static Elements select(Page page, String cssSelector) {
        return page.getDoc().select(cssSelector);
    }

    public static Element selectOne(Page page, String cssSelector, int index) {
        Elements elements = select(page, cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = elements.size() + index;
        }
        return elements.get(realIndex);
    }

    public static HashSet<String> getLinks(Page page, String cssSelector) {
        HashSet<String> links = new HashSet();
        Elements eles = select(page, cssSelector);
        Iterator<Element> iterator = eles.iterator();
        while (iterator.hasNext()) {
            Element ele = iterator.next();
            if (ele.hasAttr("href")) {
                // ele.attr(""abs:href"") 可以筛选和补全url
                links.add(ele.attr("abs:href"));
            } else {
                if (ele.hasAttr("src")) {
                    links.add(ele.attr("abs:src"));
                }
            }
        }
        return links;
    }

    public static ArrayList getAttrResult(Page page,String cssSelector,String attr){
        ArrayList result = new ArrayList<String>();
        Elements eles = select(page, cssSelector);
        Iterator<Element> iterator = eles.iterator();
        while (iterator.hasNext()){
            Element next = iterator.next();
            if (next.hasAttr(attr)){
                result.add(next.attr(attr));
            }
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
