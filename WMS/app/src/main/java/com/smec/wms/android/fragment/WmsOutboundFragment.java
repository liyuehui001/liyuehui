package com.smec.wms.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.android.activity.BoxBindActivity;
import com.smec.wms.android.activity.DeliveryActivity;
import com.smec.wms.android.activity.PackActivity;
import com.smec.wms.android.activity.PickActivity;

/**
 * Created by Adobe on 2016/2/21.
 */
public class WmsOutboundFragment extends Fragment {
    private ViewGroup rdcBind;              //RDC_绑定箱子
    private ViewGroup rdcPick;              //RDC_拣货
    private ViewGroup rdcPack;              //RDC_装箱
    private ViewGroup deliveryQuery;        //物流管理
    private View.OnClickListener actionHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rdc_pack:
                    startRdcPackActivity();
                    break;
                case R.id.rdc_bind:
                    startRdcBindActivity();
                    break;
                case R.id.rdc_pick:
                    startPickActivity();
                    break;
                case R.id.rdc_delivery:
                    startDeliveryQueryActivity();
                    break;
                default:
                    break;

            }
        }
    };

    private void startRdcBindActivity() {
        startActivity(new Intent(getActivity(),
                BoxBindActivity.class));
    }

    private void startPickActivity() {
        startActivity(new Intent(getActivity(),
                PickActivity.class));
    }

    private void startRdcPackActivity() {
        startActivity(new Intent(getActivity(),
                PackActivity.class));
    }

    private void startDeliveryQueryActivity(){
        startActivity(new Intent(getActivity(),
                DeliveryActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_layout, null);
        rdcPack=(ViewGroup)view.findViewById(R.id.rdc_pack);
        rdcBind=(ViewGroup)view.findViewById(R.id.rdc_bind);
        rdcPick=(ViewGroup)view.findViewById(R.id.rdc_pick);
        deliveryQuery=(ViewGroup)view.findViewById(R.id.rdc_delivery);
        rdcPack.setOnClickListener(actionHandler);
        rdcBind.setOnClickListener(actionHandler);
        rdcPick.setOnClickListener(actionHandler);
        deliveryQuery.setOnClickListener(actionHandler);
        return view;
    }


}
