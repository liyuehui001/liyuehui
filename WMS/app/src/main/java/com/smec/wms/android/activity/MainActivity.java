package com.smec.wms.android.activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.android.bean.Version;
import com.smec.wms.android.fragment.MessageFragment;
import com.smec.wms.android.fragment.MyFragment;
import com.smec.wms.android.fragment.WmsHomeFragment;
import com.smec.wms.android.fragment.WmsInboundFragment;
import com.smec.wms.android.fragment.WmsOtherFragment;
import com.smec.wms.android.fragment.WmsOutboundFragment;
import com.smec.wms.android.fragment.WmsQueryFragment;
import com.smec.wms.android.fragment.WmsUserFragment;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.util.HttpUtils;
import com.smec.wms.android.util.ToastUtil;
import com.smec.wms.android.view.ChangeColorIconWithText;

public class MainActivity extends FragmentActivity implements OnClickListener,
        OnPageChangeListener, Runnable {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private TextView tvTitle;

    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    private long exitTime = 0;

    private WmsApplication app ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_common_title);
        app = (WmsApplication) this.getApplication();
        initView();
        initEvent();

        //检测版本
        Thread thread = new Thread(this);
        thread.start();
        WmsApplication.lastChecmVersion=System.currentTimeMillis();
//        Version test=new Version();
//        test.setCurVersion("1.2.2.2");
//        test.setHaveUpdate("Y");
//        test.setUpdatePolicy("FORCE");
//        this.showUpdateDialog(test);
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {

        mViewPager.setOnPageChangeListener(this);

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        tvTitle = (TextView) findViewById(R.id.layout_title);
        tvTitle.setText(String.format("工作台-%s", app.getUserProfile().getDsiplayName()));
        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);

        WmsInboundFragment tabWmsInboundFragment = new WmsInboundFragment();
        WmsOutboundFragment tabWmsOutboundFragment = new WmsOutboundFragment();
        WmsOtherFragment tabFragment3 = new WmsOtherFragment();
        WmsUserFragment tabFragment4 = new WmsUserFragment();

        WmsHomeFragment wmsHomeFragment = WmsHomeFragment.newInstance(null,null);
        mTabs.add(wmsHomeFragment);
//        mTabs.add(tabWmsInboundFragment);
        MessageFragment messageFragment = new MessageFragment();
        mTabs.add(messageFragment);
//        mTabs.add(tabWmsOutboundFragment);
        WmsQueryFragment wmsQueryFragment = new WmsQueryFragment();
//        mTabs.add(tabFragment3);
        mTabs.add(wmsQueryFragment);
        MyFragment myFragment = new MyFragment();
