package com.smec.wms.android.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.view.ViewfinderView;
import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.ScanPageInformation;
import com.smec.wms.android.bean.ScanTimeLine;
import com.smec.wms.android.bean.Status;
import com.smec.wms.android.face.ScanSuccessListener;
import com.smec.wms.android.util.AppUtil;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Vector;

/**
 * Created by Adobe on 2016/1/14.
 */
public abstract class BaseScanActivity extends BaseActivity implements SurfaceHolder.Callback,ScanSuccessListener {
    protected int currentTimeLine = 0;
    private SoundPool soundPool;    //播放声音效果
    protected String lastScanBarcode;
    private long lastScanTime = System.currentTimeMillis(); //记录最后一次扫描成功时间，避免扫描过快
    public static final String[] SHIFT_KEY_CHAR = new String[]{")", "!", "@", "#", "$", "%", "^", "&", "*", "("};


    private ImageButton backActionView;
    private ImageButton flashActionView;
    private ImageButton handInputView;
    private ImageButton cancelView;
    private AlertDialog handInputDialog;
    private EditText etBarcodeInput;
    private TextView tvTitle;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;

    private StringBuffer scanBuf = new StringBuffer();
    private long lastPhoneScanTime = System.currentTimeMillis();

    private int diSoundId=1;
    private int errorSoundId=1;

    //step information
    private View previousView;


