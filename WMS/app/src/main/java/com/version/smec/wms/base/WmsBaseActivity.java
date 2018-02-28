package com.version.smec.wms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.SmecRxBus;
import com.smec.wms.android.activity.BaseActivity;


/**
 * Created by xupeizuo on 2017/8/7.
 */

public abstract class WmsBaseActivity<T> extends BaseActivity {

    protected Bus mRxBus;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxBus= SmecRxBus.get();
        mRxBus.register(this); // 这里的this是指包含了下面注解事件方法的类实例
        mPresenter=getPresenter();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public abstract T getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxBus.unregister(this);
    }
}
