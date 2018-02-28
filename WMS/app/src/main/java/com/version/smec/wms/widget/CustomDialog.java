package com.version.smec.wms.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.smec.wms.R;
import com.smec.wms.databinding.ItemConfimDialogBinding;

/**
 * Created by xupeizuo on 2017/9/15.
 */

public class CustomDialog extends Dialog{

    private Context mContext;
    private ItemConfimDialogBinding itemConfimDialogBinding;

    public interface ButtonListener{
        void cancellistener();
        void makeSurelistener();
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
        this.setCanceledOnTouchOutside(true);
        itemConfimDialogBinding=DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_confim_dialog,null,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(itemConfimDialogBinding.getRoot());
    }

    public CustomDialog setContent(String content){
        if(content == null){
            return this;
        }
        itemConfimDialogBinding.popwindowContent.setText(content);
        return this;
    }

    public CustomDialog buttonlistener(final ButtonListener buttonListener){
        itemConfimDialogBinding.tvNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.cancellistener();
            }
        });
        itemConfimDialogBinding.tvPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.makeSurelistener();
            }
        });
        return this;
    }
}
