package acr.browser.lightning;

/**
 * Created by charlesfoxw on 4/19/15.
 */

import android.content.Context;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class SpeedDials {
 

    public static final String FILENAME = "history.html";

    public static final String HEADING = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta content=\"en-us\" http-equiv=\"Content-Language\" /><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"><title>"
            + BrowserApp.getAppContext().getString(R.string.action_new_tab)
            + "</title></head><style>body { max-width:100%; min-height:100%;}#content {width:100%; max-width:800dp; margin:50 auto; text-align:center;}.box { vertical-align:middle;text-align:center;position:relative;display: inline-block; height: 100dp; width: 800dp; margin: 18px; background-color:#eee; border:5px outset orange;box-shadow: 7px 7px rgba( 0, 0, 0, 0.2 );font-family: Arial;color: #0b3861;font-size: 16px;-moz-border-radius: 2px;-webkit-border-radius: 2px;border-radius: 2px;}.stuff {height: 155px; width: 800dp;vertical-align:middle;horizontal-align:middle;text-align:center;display: table-cell;}p.ellipses {width:300px; white-space: nowrap; overflow: hidden;text-align:center;margin:15px; text-overflow: ellipsis;-o-text-overflow: ellipsis; -ms-text-overflow: ellipsis;}.box a { width: 100%; height: 100%; position: absolute; left: 0; top: 0;} </style><body background=\"file:///android_res/drawable/background_newtab.jpg\"> <div id=\"content\">";

    public static final String PART1 = "<div class=\"box\"><a href=\"";

    public static final String PART2 = "\" ></a><div class=\"stuff\" ><img height=\"30\" width=\"30\" src='http://www.google.com/s2/favicons?domain=";

    public static final String PART3 = "' /><p class=\"ellipses\">";

    public static final String PART4 = "</p></div></div>";

    public static final String END = "</div></body></html>";
    public static String getSDPage(Context context) {
        int count = 9;
        String historyHtml = SpeedDials.HEADING;
        List<HistoryItem> historyList = getWebHistory(context);

        Iterator<HistoryItem> it = historyList.iterator();
        HistoryItem helper;
        while (it.hasNext() && count > 0) {
            helper = it.next();
            historyHtml += SpeedDials.PART1 + helper.getUrl() + SpeedDials.PART2
                    + helper.getUrl() + SpeedDials.PART3 + helper.getTitle() + SpeedDials.PART4;
            count--;
        }

        historyHtml += SpeedDials.END;
        File historyWebPage = new File(context.getFilesDir(), FILENAME);
        try {
            FileWriter historyWriter = new FileWriter(historyWebPage, false);
            historyWriter.write(historyHtml);
            historyWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.FILE + historyWebPage;
    }

    private static List<HistoryItem> getWebHistory(Context context) {
        HistoryDatabaseHandler databaseHandler = new HistoryDatabaseHandler(context);
        return databaseHandler.getLastHundredItems();
    }
}
