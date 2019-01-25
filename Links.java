package filter;

import java.util.HashSet;
import java.util.LinkedList;

public class Links {

    public  HashSet<String> visitedUrlQueue = new HashSet<String>();
    public  LinkedList<String> unVisitedUrlQueue = new LinkedList<String>();

    public  void addVisitedUrl(String url) {
        visitedUrlQueue.add(url);
    }

    public  void removeVisitedUrl(String url) {
        visitedUrlQueue.remove(url);
    }

    public  void addUnvisitedUrl(String url) {
        if (url != null && !url.trim().equals("") && !visitedUrlQueue.contains(url) && !unVisitedUrlQueue.contains(url)) {
            unVisitedUrlQueue.add(url);
        }
    }

    public  String getUnvisitedUrl() {
        if (!unVisitedUrlQueue.isEmpty()) {
            String url = unVisitedUrlQueue.removeFirst();
            return url;
        }else {
            return null;
        }
    }

    public  boolean visitedUrlQueueIsEmpty(){
        return visitedUrlQueue.isEmpty();
    }

    public  boolean unVisitedUrlQueueIsEmpty(){
        return unVisitedUrlQueue.isEmpty();
    }

}
