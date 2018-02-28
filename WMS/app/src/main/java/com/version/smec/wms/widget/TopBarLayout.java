package com.version.smec.wms.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smec.wms.R;


/**
 * Created by wei on 2016/11/03.
 */

public class TopBarLayout extends RelativeLayout {
    // 自定义属性，与xml文件中的属性对应
    private String left;
    private String title;
    private String titleHeader;
    private String rightFirst;
    private String rightSecond;
    private Drawable leftIcon;
    private Drawable rightFirstIcon;
    private Drawable rightSecondIcon;

    // layout中的控件
    private TextView tvTextLeft;
    private TextView tvTextTitle;
    private TextView tvTextViewTitleHeader;
    private TextView tvTextRightFirst;
    private TextView tvTextRightSecond;
    private LinearLayout back;

    // 内部接口
    private TopBarListener topBarListener;

    public TopBarLayout(Context context) {
        super(context);
    }

    public TopBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.layout_top_bar, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TopBarLayout);
        this.setProps(typedArray);
        typedArray.recycle();
        this.init();
        this.setAttrs();
    }

    /**
     * 从xml文件中得到属性值
     * 给该类成员属性赋值
     * @param typedArray
     */
    private void setProps(final TypedArray typedArray){
        left = typedArray.getString(R.styleable.TopBarLayout_text_left);
        title = typedArray.getString(R.styleable.TopBarLayout_text_title);
        titleHeader = typedArray.getString(R.styleable.TopBarLayout_text_title_header);
        rightFirst = typedArray.getString(R.styleable.TopBarLayout_text_right_first);
        rightSecond = typedArray.getString(R.styleable.TopBarLayout_text_right_second);
        leftIcon = typedArray.getDrawable(R.styleable.TopBarLayout_icon_left);
        rightFirstIcon = typedArray.getDrawable(R.styleable.TopBarLayout_icon_right_first);
        rightSecondIcon = typedArray.getDrawable(R.styleable.TopBarLayout_icon_right_second);
    }

    /**
     * 控件初始化
     * 为控件增加点击事件
     */
    private void init() {
        // 根据id得到控件
        tvTextLeft = (TextView) findViewById(R.id.tv_text_left);
        tvTextTitle = (TextView) findViewById(R.id.tv_text_title);
        tvTextViewTitleHeader = (TextView) findViewById(R.id.tv_text_title_header);
        tvTextRightFirst = (TextView) findViewById(R.id.tv_text_right1);
        tvTextRightSecond = (TextView) findViewById(R.id.tv_text_right2);
        back = (LinearLayout) findViewById(R.id.back);

        // 为控件设置相应的监听接口
        back.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topBarListener !=null){
                    topBarListener.setOnLeftClickListener();
                }
            }
        });

        tvTextRightFirst.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topBarListener !=null){
                    topBarListener.setOnRight1ClickListener();
                }
            }
        });

        tvTextRightSecond.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topBarListener !=null){
                    topBarListener.setOnRight2ClickListener();
                }
            }
        });
    }

    /**
     * 给控件设置相应的值
     */
    private void setAttrs(){
        tvTextTitle.setText(title);
        tvTextViewTitleHeader.setText(titleHeader);

        if (leftIcon != null) {
            tvTextLeft.setText(null);
            leftIcon.setBounds(0, 0,
                    (int)getResources().getDimension(R.dimen.px50dp),
                    (int)getResources().getDimension(R.dimen.px50dp));
            tvTextLeft.setCompoundDrawables(leftIcon, null, null, null);
        } else {
            tvTextLeft.setText(left);
        }

        if (rightFirstIcon != null) {
            tvTextRightFirst.setText(null);
            rightFirstIcon.setBounds(0, 0,
                    (int)getResources().getDimension(R.dimen.px50dp),
                    (int)getResources().getDimension(R.dimen.px50dp));
            tvTextRightFirst.setCompoundDrawables(rightFirstIcon, null, null, null);
        } else {
            tvTextRightFirst.setText(rightFirst);
        }

        /**
         *
         * 修改：李月辉
         * 有bug ----不设置firsticon 直接设置secondicon 报空指针异常
         * 修改时间：2017-08-28 上午
         */

        if (rightSecondIcon != null) {
            tvTextRightSecond.setText(null);
            rightSecondIcon.setBounds(0, 0,
                    (int)getResources().getDimension(R.dimen.px50dp),
                    (int)getResources().getDimension(R.dimen.px50dp));
            tvTextRightSecond.setCompoundDrawables(rightSecondIcon, null, null, null);
        } else {
            tvTextRightSecond.setText(rightSecond);
        }
    }

    /**
     * 定义TopBar的监听接口
     * 暴露出相应的方法
     */
    public interface TopBarListener{
        /**
         * 左边按钮的点击事件
         */
         void setOnLeftClickListener();

        /**
         * 右1按钮的点击事件
         */
         void setOnRight1ClickListener();

        /**
         * 右2按钮的点击事件
         */
         void setOnRight2ClickListener();
    }

    /**
     * 暴露出一个设置监听的方法
     * @param topBarListener
     */
    public void setTopBarListener(TopBarListener topBarListener){
        this.topBarListener = topBarListener;
    }

    // getter
    public TextView getTvTextLeft() {
        return tvTextLeft;
    }

    public TextView getTvTextTitle() {
        return tvTextTitle;
    }

    public TextView gettvTextRightFirst() {
        return tvTextRightFirst;
    }

    public TextView gettvTextRightSecond() {
        return tvTextRightSecond;
    }
}
