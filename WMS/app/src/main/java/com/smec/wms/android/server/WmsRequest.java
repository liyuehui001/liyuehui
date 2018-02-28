package com.smec.wms.android.server;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smec.wms.android.application.WmsApplication;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by apple on 16/9/21.
 */

public class WmsRequest<T> implements Response.ErrorListener{

    private String url ;
    private Map<String,String> params ;
    private WmsGsonRequest<T> wmsGsonRequest ;

    private WmsCallback<T> wmsCallback ;

    private static RequestQueue requestQueue ;

    public WmsRequest(String url, Map<String,String> params){
        this.url = url ;
        this.params = params ;
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(WmsApplication.getInstance());
        }
    }

    public void doPost(WmsCallback<T> wmsCallback) {
        this.wmsCallback = wmsCallback ;
        wmsGsonRequest = new WmsGsonRequest<T>(Request.Method.POST,url,this,wmsCallback);
        requestQueue.add(wmsGsonRequest);
    }

    public void doGet(WmsCallback<T> wmsCallback) {
        this.wmsCallback = wmsCallback ;
        wmsGsonRequest = new WmsGsonRequest<T>(Request.Method.GET,url,this,wmsCallback);
        requestQueue.add(wmsGsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(wmsCallback != null) {
            wmsCallback.error(error);
        }
    }

    public static class WmsGsonRequest<T> extends Request<T> {
        private WmsCallback<T> wmsCallback ;
        private Map<String,String> params ;

        Gson gson = new Gson();

        public WmsGsonRequest(int method, String url, Response.ErrorListener listener) {
            super(method, url, listener);
        }

        public WmsGsonRequest(int method, String url, Response.ErrorListener listener, WmsCallback wmsCallback, Map<String,String> params) {
            super(method, url, listener);
            this.wmsCallback = wmsCallback ;
            this.params = params ;
        }

        public WmsGsonRequest(int method, String url, Response.ErrorListener listener, WmsCallback wmsCallback) {
            super(method, url, listener);
            this.wmsCallback = wmsCallback ;
        }

        @Override
        protected Map<String, String> getPostParams() throws AuthFailureError {
            if(params == null){
                return new HashMap<>();
            }
            return params;
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Type type = (new TypeToken<T>(){}).getType();
                T res = (T) gson.fromJson(jsonString,wmsCallback.getTypeToken().getType());
                return Response.success(res,HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                e.printStackTrace();
                return Response.error(new VolleyError(response));
            }
        }

        @Override
        protected void deliverResponse(T response) {
            this.wmsCallback.success(response);
        }
    }

    public interface WmsCallback<T> {
        void success(T response);
        void error(VolleyError error);
        TypeToken getTypeToken();
    }
}
