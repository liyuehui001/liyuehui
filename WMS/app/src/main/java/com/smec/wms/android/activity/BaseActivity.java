package com.smec.wms.android.activity;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.Status;
import com.smec.wms.android.bean.UserProfile;
import com.smec.wms.android.server.GsonRequest;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;


import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
public class BaseActivity extends FragmentActivity implements Response.Listener, Response.ErrorListener, View.OnClickListener {

    private RequestQueue requestQueue;
    protected ProgressDialog progressDialog;
    protected String activityStatus = Status.NEW;

    private boolean multRequest = false;

    public static final String WMS_USER = "WMS_USER";
    public static final String WMS_USER_PWD = "WMS_USER_PWD";
    public static final String WMS_USER_DISPLAYNAME = "WMS_USER_DISPLAYNAME";
    public static final String WMS_WAREHOUSE_NO = "WMS_WAREHOUSE_NO";
    public static final String WMS_IS_CDC_FLAG = "WMS_IS_CDC_FLAG";
    public static final String WMS_IS_LOGIN = "WMS_IS_LOGIN";     //用户是否已经登录

    public static final String WMS_NETWORK = "WMS_NETWORK";     //网络

    public static final String WMS_AUTO_LOGIN = "WMS_AUTO_LOGIN" ;
    public static final String WMS_TOKEN="WMS_TOKEN";
    public static final String WMS_USER_ROLE="USER_ROLE";//用户角色

    /**
     * 统一错误处理
     *
     * @param status  com.smec.wms.android.bean.Status 相关数据
     * @param message 错误详细信息
     */
    public void erroHandle(String status, String message) {
        Log.e("status", status);
        Log.e("message", message);
        ToastUtil.show(this, message);
        this.endMulRequest();
        afterErrorHandle(status, message);
    }

    public void request(String iface, Map<String, String> param, Class cls, String message) {
        if (iface == null) {
            return;
        }
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, "", message, false, false);
        } else {
            progressDialog.setMessage(message);
        }
        String url = IFace.getFullUrl(iface, param);
        Log.e("url:", url);
        GsonRequest request = new GsonRequest(iface, url, param, cls, this, this);
        requestQueue.add(request);
        activityStatus = Status.REQUESTING;
    }

    protected void requestSuccess(ServerResponse response) {
    }

    protected void requestFail() {
        this.endMulRequest();
    }

    /**
     * 如果服务器返回错误信息将调用该方法
     *
     * @param iface
     * @param errorMsg
     */
    protected void serverError(String iface, String errorMsg) {
        //默认用toast提示用户
        ToastUtil.show(this, errorMsg);
        this.endMulRequest();
        afterErrorHandle(iface,errorMsg);

    }

    protected void afterErrorHandle(String iface,String errorMsg){

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        this.requestFail();
        this.erroHandle(Status.ERROR, "与服务器通信失败");
        activityStatus = Status.NEW;
    }

    public void onResponse(Object serverResponse) {
        this.dismissProgress();
        activityStatus = Status.NEW;
        Object response = ((ServerResponse) serverResponse).getData();
        if (response instanceof ResponseData) {
            ResponseData responseData = (ResponseData) response;
            if (!Status.OK.equals(responseData.getCode())) {
                //数据错误
                this.erroHandle(responseData.getCode(), responseData.getErrMsg());
                return;
            }
            if (!this.isResponseOK((ServerResponse) serverResponse)) {
                this.serverError(((ServerResponse) serverResponse).getIface(), responseData.getErrMsg());
                return;
            }
        }
        this.requestSuccess((ServerResponse) serverResponse);
    }

    protected void clickActionHandler(View v) {
    }

    protected boolean isResponseOK(ServerResponse response) {
        ResponseData responseData = (ResponseData) response.getData();
//        if (responseData.getErrMsg() != null && responseData.getErrMsg().trim().length() > 0 && !"当前人员没有所属仓库".equals(responseData.getErrMsg().trim())) {
//            return false;
//        }
        if (responseData.getErrMsg() != null && responseData.getErrMsg().trim().length() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        this.clickActionHandler(v);
    }

    public void beginMulRequest() {
        this.multRequest = true;
    }

    public void endMulRequest() {
        this.multRequest = false;
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    private void dismissProgress() {
        if (false == this.multRequest) {
            //如果是多次请求，窗口不关闭
            this.progressDialog.dismiss();
        }
    }

    protected String getValue(String key) {
        return AppUtil.getValue(this, key);
    }

    protected void putValue(Map<String, String> data) {
        AppUtil.putValue(this, data);
    }

    protected void putValue(String key, String value) {
        AppUtil.putValue(this, key, value);
    }

    private String getCurrentUser() {
        return this.getValue(WMS_USER);
    }

    public WmsApplication getWmsApplication() {
        return (WmsApplication) this.getApplication();
    }

    public UserProfile getUserProfile() {
        return this.getWmsApplication().getUserProfile();
    }

    public void showMessage(String msg) {
        ToastUtil.show(this, msg);
    }

    /**
     * 格式化消息
     *
     * @param format
     * @param params
     */
    public void showMessage(String format, Object... params) {
        ToastUtil.show(this, String.format(format, params));
    }
}
