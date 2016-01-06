package com.android.providers.downloads.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DownloadAdapter
  extends CursorAdapter
{
  private Cursor mCursor;
  private final int mDateColumnId;
  private DateFormat mDateFormat;
  private final int mDescriptionColumnId;
  private final DownloadList mDownloadList;
  private final int mFileNameColumnId;
  private final int mIdColumnId;
  private final int mMediaTypeColumnId;
  private final int mReasonColumnId;
  private Resources mResources;
  private final int mStatusColumnId;
  private DateFormat mTimeFormat;
  private final int mTitleColumnId;
  private final int mTotalBytesColumnId;
  
  private String getDateString()
  {
    Date localDate = new Date(this.mCursor.getLong(this.mDateColumnId));
    if (localDate.before(getStartOfToday())) {
      return this.mDateFormat.format(localDate);
    }
    return this.mTimeFormat.format(localDate);
  }
  
  private String getSizeText()
  {
    long l = this.mCursor.getLong(this.mTotalBytesColumnId);
    String str = "";
    if (l >= 0L) {
      str = Formatter.formatFileSize(this.mContext, l);
    }
    return str;
  }
  
  private Date getStartOfToday()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.set(11, 0);
    localGregorianCalendar.set(12, 0);
    localGregorianCalendar.set(13, 0);
    localGregorianCalendar.set(14, 0);
    return localGregorianCalendar.getTime();
  }
  
  private int getStatusStringId(int paramInt)
  {
    int i = 2131165192;
    switch (paramInt)
    {
    default: 
      throw new IllegalStateException("Unknown status: " + this.mCursor.getInt(this.mStatusColumnId));
    case 16: 
      i = 2131165194;
    case 1: 
    case 2: 
      return i;
    case 8: 
      return 2131165193;
    }
    switch (this.mCursor.getInt(this.mReasonColumnId))
    {
    default: 
      return i;
    }
    return 2131165191;
  }
  
  private void retrieveAndSetIcon(View paramView)
  {
    String str = this.mCursor.getString(this.mMediaTypeColumnId);
    ImageView localImageView = (ImageView)paramView.findViewById(2131296262);
    localImageView.setVisibility(4);
    if (str == null) {
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setDataAndType(Uri.fromParts("file", "", null), str);
    PackageManager localPackageManager = this.mContext.getPackageManager();
    List localList = localPackageManager.queryIntentActivities(localIntent, 65536);
    if (localList.size() == 0) {
      localImageView.setImageResource(2130837504);
    }
    for (;;)
    {
      localImageView.setVisibility(0);
      return;
      localImageView.setImageDrawable(((ResolveInfo)localList.get(0)).activityInfo.loadIcon(localPackageManager));
    }
  }
  
  private void setTextForView(View paramView, int paramInt, CharSequence paramCharSequence)
  {
    ((TextView)paramView.findViewById(paramInt)).setText(paramCharSequence);
  }
  
  public void bindView(View paramView, int paramInt)
  {
    if (!(paramView instanceof DownloadItem)) {
      return;
    }
    long l = this.mCursor.getLong(this.mIdColumnId);
    ((DownloadItem)paramView).setData(l, paramInt, this.mCursor.getString(this.mFileNameColumnId), this.mCursor.getString(this.mMediaTypeColumnId));
    retrieveAndSetIcon(paramView);
    String str1 = this.mCursor.getString(this.mTitleColumnId);
    if (str1.isEmpty()) {
      str1 = this.mResources.getString(2131165188);
    }
    setTextForView(paramView, 2131296263, str1);
    setTextForView(paramView, 2131296264, this.mCursor.getString(this.mDescriptionColumnId));
    setTextForView(paramView, 2131296265, getSizeText());
    int i = this.mCursor.getInt(this.mStatusColumnId);
    if (i == 8) {}
    for (String str2 = getDateString();; str2 = this.mResources.getString(getStatusStringId(i)))
    {
      setTextForView(paramView, 2131296266, str2);
      ((DownloadItem)paramView).getCheckBox().setChecked(this.mDownloadList.isDownloadSelected(l));
      return;
    }
  }
  
  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    bindView(paramView, paramCursor.getPosition());
  }
  
  public View newView()
  {
    DownloadItem localDownloadItem = (DownloadItem)LayoutInflater.from(this.mDownloadList).inflate(2130968577, null);
    localDownloadItem.setDownloadListObj(this.mDownloadList);
    return localDownloadItem;
  }
  
  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return newView();
  }
}


/* Location:              /Users/charlesfoxw/Desktop/dex2jar-0.0.9.15/classes-dex2jar.jar!/com/android/providers/downloads/ui/DownloadAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */