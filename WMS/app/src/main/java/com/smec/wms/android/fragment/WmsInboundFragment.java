package com.smec.wms.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smec.wms.R;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.activity.BoxBindActivity;
import com.smec.wms.android.activity.BoxOfflineScanActivity;
import com.smec.wms.android.activity.BoxOnlineScanActivity;
import com.smec.wms.android.activity.CheckMatnrActivity;
import com.smec.wms.android.activity.LoginActivity;
import com.smec.wms.android.activity.MoveActivity;
import com.smec.wms.android.activity.PackActivity;
import com.smec.wms.android.activity.PickActivity;
import com.smec.wms.android.activity.PutAwayOfflineActivity;
import com.smec.wms.android.activity.PutAwayScanActivity;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

public class WmsInboundFragment extends Fragment {
    private ViewGroup rdcInboundOnline;     //RDC_入库 在线
    private ViewGroup rdcInboundOffline;    //RDC_入库 离线
    private ViewGroup rdcPutaway;           //RDC_上架
    private ViewGroup rdcPutawayOffline;    //RDC_装货清点
//    private ViewGroup rdcCheckMantr;        //RDC_盘点
//    private ViewGroup rdcPack;              //RDC_装箱
//    private ViewGroup rdcMove;              //RDC_移库
//    private ViewGroup rdcBind;              //RDC_绑定箱子
//    private ViewGroup rdcPick;              //RDC_拣货
//    private ViewGroup vgLogout;             //USER_退出
    private View view;

    private View.OnClickListener actionHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rdc_inbound_online:
                    startRdcInboundOnlineActivity();
                    break;
                case R.id.rdc_inbound_offline:
                    startRdcInboundOfflineActivity();
                    break;
                case R.id.rdc_putaway:
                    startRdcPutawayActivity();
                    break;
                case R.id.rdc_checkmatnr:
                    startRdcCheckMantrActivity();
                    break;
                case R.id.rdc_pack:
                    startRdcPackActivity();
                    break;
                case R.id.rdc_move:
                    starRdcMoveActivity();
                    break;
                case R.id.rdc_bind:
                    startRdcBindActivity();
                    break;
                case R.id.rdc_pick:
                    startPickActivity();
                    break;
                case R.id.rdc_putawayoffline:
                    startRdcPutawayOfflineActivity();
                    break;
                case R.id.user_logout:
                    logoutAction();
                    break;
                default:
                    break;

            }
        }
    };

    private void startRdcInboundOnlineActivity(){
        startActivity(new Intent(getActivity(),
                BoxOnlineScanActivity.class));
    }

    private void startRdcInboundOfflineActivity(){
        startActivity(new Intent(getActivity(),
                BoxOfflineScanActivity.class));
    }

    private void startRdcPutawayActivity(){
        startActivity(new Intent(getActivity(),
                PutAwayScanActivity.class));
    }

    private void startRdcCheckMantrActivity(){
        startActivity(new Intent(getActivity(),
                CheckMatnrActivity.class));
    }

    private void startRdcPackActivity(){
        startActivity(new Intent(getActivity(),
                PackActivity.class));
    }

    private void starRdcMoveActivity(){
        startActivity(new Intent(getActivity(),
                MoveActivity.class));
    }

    private void startRdcBindActivity(){
        startActivity(new Intent(getActivity(),
                BoxBindActivity.class));
    }

    private void startPickActivity(){
        startActivity(new Intent(getActivity(),
                PickActivity.class));
    }

    private void startRdcPutawayOfflineActivity(){
        startActivity(new Intent(getActivity(),
                PutAwayOfflineActivity.class));
    }

    /**
     * 退出操作
     */
    private void logoutAction() {
        AppUtil.putValue(this.getActivity(), BaseActivity.WMS_IS_LOGIN, "false");
        startActivity(new Intent(getActivity(),
                LoginActivity.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1_layout, null);
        initView();
        return view;

    }

    private void initView() {
        rdcInboundOnline = (ViewGroup) view.findViewById(R.id.rdc_inbound_online);
        rdcInboundOffline=(ViewGroup)view.findViewById(R.id.rdc_inbound_offline);
        rdcPutaway=(ViewGroup)view.findViewById(R.id.rdc_putaway);
        rdcPutawayOffline=(ViewGroup)view.findViewById(R.id.rdc_putawayoffline);
        rdcInboundOnline.setOnClickListener(actionHandler);
        rdcInboundOffline.setOnClickListener(actionHandler);
        rdcPutaway.setOnClickListener(actionHandler);
        rdcPutawayOffline.setOnClickListener(actionHandler);
//        rdcCheckMantr=(ViewGroup)view.findViewById(R.id.rdc_checkmatnr);
//        rdcPack=(ViewGroup)view.findViewById(R.id.rdc_pack);
//        rdcMove=(ViewGroup)view.findViewById(R.id.rdc_move);
//        rdcBind=(ViewGroup)view.findViewById(R.id.rdc_bind);
//        rdcPick=(ViewGroup)view.findViewById(R.id.rdc_pick);
//        vgLogout=(ViewGroup)view.findViewById(R.id.user_logout);
//        rdcCheckMantr.setOnClickListener(actionHandler);
//        rdcPack.setOnClickListener(actionHandler);
//        rdcMove.setOnClickListener(actionHandler);
//        rdcBind.setOnClickListener(actionHandler);
//        rdcPick.setOnClickListener(actionHandler);
//        vgLogout.setOnClickListener(actionHandler);
    }
}
