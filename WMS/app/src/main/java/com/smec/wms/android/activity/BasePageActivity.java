package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smec.wms.R;

/**
 * Created by Adobe on 2016/1/28.
 */
public abstract class BasePageActivity extends BaseActivity  {

    private TextView tvPageTitle;
    private ImageButton backAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.beforeCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setContentView(this.getContentViewId());
        this.initPageView();
        this.afterCreate(savedInstanceState);
    }

    protected abstract int getContentViewId();
    protected void beforeCreate(Bundle savedInstanceState){};
    protected void afterCreate(Bundle savedInstanceState){};

    private void initPageView(){
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_common_title);
        this.tvPageTitle=(TextView)this.findViewById(R.id.layout_title);
        this.backAction=(ImageButton)this.findViewById(R.id.common_back);
        this.backAction.setOnClickListener(this);
    }

    protected void setTitle(String title){
        this.tvPageTitle.setText(title);
    }


    @Override
    protected void clickActionHandler(View v) {
        super.clickActionHandler(v);
        if(v.getId()==R.id.common_back && backExecute()){
            this.finish();
        }
    }

    protected void enableBackAction(){
        this.backAction.setVisibility(View.VISIBLE);
    }

    public void showCompleteDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("操作完成")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }

    public boolean backExecute(){
        return true ;
    }
}
