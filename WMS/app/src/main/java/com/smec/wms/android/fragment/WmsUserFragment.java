package com.smec.wms.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.smec.wms.R;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.activity.BluetoothListActivity;
import com.smec.wms.android.activity.BoxOnlineScanActivity;
import com.smec.wms.android.activity.LoginActivity;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.face.BlueToothConnectListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WmsUserFragment extends BaseFragment implements BlueToothConnectListener {
    private ViewGroup vgAbout;
    private ViewGroup vgLogout;
    private ViewGroup vgBlueTooth;
    private ViewGroup vgNetwork;

    private TextView tvUserName;
    private TextView tvWarehouse;
    private TextView tvBlueTooth;
    private TextView tvNetwork;
    private TextView tvVersion;
    private View mainView;
    private AlertDialog networkDialog;
    private int networkChoose = 0;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    //private static int BLUE_TOOTH_CONNECT = 1;    //未连接

    @Override
    protected void clickActionHandler(View view) {
        switch (view.getId()) {
            case R.id.user_logout:
                logoutAction();
                break;
            case R.id.connect_bluetooth:
                startBlueToothActivy();
                break;
            case R.id.user_change_network:
                networkDialog.show();
                break;
            default:
                break;
        }
    }


    /**
     * 退出操作
     */
    private void logoutAction() {
        AppUtil.putValue(this.getActivity(), BaseActivity.WMS_IS_LOGIN, "false");
        startActivity(new Intent(getActivity(),
                LoginActivity.class));
    }

    private void startBlueToothActivy() {
        if (WmsApplication.BLUE_TOOTH_CONNECT == 1) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                ToastUtil.show(this.getActivity(), "该手机不支持蓝牙");
                return;
            }
            if (adapter.isEnabled() == false) {
                ToastUtil.show(this.getActivity(), "手机蓝牙未开启请先开启蓝牙");
                return;
            }
            Intent serverIntent = new Intent(this.getActivity(), BluetoothListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        } else {
            //断开连接
            WmsApplication.getInstance().disconnectBlueTooth();
            this.tvBlueTooth.setText("连接到扫描设备");
            ToastUtil.show(this.getActivity(), "连接已断开");
            WmsApplication.BLUE_TOOTH_CONNECT = 1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
                    String name = data.getExtras()
                            .getString(BluetoothListActivity.EXTRA_DEVICE_NAME);
                    //btservice.BluetoothConnect(address);  //启动连接
                    ToastUtil.show(this.getActivity(), "正在连接到扫描设备" + name);
                    WmsApplication.getInstance().connectBlueTooth(address, this);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {

                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d("bluetooth", "Bluetooth not enabled");
                    ToastUtil.show(this.getActivity(), "蓝牙未启动");
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment4_layout, null);//加载布局
        initViews();
        return mainView;

    }

    private void initViews() {
        tvUserName = (TextView) mainView.findViewById(R.id.user_username);
        tvWarehouse = (TextView) mainView.findViewById(R.id.user_warehouse);
        tvBlueTooth = (TextView) mainView.findViewById(R.id.list_item_connect_bluetooth);
        tvVersion=(TextView)mainView.findViewById(R.id.app_version);
        vgAbout = (ViewGroup) mainView.findViewById(R.id.user_about);
        vgLogout = (ViewGroup) mainView.findViewById(R.id.user_logout);
        vgBlueTooth = (ViewGroup) mainView.findViewById(R.id.connect_bluetooth);
        vgNetwork = (ViewGroup) mainView.findViewById(R.id.user_change_network);
        tvNetwork = (TextView) mainView.findViewById(R.id.user_network);
        vgLogout.setOnClickListener(this);
        vgAbout.setOnClickListener(this);
        vgBlueTooth.setOnClickListener(this);
        vgNetwork.setOnClickListener(this);
        tvBlueTooth.setText(WmsApplication.BLUE_TOOTH_CONNECT == 0 ? "断开当前连接" : "连接到扫描设备");
        tvNetwork.setText(this.getUserProfile().getNetworkDesc());
        tvVersion.setText(String.format("当前版本:%s",WmsApplication.getVersion()));

        tvUserName.setText(String.format("%s(%s)", this.getUserProfile().getDsiplayName(), this.getUserProfile().getUid()));
        tvWarehouse.setText(this.getUserProfile().getWarehouseNo());
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
    }

    @Override
    public void connectSuccess() {
        ToastUtil.show(this.getActivity(), "连接成功");
        tvBlueTooth.setText("断开当前连接");
    }

    @Override
    public void connectFailure() {
        ToastUtil.show(this.getActivity(), "连接失败请检查蓝牙是否开启或其他设备是否占用蓝牙");
        tvBlueTooth.setText("连接到扫描设备");
    }

    private void changeNetwork(int type) {
        WmsApplication.getInstance().setNetWork(type);
        String msg=this.getUserProfile().getNetworkDesc();
        ToastUtil.show(this.getActivity(),"网络已经切换到"+msg);
        tvNetwork.setText(msg);
    }
}
