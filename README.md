# LightningBrowserX86Desktop

This is an Android application running on Android x86 systems (tested on version 4.4 kitkat).
The purpose of this project is to develop a desktop-friendly web browser running on Android x86 desktop.
The design is to fit the use of mouse pointer and physical keyboard.

Here is the list of the changes we made to the code
---------------------------------
Lightning Browser:
---------------------------------

Re-arrange the action bar:
    Adding a layout xml file: address_bar.xml. It has the layout of forward and backward buttons, Adding bookmark button, and the search bar. The size is optimized for looking.

    For the back/Forward buttons:
    Inside BrowserActivity.java:
    RelativeLayout back = (RelativeLayout) findViewById(R.id.action_back);
        RelativeLayout forward = (RelativeLayout) findViewById(R.id.action_forward);
        if (back != null) {
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mCurrentView != null) {
                        if (mCurrentView.canGoBack())
                            mCurrentView.goBack();
                    }
                }

            });
        }
        if (forward != null) {
            forward.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mCurrentView != null) {
                        if (mCurrentView.canGoForward()) {
                            mCurrentView.goForward();
                        }
                    }
                }

            });
        }

Bookmark Bar:
    Adding a file that is bookmark_bar.xml, which includes one instance of a bookmark (a favicon plus a name title) in a Linearlayout.

    Include the dependency of HorizontalListView into the project library. This is in order to use comparing with the ListView of android, which only supports the vertial lists.

    Inside activity_main.xml, add the view of bookmark_bar below the action_bar. Using a image "star" that informing the bookmarks and a HorizontalListView Layout.

    In BrowserActivity.java, add:

        mBookmarkList = mBookmarkManager.getBookmarks(true);
       mBookmarkAdapter = new BookmarkViewAdapter(this, R.layout.bookmark_bar, mBookmarkList);

       //BookMark Bar
       mBookmarkBar.setAdapter(mBookmarkAdapter);
       mBookmarkBar.setOnItemClickListener(new BookmarkItemClickListener());
       mBookmarkBar.setOnItemLongClickListener(new BookmarkItemLongClickListener());

   Also, for the Adding-bookmark button on the action bar, we added:

       ImageView addBookmark = (ImageView) findViewById(R.id.action_new_bookmark);
        addBookmark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCurrentView.getUrl().startsWith(Constants.FILE)) {
                    HistoryItem bookmark = new HistoryItem(mCurrentView.getUrl(),
                            mCurrentView.getTitle());
                    if (mBookmarkManager.addBookmark(bookmark)) {
                        mBookmarkList.add(bookmark);
                        Collections.sort(mBookmarkList, new SortIgnoreCase());
                        notifyBookmarkDataSetChanged();
                        mSearchAdapter.refreshBookmarks();
                    }
                }
            }
        });


   The resources of bookmarks are from the BookmarkManager.

Tab Bar:
    Move the tabs Listview from the left drawer to the Tab Bar.
    Still using the HorizontalListView, Similar with the bookmark Bar.

    Inside the activity_main.xml, add the layout of the tab bar. A imageView which is the Add-new-tab button and followed by the horizontal list.

    In the BrowserActivity.java, add:
        mTitleAdapter = new LightningViewAdapter(this, R.layout.tab_list_item, mWebViews);
       //Tab Bar
       mTabBar.setAdapter(mTitleAdapter);
       mTabBar.setOnItemClickListener(new DrawerItemClickListener());
       mTabBar.setOnItemLongClickListener(new DrawerItemLongClickListener());

Homepage:
    Add a layout: SpeedDails.java. This class uses HTML language inside java strings to implement the layout view of the homepage., After that, it combines different pieces of HTML strings together and display it on the screen. Using a counter to display the top 9 pages from the History:

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

   The PART1 ~ PART 4 are all Strings with HTML syntax.


   Since we need to get the current history when the browser starts, we added the Context object into the constructor of LightningView.java to pass the current context into this class so that the homepage is ininialized here:

   mDefaultUserAgent = mWebView.getSettings().getUserAgentString();
        mSettings = mWebView.getSettings();
        initializeSettings(mWebView.getSettings(), activity);
        initializePreferences(activity);

        if (url != null) {
            if (!url.trim().isEmpty()) {
                mWebView.loadUrl(url);
            } else {
                // don't load anything, the user is looking for a blank tab
            }
        } else {
            if (mHomepage.startsWith("about:home")) {
                mWebView.loadUrl(SpeedDials.getSDPage(context));
            } else if (mHomepage.startsWith("about:bookmarks")) {
                mBrowserController.openBookmarkPage(mWebView);
            } else {
                mWebView.loadUrl(mHomepage);
            }
        }

Download Manager:
    In this part, we need to use intent to call the DownloadManager inside the Android API so that it can be running on top of the browser activity. When it is called, the Broser is onPause and the DownloadManager is poped up, pressing by anywhere on the screen outside the range of the Download UI, it goes back to the browser again (onResume).

    import android.app.DownloadManager;

    Inside the onClick handler of the right drawer, add:

    case R.id.displayDownload:
                       Intent intent = new Intent();
                       intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                       startActivity(intent);
               return true;

UI Optimization:

    The rest thing is to optimiaze the UI of the browser, change the SpeedDials.java, which is the homepage (The HTML strings). Add some effects on it, showing shadows and boxes of tiles. Resize them so that it's better for desktop usage. Adding a wallpaper in it.
    Change the Bitmap at the very left of the browser into an Lightning icon., change the left drawer into the information of this modified browser.
    Changed the about_settings.xml so that it displays the list we want.
