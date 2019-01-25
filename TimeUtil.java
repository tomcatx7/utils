package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TimeUtil {

    public static String format(Long time) {
        if (time == null) {
            return null;
        } else {
            long hour = time / (60 * 60 * 1000);
            long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
            long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
            return (hour == 0 ? "00" : (hour > 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute > 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second > 10 ? second : ("0" + second)));
        }
    }

    /**
     * 时间为秒
     * Long类型时间转换成视频时长
     */
    public static String formatTime(Long time) {
        if (time == null) {
            return null;
        } else {
            long hour = time / (60 * 60);
            long minute = (time - hour * 60 * 60) / 60;
            long second = time - hour * 60 * 60 - minute * 60;
            return (hour == 0 ? "00" : (hour > 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute > 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second > 10 ? second : ("0" + second)));
        }
    }

    public static void main(String[] args) {
        Executors.newFixedThreadPool(5);
                Long time = 73796L;
        String format = TimeUtil.format(time);
        System.out.println(format);
    }
}
