package com.smec.wms.android.bean;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adobe on 2016/1/14.
 */
public class ScanTimeLine {
    private int textViewId;
    private int imageViewId;
    private TextView textView;
    private ImageView imageView;
    private String content;


    public ScanTimeLine(String content) {
        this.content=content;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }

    public int getTextViewId() {
        return textViewId;
    }

    public void setTextViewId(int textViewId) {
        this.textViewId = textViewId;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
