package com.version.smec.wms.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smec.wms.R;

/**
 * Created by 小黑 on 2017/8/15.
 */
public class ImageTextView extends LinearLayout{

    private String text;
    private Drawable image_left_bg;
    private Drawable image_right_bg;
    private TextView textview;
    private ImageView iv_Left_icon;
    private ImageView iv_right_icon;



    public ImageTextView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_imge_text_view,this,true);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ImageTextView);
        this._setProp(typedArray);
        typedArray.recycle();
        this._init();
        this._setAttrs();


    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void _setProp(TypedArray typedArray) {
        text = typedArray.getString(R.styleable.ImageTextView_view_text);
        image_left_bg = typedArray.getDrawable(R.styleable.ImageTextView_image_left_bg);
        image_right_bg = typedArray.getDrawable(R.styleable.ImageTextView_image_right_bg);
    }

    private void _init(){
        textview = (TextView)findViewById(R.id.tv_text);
        iv_Left_icon = (ImageView)findViewById(R.id.iv_left_icon_image);
        iv_right_icon = (ImageView)findViewById(R.id.iv_right_icon_image);

    }

    private void _setAttrs(){
        if (text!=null){
            textview.setText(text);
        }
        if (image_left_bg!=null){
            iv_Left_icon.setVisibility(VISIBLE);
            iv_Left_icon.setImageDrawable(image_left_bg);
        }else{
            iv_Left_icon.setVisibility(GONE);
        }
        if (image_right_bg!=null){
            iv_right_icon.setVisibility(VISIBLE);
            iv_right_icon.setImageDrawable(image_right_bg);
        }else{
            iv_right_icon.setVisibility(GONE);
        }
    }

    public void setText(String str){
        textview.setText(str);
    }

    public void setLeftImageBg(Drawable drawable){
        iv_Left_icon.setVisibility(VISIBLE);
        iv_Left_icon.setImageDrawable(drawable);
        iv_right_icon.setVisibility(GONE);
    }

    public void setRightImageBg(Drawable drawable){
        iv_right_icon.setVisibility(VISIBLE);
        iv_right_icon.setImageDrawable(drawable);
        iv_Left_icon.setVisibility(GONE);
    }



    public String getText(){
        return textview.getText().toString();
    }



}
