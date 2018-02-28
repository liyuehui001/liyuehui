package com.version.smec.wms.module.PendingApproval;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smec.wms.R;
import com.version.smec.wms.utils.CommonUtils;

/**
 * Created by Administrator on 2017/8/18.
 */
public class TodoProjectsCustomDialog extends Dialog {
    private Context context;
    private View customView;

    public TodoProjectsCustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        customView = inflater.inflate(R.layout.pengdingdialog, null);

        TextView tv = (TextView) customView.findViewById(R.id.tv_senior_title);
        CommonUtils.styleBoldTextView(tv);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(customView);
    }

    public View findViewById(int id) {
        return super.findViewById(id);
    }

    public View getCustomView() {
        return customView;
    }
}
