package com.version.smec.wms.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import com.flyco.dialog.listener.OnBtnRightClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;

import static com.smec.wms.android.application.WmsApplication.getContext;

/**
 * Created by xupeizuo on 2017/9/11.
 */

public class WmsConstant {

    /**
     * 下拉刷新头部
     */
    private static SinaRefreshView sinaRefreshView;

    private static ProgressDialog progressDialog;

    public static SinaRefreshView getSinaRefreshView() {
        sinaRefreshView=new SinaRefreshView(getContext());
        sinaRefreshView.setArrowResource(R.drawable.smec_refresh);
        sinaRefreshView.setTextColor(getContext().getResources().getColor(R.color.darkgray));
        return sinaRefreshView;
    }

    public static ProgressDialog getProgressDialog(){
        if(progressDialog == null){
            progressDialog=new ProgressDialog(WmsApplication.getContext());
        }
        return progressDialog;
    }

    public static NormalDialog getInstance(Context mContext, String title){

        final NormalDialog normalDialog=new NormalDialog(mContext);
        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#383838"))
                .cornerRadius(5)
                .content(title)
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))
                .dividerColor(Color.parseColor("#222222"))
                .btnText("取消","确定")
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))
                .btnColorPress(Color.parseColor("#2B2B2B"))
                .widthScale(0.85f)
                .dismiss();

        normalDialog.setOnBtnRightClickL(new OnBtnRightClickL() {
            @Override
            public void onBtnRightClick() {
                normalDialog.dismiss();
            }
        });

        return normalDialog;
    }

}
