package com.smec.wms.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smec.wms.R;
import com.smec.wms.android.bean.Status;
import com.smec.wms.android.bean.UserProfile;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;
import com.smec.wms.android.util.AppUtil;
import com.smec.wms.android.util.ToastUtil;
import com.version.smec.wms.WmsManager.VolleyManager.HttpResult;
import com.version.smec.wms.WmsManager.VolleyManager.VolleyManager;
import com.version.smec.wms.WmsManager.WmsRealmManager.RealmManager;
import com.version.smec.wms.bean.HttpResultDto;
import com.version.smec.wms.bean.SmecUser;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.GsonHelper;
import com.version.smec.wms.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jianfeng on 2015/12/24.
 */
public class LoginActivity extends BaseActivity {

    //views
    private Button btLogin;
    private EditText etAccount;
    private EditText etPassword;
    private Spinner spNet;
    private List<String> networkList = new ArrayList<String>();
    private String autoLogin = "Y" ;
    private ImageButton imageButton ;
    private ViewGroup autoLoginLayout ;
    private String passWord="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeCreate(savedInstanceState);
        setContentView(getContentViewId());
        afterCreate(savedInstanceState);
    }

    protected int getContentViewId() {
        return R.layout.activity_user_login;
    }

    protected void beforeCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //页面创建前判断是否登录。登录过的用户直接跳转到主页面
        String isLogin = this.getValue(WMS_IS_LOGIN);
        if ("true".equals(isLogin)) {
            startActivity(new Intent(LoginActivity.this,
                    MainActivity.class));
            String network = this.getValue(WMS_NETWORK);
            //设置网络类型
            if (network != null && network.trim().length() > 0) {
                this.getWmsApplication().setNetWork(Integer.parseInt(network));
            }
        }
    }

    protected void afterCreate(Bundle savedInstanceState) {
        networkList.add("外网环境");
        networkList.add("内网环境");
        networkList.add("测试环境");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, networkList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.btLogin = (Button) this.findViewById(R.id.login_action);
        this.etAccount = (EditText) this.findViewById(R.id.login_account);
        this.etPassword = (EditText) this.findViewById(R.id.login_password);
        this.spNet = (Spinner) this.findViewById(R.id.network_list);
        this.spNet.setAdapter(adapter);
        this.btLogin.setOnClickListener(this);
        this.etAccount.setText(this.getValue(WMS_USER));
        this.autoLoginLayout = (ViewGroup)this.findViewById(R.id.check_layout);
        this.imageButton  = (ImageButton)this.findViewById(R.id.user_checkbox);
        if (this.getValue(WMS_AUTO_LOGIN).equals("")){
            this.putValue(WMS_AUTO_LOGIN,"Y");
            this.autoLogin = "Y";
        }else {
            this.autoLogin = this.getValue(WMS_AUTO_LOGIN);
        }

        if (this.autoLogin.equals("Y")){
            this.imageButton.setBackgroundResource(R.mipmap.select_checkbox);
            this.etPassword.setText(this.getValue(WMS_USER_PWD));
        }else {
            this.imageButton.setBackgroundResource(R.mipmap.login_unselect_checkbox);
        }

        autoLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoLogin.equals("N")){
                    autoLogin = "Y" ;
                    imageButton.setBackgroundResource(R.mipmap.select_checkbox);
                }else {
                    autoLogin = "N";
                    imageButton.setBackgroundResource(R.mipmap.login_unselect_checkbox);
                }
            }
        });

    }


    @Override
    protected void clickActionHandler(View v) {
        int net = spNet.getSelectedItemPosition();
        this.getWmsApplication().setNetWork(net);
        if (R.id.login_action == v.getId()) {
            //登录
            String account = this.etAccount.getText().toString();
            String pwd = this.etPassword.getText().toString();
            if (AppUtil.strNull(account)) {
                ToastUtil.show(this, "请输入用户名");
                return;
            }
            if (AppUtil.strNull(pwd)) {
                ToastUtil.show(this, "请输入密码");
                return;
            }
            login(account,pwd);
        }
    }

    private void login(final String username, final String password){
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, "", "正在登录...", false, false);
        } else {
            progressDialog.setMessage("正在登录...");
        }

        final String url=IFace.WMS_LOGIN+"?username="+username+"&password="+password;

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                passWord=password;
                HttpResultDto httpResult=GsonHelper.convertEntity(response,HttpResultDto.class);
                if(httpResult.getCode()!=null && httpResult.getCode().equals(Status.OK)){
                    loginSuccess(httpResult.getData());
                }else {
                    if (CommonUtils.notEmptyStr(httpResult.getErrMsg())) {
                        ToastUtils.showToast(httpResult.getErrMsg());
                    }else{
                        ToastUtils.showToast(httpResult.getUserMsg());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                ToastUtils.showToast("登录失败请稍后再试");
            }
        });
        VolleyManager.getRequestQueue().add(stringRequest);

    }

    @Override
    protected void requestSuccess(ServerResponse response) {
        super.requestSuccess(response);
    }

    private void loginSuccess(UserProfile data){
        int net = spNet.getSelectedItemPosition();
        this.getWmsApplication().setNetWork(net);
        data.setNetwork(String.valueOf(net));
        String lastUser=AppUtil.getValue(this,WMS_USER);
        if(lastUser!=null && !lastUser.equals(data.getUserId())){
            RealmManager.getInstance().clearDatabase();
        }
        this.putValue(WMS_USER, data.getUserId());
        this.putValue(WMS_IS_CDC_FLAG, data.getIsCDCFlag());
        this.putValue(WMS_USER_PWD, passWord);
        this.putValue(WMS_USER_DISPLAYNAME, data.getUserName());
        this.putValue(WMS_WAREHOUSE_NO, data.getWarehouseNo());
        this.putValue(WMS_IS_LOGIN, "true"); //标识已经登录
        this.putValue(WMS_NETWORK,data.getNetwork());
        this.putValue(WMS_AUTO_LOGIN,autoLogin);
        this.putValue(WMS_TOKEN,data.getToken());
        this.putValue(WMS_USER_ROLE,data.getUserRole());//存入用户角色，用于决定权限
        this.getWmsApplication().setUserProfile(data);
        startActivity(new Intent(LoginActivity.this,
                MainActivity.class));
        this.finish();
    }
}