    private View.OnClickListener commonActionHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (R.id.common_back == v.getId()) {
                //退出
                back();
            } else if (R.id.common_light == v.getId()) {
                //打开闪光灯
                controlFlashLight();
            } else if (R.id.common_hand_input == v.getId()) {
                //手动输入
                showInputDialog();
            } else if (R.id.common_cancel_input == v.getId()) {
                //取消输入
                cancelAction();
            }
        }
    };

    protected abstract int getContentViewId();

    protected abstract void cancelAction();  //响应用户撤销动作

    protected void beforeCreate(Bundle savedInstanceState) {
    }

    ;

    protected void afterCreate(Bundle savedInstanceState) {
    }

    ;

    private void controlFlashLight() {
        Camera camera = CameraManager.get().getCamera();
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (flashMode == null) {
            return;
        }
        //打开闪光灯
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }
            return;
        }

        //关闭闪光灯
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            }
        }

    }

    protected void nextTimeLine() {
        this.goToTimeLine(currentTimeLine + 1);
    }

    protected void preTimeLine() {
        if (currentTimeLine == 0) {
            return;
        }
        this.goToTimeLine(currentTimeLine - 1);
    }


    protected void goToTimeLine(int index) {
        ScanPageInformation componentViews = this.getPageComponentInf();
        List<ScanTimeLine> timeLineList = componentViews.getTimeLine();
        ScanTimeLine timeLine = timeLineList.get(currentTimeLine);
        TextView textView = timeLine.getTextView();
        ImageView imageView = timeLine.getImageView();
        int nomalColor = this.getResources().getColor(R.color.black);
        int highLightColor = this.getResources().getColor(R.color.smallblue);
        textView.setTextColor(nomalColor);
        imageView.setImageResource(R.mipmap.point);
        currentTimeLine = index % timeLineList.size();
        //设置高亮
        ScanTimeLine curTimeLine = timeLineList.get(currentTimeLine);
        curTimeLine.getTextView().setTextColor(highLightColor);
        curTimeLine.getImageView().setImageResource(R.mipmap.point_highlight);
        this.lastScanBarcode = "";
    }

    protected boolean initCamera() {
        CameraManager.init(getApplication());
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.fragment_scan_common_title);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        this.handInputView = (ImageButton) this.findViewById(R.id.common_hand_input);
        backActionView = (ImageButton) this.findViewById(R.id.common_back);
        flashActionView = (ImageButton) this.findViewById(R.id.common_light);
        cancelView = (ImageButton) this.findViewById(R.id.common_cancel_input);
        tvTitle = (TextView) this.findViewById(R.id.common_title);
        cancelView.setOnClickListener(commonActionHandler);
        backActionView.setOnClickListener(commonActionHandler);
        flashActionView.setOnClickListener(commonActionHandler);
        handInputView.setOnClickListener(commonActionHandler);
        hasSurface = false;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        diSoundId=soundPool.load(this, R.raw.di, 1);
        errorSoundId=soundPool.load(this,R.raw.error,1);
        this.initScanPageView();
        return true;
    }

    protected void playSound() {
        soundPool.play(diSoundId, 1, 1, 0, 0, 1);
    }

    @Override
    protected void afterErrorHandle(String iface,String errorMsg){
        soundPool.play(errorSoundId, 1, 1, 1, 0, 1);
    }



    /**
     * 扫码成功调用该方法,子类可以重写该方法处理扫描成功逻辑
     *
     * @param value
     */
    protected void scanSuccess(String value) {
        Log.e("scanSuccess", value);
    }

    protected void setSumaryText(String text) {
        this.getPageComponentInf().getSumaryView().setText(text);
    }

    protected abstract ScanPageInformation getPageComponentInf();

    protected void initScanPageView() {
        ScanPageInformation componentViews = this.getPageComponentInf();
        componentViews.renderTimeLine();
        this.goToTimeLine(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.beforeCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setContentView(this.getContentViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.initCamera();
        this.afterCreate(savedInstanceState);
        WmsApplication.getInstance().setCurrentScanActivity(this);
    }


    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
////        this.showMessage(String.valueOf(event.getFlags()));
////        Log.e("device",String.valueOf(event.getFlags()));
//        Log.e("keycode", String.valueOf((char) event.getUnicodeChar()));
//        int flag=event.getFlags();
//        Log.e("flag",String.valueOf(flag));
//        if (event.getAction() == KeyEvent.ACTION_DOWN&&flag==8) {
//            int key = event.getKeyCode();
//            Log.e("keycode", String.valueOf((char) event.getUnicodeChar()));
//            //   Log.e("device",String.valueOf(event.getFlags()));
//            if (KeyEvent.KEYCODE_0 <= key && key <= KeyEvent.KEYCODE_9) {
//                if (!event.isShiftPressed()) {
//                    scanBuf.append(key - KeyEvent.KEYCODE_0);
//                } else {
//                    scanBuf.append(SHIFT_KEY_CHAR[key - KeyEvent.KEYCODE_0]);
//                }
//            } else if (KeyEvent.KEYCODE_A <= key && key <= KeyEvent.KEYCODE_Z) {
//                char cc = (char) ('A' + (key - KeyEvent.KEYCODE_A));
//                scanBuf.append(cc);
//            } else if (KeyEvent.KEYCODE_ENTER == key) {
//                String barcode = scanBuf.toString();
//                Log.e("code", barcode);
//                scanBuf.setLength(0);
//                barcode=barcode.toUpperCase().trim();
//                this.scanSuccess(barcode);
//            } else {
//                if (key != KeyEvent.KEYCODE_SHIFT_LEFT && key != KeyEvent.KEYCODE_SHIFT_RIGHT) {
//                    scanBuf.append((char) event.getUnicodeChar());
//                }
//            }
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    @Override
    public void scanBarcodeSuccess(String content){
        if(content!=null&&content.trim().length()>0) {
            this.scanSuccess(content.toUpperCase().trim());
        }
    }


    protected String getBarcodeShow(String barcode) {
        return barcode;
    }

    @Override
    protected void requestFail() {
        this.lastScanBarcode = "";
    }

    @Override
    protected void serverError(String iface, String errorMsg) {
        super.serverError(iface, errorMsg);
        this.lastScanBarcode = "";
    }

    protected void focusStepLayout(int layoutid) {
        View layout = this.findViewById(layoutid);
        if (previousView != null) {
            this.highlightViews(previousView, true, false);
        }
        this.highlightViews(layout, true, true);
    }

    private void highlightViews(View view, boolean root, boolean focus) {
        if (view instanceof ViewGroup) {
            //布局组件递归高亮子组件
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                highlightViews(vg.getChildAt(i), false, focus);
            }
            if (root) {
                int color = this.getResources().getColor(focus ? R.color.yellow : R.color.white);
                if (focus == false) {
                    vg.setBackgroundColor(color);
                } else {
                    vg.setBackgroundResource(R.drawable.bg_layout_focus);
                }
                previousView = view;
            }
        } else if (view instanceof TextView) {
            //文本组件直接高亮
            TextView tv = (TextView) view;
            int color = this.getResources().getColor(focus ? R.color.main_color : R.color.black);
            tv.setTextColor(color);
            tv.getPaint().setFakeBoldText(focus);
        } else if (view instanceof EditText) {
            //输入框组件聚焦
            EditText et = (EditText) view;
            if (focus) {
                et.requestFocus();
            } else {
                et.clearFocus();
            }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result result, Bitmap bitmap) {
        String barcode = result.getText();
        barcode = barcode.trim().toUpperCase();
        this.handler.restartPreviewAndDecode();
        Log.e("result", barcode);
        if (barcode.equals(lastScanBarcode)) {
            return;
        }
        if (Status.REQUESTING.equals(this.activityStatus)) {
            //请求期间不做响应
            return;
        }
        long curTime = System.currentTimeMillis();
        if (curTime - this.lastScanTime < 1000 * 2) {
            //两次扫描时间间隔需要超过3s
            return;
        }
        this.lastScanBarcode = barcode;
        this.lastScanTime = System.currentTimeMillis();
        String showText = this.getBarcodeShow(barcode);
        if (this.getPageComponentInf().getBarcodeView() != null) {
            this.getPageComponentInf().getBarcodeView().setText(showText);
        }
        //扫码结果需转换成大写并且去掉两边空格
        this.scanSuccess(barcode);
        Log.e("result", barcode);
        soundPool.play(1, 10, 10, 0, 0, 1);
        this.handler.restartPreviewAndDecode();
    }

    public void back() {
        this.finish();
    }

    private DialogInterface.OnClickListener handInputDialogListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    handInputDialog.dismiss();
                    String value = etBarcodeInput.getText().toString();
                    if (value != null) {
                        value = value.toUpperCase().trim();
                    }
                    scanSuccess(value);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    handInputDialog.dismiss();
                    break;
                default:
                    handInputDialog.dismiss();
                    break;
            }
        }
    };

    private void showInputDialog() {
        //手动输入对话框
        View view = this.getLayoutInflater().inflate(R.layout.layout_barcode_input, null);
        this.etBarcodeInput = (EditText) view.findViewById(R.id.barcode_input);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getInputDialogTitle());
        builder.setPositiveButton("确定", handInputDialogListener);
        builder.setNegativeButton("取消", handInputDialogListener);
        builder.setView(view);
        builder.setCancelable(false);
        this.handInputDialog = builder.create();
        this.handInputDialog.show();
        AppUtil.openIMM(this, this.etBarcodeInput);
    }

    protected String getInputDialogTitle() {
        return "请输入条形码";
    }

    protected void resetTextView(TextView... views) {
        for (TextView view : views) {
            view.setText(null);
        }
    }

    protected void resetTextView(TextView[] views, int begin, int length) {
        for (int i = begin; i < begin + length; ++i) {
            views[i].setText(null);
        }
    }

    public void setPageTitle(String title) {
        this.tvTitle.setText(title);
    }

    private long lastClickTimestamp = System.currentTimeMillis();
    private View.OnClickListener layoutDoubleClickLsn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer step = (Integer) v.getTag();
            Integer currentStep = getCurrentOperationStep();
            if (currentStep == -1 || step != currentStep) {
                return;
            }
            long curTimestamp = System.currentTimeMillis();
            if (curTimestamp - lastClickTimestamp < 1500) {
                //1.5秒内连续双击有效
                showInputDialog();
            }
            lastClickTimestamp = curTimestamp;
        }
    };

    protected void bindLayoutDoubleClick(int[] layoutIDs, int[] steps) {
        for (int i = 0; i < layoutIDs.length; ++i) {
            View v = findViewById(layoutIDs[i]);
            v.setTag(steps[i]);
            v.setOnClickListener(layoutDoubleClickLsn);
//            v.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    showMessage("input:"+keyCode);
//                    return true;
//                }
//            });
        }
    }

    protected int getCurrentOperationStep() {
        return -1;
    }


    public void showCompleteDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("操作完成")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .show();
    }
}