//        mTabs.add(tabFragment4);
        mTabs.add(myFragment);


        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {
        clickTab(v);

    }

    /**
     * 点击Tab按钮
     *
     * @param v
     */
    private void clickTab(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
//                mTabIndicators.get(0).setIconAlpha(1.0f);
                mTabIndicators.get(0).setIconDrawable(R.mipmap.select_home,R.color.blue_user);
                mViewPager.setCurrentItem(0, false);
                tvTitle.setText(String.format("工作台-%s", app.getUserProfile().getDsiplayName()));
                break;
            case R.id.id_indicator_two:
//                mTabIndicators.get(1).setIconAlpha(1.0f);
                mTabIndicators.get(1).setIconDrawable(R.mipmap.select_message,R.color.blue_user);
                mViewPager.setCurrentItem(1, false);
                tvTitle.setText("消息");
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconDrawable(R.mipmap.select_list,R.color.blue_user);
                mViewPager.setCurrentItem(2, false);
                tvTitle.setText("查询列表");
                break;
            case R.id.id_indicator_four:
                tvTitle.setText("");
                mTabIndicators.get(3).setIconDrawable(R.mipmap.select_my,R.color.blue_user);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        mTabIndicators.get(0).setIconDrawable(R.mipmap.unselect_home,R.color.black_user);
        mTabIndicators.get(1).setIconDrawable(R.mipmap.unselect_message,R.color.black_user);
        mTabIndicators.get(2).setIconDrawable(R.mipmap.unselect_list,R.color.black_user);
        mTabIndicators.get(3).setIconDrawable(R.mipmap.unselect_my,R.color.black_user);
//        for (int i = 0; i < mTabIndicators.size(); i++) {
//            mTabIndicators.get(i).setIconAlpha(0);
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0) {
//            ChangeColorIconWithText left = mTabIndicators.get(position);
//            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
//            left.setIconAlpha(1 - positionOffset);
//            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        resetOtherTabs();
        switch (position) {
            case 0:
//                mTabIndicators.get(0).setIconAlpha(1.0f);
                mTabIndicators.get(0).setIconDrawable(R.mipmap.select_home, R.color.blue_user);
                mViewPager.setCurrentItem(0, false);
                tvTitle.setText("工作台");
                break;
            case 1:
//                mTabIndicators.get(1).setIconAlpha(1.0f);
                mTabIndicators.get(1).setIconDrawable(R.mipmap.select_message, R.color.blue_user);
                mViewPager.setCurrentItem(1, false);
                tvTitle.setText("消息");
                break;
            case 2:
                mTabIndicators.get(2).setIconDrawable(R.mipmap.select_list, R.color.blue_user);
                mViewPager.setCurrentItem(2, false);
                tvTitle.setText("查询列表");
                break;
            case 3:
                mTabIndicators.get(3).setIconDrawable(R.mipmap.select_my, R.color.blue_user);
                mViewPager.setCurrentItem(3, false);
                tvTitle.setText("");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            if (curTime - exitTime > 2000) {
                exitTime = curTime;
                ToastUtil.show(this, "再按一次退出");
            } else {
                exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        long curTime = System.currentTimeMillis();
        //每五分钟检测一次版本
        if (curTime - WmsApplication.lastChecmVersion > 10 * 60 * 1000) {
            WmsApplication.lastChecmVersion = curTime;
//            Version test = new Version();
//            test.setCurVersion("1.2.2.2");
//            test.setHaveUpdate("Y");
//            test.setUpdatePolicy("FORCE");
//            test.setApkURL("http://www.baidu.com");
//            this.showUpdateDialog(test);
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        String curVersion = WmsApplication.getVersion();
        Map<String, String> params = new HashMap<String, String>();
        params.put("version",curVersion);
        String url = IFace.getFullUrl(IFace.WMS_VERSION_CHECK,params);
        String html = HttpUtils.doGet(url);
        Gson gjson = new Gson();
        try {
            Version version = gjson.fromJson(html, Version.class);
            Message message = new Message();
            message.what = 1;
            message.obj = version;
            mHandler.sendMessage(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }


    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Version version = (Version) msg.obj;
                if (version!=null&&version.isHaveUpdate()) {
                    showUpdateDialog(version);
                }
            }
        }
    };

    public void showUpdateDialog(final Version version) {
        String msg = null;
        if (Version.UPDATE_POLICY_FORCE.equals(version.getUpdatePolicy())) {
            msg = String.format("发现新版本%s该版本要求强制升级请点击确定下载安装最新程序", version.getCurVersion());
        } else {
            msg = String.format("发现新版本%s是否升级");
        }
        AlertDialog.Builder builde = new AlertDialog.Builder(this);
        builde.setTitle("升级");
        builde.setMessage(msg);
        builde.setCancelable(false);
        if (Version.UPDATE_POLICY_FORCE.equals(version.getUpdatePolicy())) {
            //强制升级
            builde.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, false);
                        openURL(version.getApkURL());
                    } catch (Exception ex) {

                    }
                }
            });

            builde.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exit();
                }
            });
        } else if (Version.UPDATE_POLICY_OPTION.equals(version.getUpdatePolicy())) {
            //可选升级
            builde.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openURL(version.getApkURL());
                }
            });
            builde.setNegativeButton("取消", null);
        }
        builde.show();
    }

    private void openURL(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }
}
