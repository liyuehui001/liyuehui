package com.version.smec.wms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.SmecRxBus;
import com.smec.wms.android.fragment.BaseFragment;

/**
 * Created by xupeizuo on 2017/8/7.
 */

public abstract class WmsBaseFragment<T> extends Fragment{

    protected Bus mRxBus;
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxBus= SmecRxBus.get();
        mRxBus.register(this);
        mPresenter=getPresenter();
    }

    protected abstract T getPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRxBus.unregister(this);
    }

}
