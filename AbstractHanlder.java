package handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public abstract class  AbstractHanlder implements Handler {

    public void handler(File html) throws IOException {
        if (html.isDirectory()){
            File[] files = html.listFiles();
            for (File file : files) {
                Document doc = Jsoup.parse(file,"utf-8");
                preHandler(doc);
            }
            doHandler();
        }
    }

    protected abstract void doHandler() throws IOException;

    protected abstract void preHandler(Document doc);
}
