package com.android.providers.downloads.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.view.ActionMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DownloadList
  extends Activity
{
  private ActionMode mActionMode;
  private MyContentObserver mContentObserver = new MyContentObserver();
  private Cursor mCurrentCursor;
  private ListView mCurrentView;
  private boolean mCurrentViewIsExpandableListView = false;
  private MyDataSetObserver mDataSetObserver = new MyDataSetObserver(null);
  private ExpandableListView mDateOrderedListView;
  //private DateSortedDownloadAdapter mDateSortedAdapter;
  private Cursor mDateSortedCursor;
  private View mEmptyView;
  private int mIdColumnId;
  private boolean mIsSortedBySize = false;
  private AlertDialog mQueuedDialog;
  private Long mQueuedDownloadId = null;
  private int mReasonColumndId;
  String mSelectedCountFormat;
  private final Map<Long, SelectionObjAttrs> mSelectedIds = new HashMap();
  private ListView mSizeOrderedListView;
  private Cursor mSizeSortedCursor;
  private Button mSortOption;
  private int mStatusColumnId;
  
  private ListView activeListView()
  {
    if (this.mIsSortedBySize)
    {
      this.mCurrentCursor = this.mSizeSortedCursor;
      this.mCurrentView = this.mSizeOrderedListView;
      setTitle(2131165186);
      this.mSortOption.setText(2131165190);
    }
    for (this.mCurrentViewIsExpandableListView = false;; this.mCurrentViewIsExpandableListView = true)
    {
      if (this.mActionMode != null) {
        this.mActionMode.finish();
      }
      return this.mCurrentView;
      this.mCurrentCursor = this.mDateSortedCursor;
      this.mCurrentView = this.mDateOrderedListView;
      setTitle(2131165185);
      this.mSortOption.setText(2131165189);
    }
  }
  
  private void checkSelectionForDeletedEntries()
  {
    HashSet localHashSet = new HashSet();
    this.mDateSortedCursor.moveToFirst();
    while (!this.mDateSortedCursor.isAfterLast())
    {
      localHashSet.add(Long.valueOf(this.mDateSortedCursor.getLong(this.mIdColumnId)));
      this.mDateSortedCursor.moveToNext();
    }
    Iterator localIterator = this.mSelectedIds.keySet().iterator();
    while (localIterator.hasNext()) {
      if (!localHashSet.contains(localIterator.next())) {
        localIterator.remove();
      }
    }
  }
  
  private void chooseListToShow()
  {
    this.mDateOrderedListView.setVisibility(8);
    this.mSizeOrderedListView.setVisibility(8);
    if ((this.mDateSortedCursor == null) || (this.mDateSortedCursor.getCount() == 0))
    {
      this.mEmptyView.setVisibility(0);
      this.mSortOption.setVisibility(8);
    }
    for (;;)
    {
      if (this.mActionMode != null) {
        setActionModeTitle(this.mActionMode);
      }
      return;
      this.mEmptyView.setVisibility(8);
      this.mSortOption.setVisibility(0);
      ListView localListView = activeListView();
      localListView.setVisibility(0);
      localListView.invalidateViews();
    }
  }
  
  private void ensureSomeGroupIsExpanded()
  {
    this.mDateOrderedListView.post(new Runnable()
    {
      public void run()
      {
        if (DownloadList.this.mDateSortedAdapter.getGroupCount() == 0) {
          return;
        }
        for (int i = 0;; i++)
        {
          if (i >= DownloadList.this.mDateSortedAdapter.getGroupCount()) {
            break label50;
          }
          if (DownloadList.this.mDateOrderedListView.isGroupExpanded(i)) {
            break;
          }
        }
        label50:
        DownloadList.this.mDateOrderedListView.expandGroup(0);
      }
    });
  }
  
  private boolean haveCursors()
  {
    return (this.mDateSortedCursor != null) && (this.mSizeSortedCursor != null);
  }
  
  private boolean isPausedForWifi(Cursor paramCursor)
  {
    return paramCursor.getInt(this.mReasonColumndId) == 3;
  }
  
  private boolean moveToDownload(long paramLong)
  {
    this.mDateSortedCursor.moveToFirst();
    while (!this.mDateSortedCursor.isAfterLast())
    {
      if (this.mDateSortedCursor.getLong(this.mIdColumnId) == paramLong) {
        return true;
      }
      this.mDateSortedCursor.moveToNext();
    }
    return false;
  }
  
  private void refresh()
  {
    this.mDateSortedCursor.requery();
    this.mSizeSortedCursor.requery();
  }
  
  ListView getCurrentView()
  {
    return this.mCurrentView;
  }
  
  ExpandableListView getExpandableListView()
  {
    return this.mDateOrderedListView;
  }
  
  void handleDownloadsChanged()
  {
    checkSelectionForDeletedEntries();
    if ((this.mQueuedDownloadId != null) && (moveToDownload(this.mQueuedDownloadId.longValue())) && ((this.mDateSortedCursor.getInt(this.mStatusColumnId) != 4) || (!isPausedForWifi(this.mDateSortedCursor)))) {
      this.mQueuedDialog.cancel();
    }
  }
  
  public boolean isDownloadSelected(long paramLong)
  {
    return this.mSelectedIds.containsKey(Long.valueOf(paramLong));
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = new Intent("android.provider.action.MANAGE_ROOT");
    localIntent.setData(DocumentsContract.buildRootUri("com.android.providers.downloads.documents", "downloads"));
    startActivity(localIntent);
    finish();
  }
  
  public void onDownloadSelectionChanged(long paramLong, boolean paramBoolean, String paramString1, String paramString2)
  {
    if (paramBoolean)
    {
      this.mSelectedIds.put(Long.valueOf(paramLong), new SelectionObjAttrs(paramString1, paramString2));
      return;
    }
    this.mSelectedIds.remove(Long.valueOf(paramLong));
  }
  
  protected void onPause()
  {
    super.onPause();
    if (haveCursors())
    {
      this.mDateSortedCursor.unregisterContentObserver(this.mContentObserver);
      this.mDateSortedCursor.unregisterDataSetObserver(this.mDataSetObserver);
    }
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.mIsSortedBySize = paramBundle.getBoolean("isSortedBySize");
    this.mSelectedIds.clear();
    long[] arrayOfLong = paramBundle.getLongArray("download_ids");
    String[] arrayOfString1 = paramBundle.getStringArray("filenames");
    String[] arrayOfString2 = paramBundle.getStringArray("mimetypes");
    if ((arrayOfLong != null) && (arrayOfLong.length > 0)) {
      for (int i = 0; i < arrayOfLong.length; i++) {
        this.mSelectedIds.put(Long.valueOf(arrayOfLong[i]), new SelectionObjAttrs(arrayOfString1[i], arrayOfString2[i]));
      }
    }
    chooseListToShow();
  }
  
  protected void onResume()
  {
    super.onResume();
    if (haveCursors())
    {
      this.mDateSortedCursor.registerContentObserver(this.mContentObserver);
      this.mDateSortedCursor.registerDataSetObserver(this.mDataSetObserver);
      refresh();
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("isSortedBySize", this.mIsSortedBySize);
    int i = this.mSelectedIds.size();
    if (i == 0) {
      return;
    }
    long[] arrayOfLong = new long[i];
    String[] arrayOfString1 = new String[i];
    String[] arrayOfString2 = new String[i];
    int j = 0;
    Iterator localIterator = this.mSelectedIds.keySet().iterator();
    while (localIterator.hasNext())
    {
      long l = ((Long)localIterator.next()).longValue();
      arrayOfLong[j] = l;
      SelectionObjAttrs localSelectionObjAttrs = (SelectionObjAttrs)this.mSelectedIds.get(Long.valueOf(l));
      arrayOfString1[j] = localSelectionObjAttrs.getFileName();
      arrayOfString2[j] = localSelectionObjAttrs.getMimeType();
      j++;
    }
    paramBundle.putLongArray("download_ids", arrayOfLong);
    paramBundle.putStringArray("filenames", arrayOfString1);
    paramBundle.putStringArray("mimetypes", arrayOfString2);
  }
  
  void setActionModeTitle(ActionMode paramActionMode)
  {
    int i = this.mSelectedIds.size();
    if (i > 0)
    {
      String str = this.mSelectedCountFormat;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(this.mCurrentCursor.getCount());
      paramActionMode.setTitle(String.format(str, arrayOfObject));
      return;
    }
    paramActionMode.setTitle("");
  }
  
  private class MyContentObserver
    extends ContentObserver
  {
    public MyContentObserver()
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      DownloadList.this.handleDownloadsChanged();
    }
  }
  
  private class MyDataSetObserver
    extends DataSetObserver
  {
    private MyDataSetObserver() {}
    
    public void onChanged()
    {
      if (DownloadList.this.mSelectedIds.size() > 0) {
        return;
      }
      DownloadList.this.chooseListToShow();
      DownloadList.this.ensureSomeGroupIsExpanded();
    }
  }
  
  private static class SelectionObjAttrs
  {
    private String mFileName;
    private String mMimeType;
    
    SelectionObjAttrs(String paramString1, String paramString2)
    {
      this.mFileName = paramString1;
      this.mMimeType = paramString2;
    }
    
    String getFileName()
    {
      return this.mFileName;
    }
    
    String getMimeType()
    {
      return this.mMimeType;
    }
  }
}


/* Location:              /Users/charlesfoxw/Desktop/dex2jar-0.0.9.15/classes-dex2jar.jar!/com/android/providers/downloads/ui/DownloadList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */