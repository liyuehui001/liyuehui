package com.smec.wms.android.fragment;

import android.support.v4.app.Fragment;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.smec.wms.android.activity.BaseActivity;
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
 * Created by Adobe on 2016/1/28.
 */
public class BaseFragment extends Fragment implements Response.Listener, Response.ErrorListener, View.OnClickListener {

    private RequestQueue requestQueue;
    protected ProgressDialog progressDialog;
    protected String activityStatus = Status.NEW;

    private boolean multRequest = false;

    /**
     * 统一错误处理
     *
     * @param status  com.smec.wms.android.bean.Status 相关数据
     * @param message 错误详细信息
     */
    public void erroHandle(String status, String message) {
        Log.e("status", status);
        Log.e("message", message);
        ToastUtil.show(this.getActivity(), message);
    }

    public void request(String iface, Map<String, String> param, Class cls, String message) {
        if (iface == null) {
            return;
        }
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this.getActivity());
        }
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this.getActivity(), "", message, false, false);
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
        ToastUtil.show(this.getActivity(), errorMsg);
        this.endMulRequest();
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
            if (responseData.getErrMsg() != null && responseData.getErrMsg().trim().length() > 0) {
                this.serverError(((ServerResponse) serverResponse).getIface(), responseData.getErrMsg());
                return;
            }
        }
        this.requestSuccess((ServerResponse) serverResponse);
    }

    protected void clickActionHandler(View v) {

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
        return AppUtil.getValue(this.getActivity(), key);
    }

    protected void putValue(Map<String, String> data) {
        AppUtil.putValue(this.getActivity(), data);
    }

    protected void putValue(String key, String value) {
        AppUtil.putValue(this.getActivity(), key, value);
    }

    private String getCurrentUser() {
        return this.getValue(BaseActivity.WMS_USER);
    }

    public WmsApplication getWmsApplication(){
        return (WmsApplication)this.getActivity().getApplication();
    }

    public UserProfile getUserProfile(){
        return this.getWmsApplication().getUserProfile();
    }

}

