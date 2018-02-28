package com.smec.wms.android.adapter;

import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.smec.wms.android.activity.BaseActivity;
import com.smec.wms.android.bean.ResponseData;
import com.smec.wms.android.bean.Status;
import com.smec.wms.android.server.GsonRequest;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.ServerResponse;

import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
public abstract class ServerRequestAdapter extends BaseAdapter implements Response.Listener,Response.ErrorListener{

    private BaseActivity baseActivity;
    private String dataIface;
    private RequestQueue requestQueue;

    protected abstract void updateData(Object data);

    public ServerRequestAdapter(BaseActivity activity,String iface){
        this.baseActivity=activity;
        this.dataIface=iface;
        requestQueue= Volley.newRequestQueue(activity);
    }

    /**
     *  请求数据主方法
     * @param iface 接口名定义在IFace静态字段里
     * @param cls   接口返回值对应的bean
     * @param param 请求参数
     */
    public void request(String iface,Class cls,Map<String,String> param){
        if(iface==null){
            baseActivity.erroHandle(Status.RUNTIME_ERROR,"接口为空");
            return;
        }
        String url= IFace.getFullUrl(iface,param);
        GsonRequest request=new GsonRequest(iface,url,cls,this,this);
        requestQueue.add(request);
    }

    public void request(Class cls,Map<String,String>param){
        this.request(dataIface,cls,param);
    }

    /**
     * 请求数据成功处理
     * @param serverResponse
     */
    public void onResponse(Object serverResponse){
        Object response=((ServerResponse)serverResponse).getData();
        if(response instanceof ResponseData){
            ResponseData responseData=(ResponseData)response;
            if(!Status.OK.equals(responseData.getCode())){
                //数据错误
                baseActivity.erroHandle(responseData.getCode(),responseData.getErrMsg());
                return;
            }
        }
        this.updateData(response);
    }

    /**
     *  请求数据失败处理
     * @param error
     */
    public void onErrorResponse(VolleyError error){
        error.printStackTrace();
        this.baseActivity.erroHandle(Status.ERROR,"获取数据失败");
    }
}
