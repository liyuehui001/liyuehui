package com.smec.wms.android.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.util.ToastUtil;

/**
 * Created by apple on 2016/10/30.
 */

public class CopyTextView extends TextView {

    @IdRes
    private int copyId ;

    public CopyTextView(Context context) {
        super(context);
    }

    public CopyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CopyTextView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void init(final Context context ,AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CopyTextView);
        copyId = a.getResourceId(R.styleable.CopyTextView_copy_text,0);
        if (copyId != 0) {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup viewGroup =(ViewGroup)(view.getParent());
                    try {
                        View copyView = viewGroup.findViewById(copyId);
                        if (copyView != null && copyView instanceof TextView) {
                            TextView textView = (TextView)copyView ;
                            ClipboardManager myClipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData myClip = ClipData.newPlainText("text", textView.getText());
                            myClipboard.setPrimaryClip(myClip);
                            ToastUtil.show(context,"已添加到粘贴板");
                            return;
                        }
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

}
