package com.smec.wms.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.android.activity.CPZXActivity;
import com.smec.wms.android.activity.CheckMatnrActivity;
import com.smec.wms.android.activity.DeliveryActivity;
import com.smec.wms.android.activity.ImageQueryActivity;
import com.smec.wms.android.activity.MatnrQueryActivity;
import com.smec.wms.android.activity.MatnrStockQueryActivity;
import com.smec.wms.android.activity.MoveActivity;
import com.smec.wms.android.activity.ShelfQueryActivity;
import com.smec.wms.android.activity.StockQueryActivity;

public class WmsOtherFragment extends Fragment {
    private ViewGroup rdcMove;              //RDC_移库
    private ViewGroup rdcCheckMantr;        //RDC_盘点
    private ViewGroup rdcStockQuery;        //库存清单查询
    private ViewGroup rdcMatnrQuery;        //库存清单查询
    private ViewGroup rdcShelfQuery;        //货位库存查询
    private ViewGroup rdcMatnrStockQuery;   //物料库存查询
    private ViewGroup deliveryQuery;        //物流管理
    private ViewGroup cpzxQuery;            //成品装箱清单查询
    private ViewGroup imageQuery ;


    private View.OnClickListener actionHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rdc_checkmatnr:
                    startRdcCheckMantrActivity();
                    break;
                case R.id.rdc_move:
                    starRdcMoveActivity();
                    break;
                case R.id.rdc_stockquery:
                    startRdcStockQueryActivity();
                    break;
                case R.id.rdc_matnrquery:
                    startRdcMatnrQueryActivity();
                    break;
                case R.id.rdc_shelfquery:
                    startRdcShelftQueryActivity();
                    break;
                case R.id.rdc_matnr_stock_query:
                    startRdcMatnrStockQueryActivity();
                    break;
                case R.id.rdc_delivery:
                    startDeliveryQueryActivity();
                    break;
                case R.id.rdc_cpzx:
                    starCpzxActivity();
                    break;
                case R.id.rdc_image_query:
                    startImageQueryActivity();
                default:
                    break;

            }
        }
    };

    private void starRdcMoveActivity() {
        startActivity(new Intent(getActivity(),
                MoveActivity.class));
    }

    private void startRdcCheckMantrActivity() {
        startActivity(new Intent(getActivity(),
                CheckMatnrActivity.class));
    }

    private void startRdcStockQueryActivity() {
        startActivity(new Intent(getActivity(),
                StockQueryActivity.class));
    }

    private void startRdcMatnrQueryActivity() {
        startActivity(new Intent(getActivity(),
                MatnrQueryActivity.class));
    }

    private void startRdcShelftQueryActivity(){
        startActivity(new Intent(getActivity(),
                ShelfQueryActivity.class));
    }

    private void startRdcMatnrStockQueryActivity(){
        startActivity(new Intent(getActivity(),
                MatnrStockQueryActivity.class));
    }
    private void startDeliveryQueryActivity(){
        startActivity(new Intent(getActivity(),
                DeliveryActivity.class));
    }

    private void starCpzxActivity(){
        startActivity(new Intent(getActivity(),
                CPZXActivity.class));
    }

    private void startImageQueryActivity(){
        startActivity(new Intent(getActivity(), ImageQueryActivity.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout, null);
        rdcCheckMantr = (ViewGroup) view.findViewById(R.id.rdc_checkmatnr);
        rdcMove = (ViewGroup) view.findViewById(R.id.rdc_move);
        rdcStockQuery=(ViewGroup)view.findViewById(R.id.rdc_stockquery);
        rdcMatnrQuery=(ViewGroup)view.findViewById(R.id.rdc_matnrquery);
        rdcShelfQuery=(ViewGroup)view.findViewById(R.id.rdc_shelfquery);
        rdcMatnrStockQuery=(ViewGroup)view.findViewById(R.id.rdc_matnr_stock_query);
        deliveryQuery=(ViewGroup)view.findViewById(R.id.rdc_delivery);
        cpzxQuery=(ViewGroup)view.findViewById(R.id.rdc_cpzx);
        imageQuery = (ViewGroup)view.findViewById(R.id.rdc_image_query);
        rdcCheckMantr.setOnClickListener(actionHandler);
        rdcMatnrQuery.setOnClickListener(actionHandler);
        rdcShelfQuery.setOnClickListener(actionHandler);
        rdcMove.setOnClickListener(actionHandler);
        rdcStockQuery.setOnClickListener(actionHandler);
        rdcMatnrStockQuery.setOnClickListener(actionHandler);
        deliveryQuery.setOnClickListener(actionHandler);
        cpzxQuery.setOnClickListener(actionHandler);
        imageQuery.setOnClickListener(actionHandler);
        return view;

    }
}
