package com.smec.wms.android.server;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.smec.wms.android.bean.ResponseData;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
@Deprecated
public class GsonRequest extends Request<ServerResponse> {
    private Response.Listener listener;
    private Class mClass;
    private Gson mGson;
    private String iface;
    private Map<String,String> requestParam;

    public GsonRequest( String iface,String url, Class mClass, Response.Listener successListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = successListener;
        this.mClass = mClass;
        mGson = new Gson();
        this.iface=iface;
    }

    public GsonRequest( String iface,String url,Map<String,String> param,Class mClass, Response.Listener successListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = successListener;
        this.mClass = mClass;
        mGson = new Gson();
        this.iface=iface;
        this.requestParam=param;
    }


    @Override
    protected Response<ServerResponse> parseNetworkResponse(NetworkResponse response) {
        String jsonString;
        Object data = null;
        ServerResponse serverResponse=new ServerResponse(this.iface);
        serverResponse.setRequestParams(this.requestParam);
        try {
            jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.e("json",jsonString);
            data = mGson.fromJson(jsonString, mClass);
        } catch (UnsupportedEncodingException e) {
            serverResponse.setData((Object) ResponseData.SERVER_JSON_PARSE_ERROR);
            return Response.success(serverResponse, HttpHeaderParser.parseCacheHeaders(response));
        }
        serverResponse.setData(data);
        return Response.success(serverResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(ServerResponse response) {
        listener.onResponse(response);
    }
    public Map<String,String> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(Map<String,String> requestParam) {
        this.requestParam = requestParam;
    }
}
