package com.android.providers.downloads.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.ListView;

public class DownloadItem
  extends GridLayout
  implements Checkable
{
  private static float CHECKMARK_AREA = -1.0F;
  private CheckBox mCheckBox;
  private long mDownloadId;
  private DownloadList mDownloadList;
  private String mFileName;
  private boolean mIsInDownEvent = false;
  private String mMimeType;
  private int mPosition;
  
  public DownloadItem(Context paramContext)
  {
    super(paramContext);
    initialize();
  }
  
  public DownloadItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initialize();
  }
  
  public DownloadItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initialize();
  }
  
  private boolean inCheckArea(MotionEvent paramMotionEvent)
  {
    if (isLayoutRtl()) {
      if (paramMotionEvent.getX() <= getWidth() - CHECKMARK_AREA) {}
    }
    while (paramMotionEvent.getX() < CHECKMARK_AREA)
    {
      return true;
      return false;
    }
    return false;
  }
  
  private void initialize()
  {
    if (CHECKMARK_AREA == -1.0F) {
      CHECKMARK_AREA = getResources().getDimensionPixelSize(2131099648);
    }
  }
  
  public CheckBox getCheckBox()
  {
    return this.mCheckBox;
  }
  
  public boolean isChecked()
  {
    return this.mCheckBox.isChecked();
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mCheckBox = ((CheckBox)findViewById(2131296261));
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    boolean bool1 = false;
    switch (i)
    {
    }
    while (bool1)
    {
      postInvalidate();
      return bool1;
      boolean bool4 = inCheckArea(paramMotionEvent);
      bool1 = false;
      if (bool4)
      {
        this.mIsInDownEvent = true;
        bool1 = true;
        continue;
        this.mIsInDownEvent = false;
        bool1 = false;
        continue;
        boolean bool2 = this.mIsInDownEvent;
        bool1 = false;
        if (bool2)
        {
          boolean bool3 = inCheckArea(paramMotionEvent);
          bool1 = false;
          if (bool3)
          {
            toggle();
            sendAccessibilityEvent(1);
            bool1 = true;
          }
        }
        this.mIsInDownEvent = false;
      }
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setChecked(boolean paramBoolean)
  {
    this.mCheckBox.setChecked(paramBoolean);
    this.mDownloadList.onDownloadSelectionChanged(this.mDownloadId, this.mCheckBox.isChecked(), this.mFileName, this.mMimeType);
    this.mDownloadList.getCurrentView().setItemChecked(this.mPosition, this.mCheckBox.isChecked());
  }
  
  public void setData(long paramLong, int paramInt, String paramString1, String paramString2)
  {
    this.mDownloadId = paramLong;
    this.mPosition = paramInt;
    this.mFileName = paramString1;
    this.mMimeType = paramString2;
    if (this.mDownloadList.isDownloadSelected(paramLong)) {
      setChecked(true);
    }
  }
  
  public void setDownloadListObj(DownloadList paramDownloadList)
  {
    this.mDownloadList = paramDownloadList;
  }
  
  public void toggle()
  {
    if (!isChecked()) {}
    for (boolean bool = true;; bool = false)
    {
      setChecked(bool);
      return;
    }
  }
}


/* Location:              /Users/charlesfoxw/Desktop/dex2jar-0.0.9.15/classes-dex2jar.jar!/com/android/providers/downloads/ui/DownloadItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */