package com.smec.wms.android.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smec.wms.R;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.activity.LoginActivity;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment {

    private TextView nameText ;
    private ViewGroup networkLayout ;
    private TextView networkInfoText ;

    private ViewGroup exitGroup ;

    private TextView systemInfoText ;

    private AlertDialog networkDialog;
    private int networkChoose = 0;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int network = 2 ;
        try{
            network = Integer.parseInt(WmsApplication.getInstance().getUserProfile().getNetwork());
        }catch (Exception e){
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        nameText = (TextView)view.findViewById(R.id.display_name);

        networkLayout = (ViewGroup)view.findViewById(R.id.item_router);
        networkInfoText = (TextView)view.findViewById(R.id.network_info);

        systemInfoText = (TextView)view.findViewById(R.id.systemInfo);

        exitGroup = (ViewGroup)view.findViewById(R.id.item_exit);

        WmsApplication.getInstance().setNetWork(network);
        String msg=this.getUserProfile().getNetworkDesc();
        networkInfoText.setText(msg);


        networkDialog = new AlertDialog.Builder(this.getActivity())
                .setTitle("选择网络环境")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeNetwork(networkChoose);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        networkDialog.dismiss();
                    }
                })
                .setSingleChoiceItems(new CharSequence[]{"外网环境", "内网环境", "测试环境"}, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int position) {
                                networkChoose = position;
                            }
                        }).create();

        networkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkDialog.show();
            }
        });

        systemInfoText.setText(String.format("当前版本:%s",WmsApplication.getVersion()));

        nameText.setText(String.format("%s %s", this.getUserProfile().getDsiplayName(), this.getUserProfile().getUid()));

        exitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAction();
            }
        });
        return view ;
    }

    private void changeNetwork(int type) {
        WmsApplication.getInstance().setNetWork(type);
        String msg=this.getUserProfile().getNetworkDesc();
        ToastUtil.show(this.getActivity(),"网络已经切换到"+msg);
        networkInfoText.setText(msg);
    }

    private void logoutAction() {
        AppUtil.putValue(this.getActivity(), BaseActivity.WMS_IS_LOGIN, "false");
        startActivity(new Intent(getActivity(),
                LoginActivity.class));
        getActivity().finish();
    }
}
