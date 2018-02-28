package com.smec.wms.android.application;

import backPower.Bluetooth.BluetoothService;


import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.smec.wms.R;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.bean.UserProfile;
import com.smec.wms.android.face.BlueToothConnectListener;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.WmsManager.WmsRealmManager.RealmManager;
import com.version.smec.wms.utils.ToastUtils;

public class WmsApplication extends Application {
    public final int NO_BLUETOOTH_MODULE = 1;
    private UserProfile userProfile;
    private static WmsApplication instance;
    public static long lastChecmVersion = 0;

    public BluetoothAdapter mBluetoothAdapter = null;
    public final Handler tHandler = new Handler();
    private BluetoothService btservice;
    private final static StringBuffer scanBuf = new StringBuffer();

    private ScanSuccessListener currentScanListener;
    private BlueToothConnectListener blueToothConnectListener;
    public static int BLUE_TOOTH_CONNECT = 1;   //未连接
    private ScanReceiver receiver;
    private IntentFilter filter;
    private static Context mContext;

    public class ScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String barcode=bundle.getString("scannerdata");
            barcode=barcode.toUpperCase().trim();
            if(currentScanListener!=null){
                currentScanListener.scanBarcodeSuccess(barcode);
            }
            //scanSuccess(barcode);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        receiver = new ScanReceiver();
        filter = new IntentFilter("smecwms");
        registerReceiver(receiver, filter);
        mContext=getApplicationContext();
        VolleyManager.init(mContext);
        ToastUtils.init(mContext);
        FileDownloader.setup(mContext);
        RealmManager.initRealm(mContext);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        unregisterReceiver(receiver);
    }


    public static WmsApplication getInstance() {
        return instance;
    }

    public void setUserProfile(UserProfile profile) {
        this.userProfile = profile;
    }

    public UserProfile getUserProfile() {
        if(userProfile == null){
            userProfile = new UserProfile();
        }
        SharedPreferences sp = this.getSharedPreferences("WMS_DATA", Context.MODE_PRIVATE);
        userProfile.setDsiplayName(sp.getString(BaseActivity.WMS_USER_DISPLAYNAME, ""));
        userProfile.setIsCDCFlag(sp.getString(BaseActivity.WMS_IS_CDC_FLAG, ""));
        userProfile.setWarehouseNo(sp.getString(BaseActivity.WMS_WAREHOUSE_NO, ""));
        userProfile.setUid(sp.getString(BaseActivity.WMS_USER, ""));
        userProfile.setNetwork(sp.getString(BaseActivity.WMS_NETWORK, ""));
        return userProfile;
    }

    public static String getVersion() {
        String version = getInstance().getResources().getString(R.string.app_version);
        return version;
    }

    public int initBlueTooth() {
      /*  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return NO_BLUETOOTH_MODULE;
        }
        btservice = new BluetoothService(this, mHandler);*/
        return 0;
    }

    public int connectBlueTooth(String address, BlueToothConnectListener lsn) {
        if (true) {
            return 0;
        }
        blueToothConnectListener = lsn;
        if (btservice == null) {
            int result = this.initBlueTooth();
            if (result != 0) {
                return result;
            }
        }
        if (address == null) {
            address = AppUtil.getValue(this, "BLUE_TOOTH_ADDRESS");
            if (address == null || address.trim().length() == 0) {
                ToastUtil.show(this, "扫描设备未连接请连接扫描设备");
                return 1;
            }
        } else {
            AppUtil.putValue(this, "BLUE_TOOTH_ADDRESS", address);
        }
        if (false == btservice.is_cennected()) {
            BLUE_TOOTH_CONNECT = 1;
            blueToothConnectListener = lsn;
            btservice.BluetoothConnect(address);
            btservice.Set_Toast_Enable(false);
        }
        return 0;
    }

    public void disconnectBlueTooth() {
        try {
            btservice.Stop();
            btservice = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        switch (msg.arg1) {
                            case 0: // we're doing nothing
                            case 1:// now listening for incoming connections
                                //  Mconnectb.setText("连接设备")
                                break;
                            case 2:// now initiating an outgoing connection

                                break;
                            case 3: // now connected to a remote device//连接成功
                                if (blueToothConnectListener != null) {
                                    blueToothConnectListener.connectSuccess();
                                }
                                BLUE_TOOTH_CONNECT = 0;
                                break;
                        }

                        break;
                    case 0:  //收到数据
                        handleBlueToothData(msg);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    };

    private void handleBlueToothData(Message msg) {
        byte[] data = (byte[]) msg.obj;
        int size = msg.arg1;
        try {
            String readMessage = new String(data, 0, size, "GBK");
            scanBuf.append(readMessage.toUpperCase().trim());
            if (0x0A == data[size - 1]) {
                //扫描结束
                if (currentScanListener != null) {
                    currentScanListener.scanBarcodeSuccess(scanBuf.toString());
                    scanBuf.setLength(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentScanActivity(ScanSuccessListener activity) {
        currentScanListener = activity;
    //    startScanService();
    }


    /**
     * 设置网络类型
     *
     * @param netWork
     */
    public void setNetWork(int netWork) {
        IFace.setNetwork(netWork);
        AppUtil.putValue(this, BaseActivity.WMS_NETWORK, String.valueOf(netWork));
        if (userProfile != null) {
            userProfile.setNetwork(String.valueOf(netWork));
        }
    }

    public static Context getContext() {
        return mContext;
    }
}

