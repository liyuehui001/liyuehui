package com.version.smec.wms.WmsManager.VolleyManager;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.server.IFace;
import com.version.smec.wms.utils.NetworkState;

import org.apache.http.HttpException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xupeizuo on 2017/8/15.
 * @author xupeizuo
 */

public final class VolleyManager {

    private static final String Volley_base_url=IFace.BASE_URL;
    private static RequestQueue mRequestQueue;
    public static byte[] syncObj=new byte[0];

    private VolleyManager(){

    }

    /**
     * 初始化Volley
     * @param context
     */
    public static void init(Context context){
        if(mRequestQueue == null){
            synchronized (syncObj){
                if(mRequestQueue == null){
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        mRequestQueue.start();
    }

    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    /**
     * 生产Observable调用post请求 Map<String,String>
     * @param url
     * @param map
     * @param headersMap
     * @return
     */
    public static Observable<JSONObject> RxVolleyRequest(final String url, final Map<String,String> map,
                                                         final Map<String, String> headersMap){
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    subscriber.onNext(postRequest(subscriber,url, map, headersMap));
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 生产Observable调用post请求 Map<String,Object>
     * @param url
     * @param map
     * @param headersMap
     * @return
     */
    public static Observable<JSONObject> RxVolleyRequestObj(final String url, final Map<String,Object> map,
                                                         final Map<String, String> headersMap){
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    subscriber.onNext(postRequestObj(subscriber,url, map, headersMap));
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 调度线程,验证有无网络
     * @param observable
     * @param <T>
     * @return
     */
    public static <T>Observable<T>schedulerThread(Observable<T> observable){
        //doOnSubscribe,它和 Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，
        //但区别在于它可以指定线程。
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(!NetworkState.networkConnected(WmsApplication.getContext())){
                            throw new ApiException("","无网络连接");
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * volley 发送post请求 v1.2 Map<String,String> map
     * @param subscriber
     * @param url
     * @param map
     * @param headersMap
     * @return
     * @throws Exception
     */
    private static JSONObject postRequest(final Subscriber subscriber,String url, Map<String,String> map,final Map<String, String> headersMap) throws Exception{

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Volley_base_url+url, new JSONObject(map),
                future, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                subscriber.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headersMap;
            }
        };
        mRequestQueue.add(jsonObjectRequest);
        return future.get();
    }

    /**
     * volley 发送post请求 v1.3 Map<String,Object> map
     * @param subscriber
     * @param url
     * @param map
     * @param headersMap
     * @return
     * @throws Exception
     */
    private static JSONObject postRequestObj(final Subscriber subscriber,String url, Map<String,Object> map,final Map<String, String> headersMap) throws Exception{

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Volley_base_url+url, new JSONObject(map),
                future, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                subscriber.onError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headersMap;
            }
        };
        mRequestQueue.add(jsonObjectRequest);
        return future.get();
    }

}
