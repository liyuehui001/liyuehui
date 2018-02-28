package com.version.smec.wms.module.PendingApproval.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.smec.wms.R;
import com.smec.wms.android.application.WmsApplication;
import com.smec.wms.databinding.FragmentPendingOneBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.api.WmsConstant;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.PendingApproval.PendingApprovalActivity;
import com.version.smec.wms.module.PendingApproval.PendingApprovalContract;
import com.version.smec.wms.module.PendingApproval.PendingApprovalPresenter;
import com.version.smec.wms.module.PendingApproval.PendingApprovalViewModel;
import com.version.smec.wms.module.PendingApproval.adapter.PendingAdapterFragmentOne;
import com.version.smec.wms.base.WmsBaseFragment;
import com.version.smec.wms.module.PendingApproval.bean.DiaSearchModel;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;
import com.version.smec.wms.utils.CommonUtils;


import com.version.smec.wms.module.PendingApproval.bean.RequirementModelDto;
import com.version.smec.wms.utils.NetworkState;
import com.version.smec.wms.utils.ToastUtils;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2017/8/14.
 */
public class PendingFragmentHeadquarters extends WmsBaseFragment<PendingApprovalPresenter> implements PendingApprovalContract{

    private FragmentPendingOneBinding fragmentPendingOneBinding;
    private PendingAdapterFragmentOne pendingAdapterFragmentOne;
    private Handler mHandler=new Handler();

    public static final String SEARCH_FAIL="SEARCH_FAIL";
    public static final String SEARCH_SUCCESS="SEARCH_SUCCESS";

    public static final String QUERY_MODEL_BASIC="basic";
    public static final String QUERY_MODEL_ADVANCE="advanced";
    private String searchKeyWord="";
    private String contractNo="";
    private String eleNo="";
    private String matnr="";
    private String matnrName="";
    private String contactPerson="";
    private boolean queryBasic=true;
    public String title="";

    private boolean isShowed=false;
    private boolean todo_true_order_false=false;
    private int currentPage=1;
    private final int pageSize=10;
    private WeakHashMap<String, String> params = new WeakHashMap<>();


    private boolean searchTrueRefreshFalse;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPendingOneBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_pending_one,container,false);
        mPresenter=(PendingApprovalPresenter)((WmsBaseActivity)getActivity()).getPresenter();
        _getDataFromIntent();
        _initView();
        return fragmentPendingOneBinding.getRoot();
    }

    public void _getDataFromIntent(){
        Bundle bundle = getArguments();
        todo_true_order_false = bundle.getBoolean("todo_true_order_false");
    }

    private void _initView(){
        isShowed = true;
        pendingAdapterFragmentOne =new PendingAdapterFragmentOne(getActivity(),todo_true_order_false);
        if(todo_true_order_false && !NetworkState.networkConnected(WmsApplication.getContext())){
            pendingAdapterFragmentOne.setPendinglist(getDataFromDB(0));
        }
        fragmentPendingOneBinding.myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentPendingOneBinding.myRecyclerView.setAdapter(pendingAdapterFragmentOne);
        fragmentPendingOneBinding.ptr.setHeaderView(WmsConstant.getSinaRefreshView());
        fragmentPendingOneBinding.ptr.setBottomView(new LoadingView(getContext()));
        fragmentPendingOneBinding.ptr.setAutoLoadMore(true);
        fragmentPendingOneBinding.ptr.setEnableOverScroll(false);
        fragmentPendingOneBinding.ptr.setEnableLoadmore(true);
        fragmentPendingOneBinding.ptr.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                fragmentPendingOneBinding.myRecyclerView.scrollToPosition(0);//每次刷新滚动到顶部
                currentPage=1;

                _clearConditions(searchTrueRefreshFalse);
                params.put("pageNumber",currentPage+"");
                search(params);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                currentPage++;
                params.put("pageNumber",currentPage+"");
                search(params);
            }
        });

        fragmentPendingOneBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchTrueRefreshFalse = false;
                fragmentPendingOneBinding.ptr.startRefresh();
            }
        },500);
    }



    public static PendingFragmentHeadquarters newInstance() {
        Bundle args = new Bundle();
        PendingFragmentHeadquarters fragment = new PendingFragmentHeadquarters();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PendingApprovalPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void search(WeakHashMap<String, String> map) {
        CommonUtils.hideImmManager(fragmentPendingOneBinding.getRoot());
        map.put("queryModule",todo_true_order_false ? WmsApi.queryModule.headTaskList : WmsApi.queryModule.headOrderList);
        map.put("queryMode",queryBasic ? QUERY_MODEL_BASIC : QUERY_MODEL_ADVANCE);//查询模式
        map.put("condition",((PendingApprovalActivity)getActivity()).getSearchKeyWord());//单号/使用项目
        map.put("contractNo",contractNo);
        map.put("eleNo",eleNo);
        map.put("matnr",matnr);
        map.put("matnrName",matnrName);
        map.put("contactPerson",contactPerson);
        map.put("pageSize",pageSize+"");
        mPresenter.search(map);
    }

    @Override
    public boolean saveDataToDB(int currentFragment,ArrayList<RequirementModel> data) {
        return mPresenter.saveDataToDB(currentFragment,data);
    }

    @Override
    public ArrayList<RequirementModel> getDataFromDB(int currentFragment) {
        return mPresenter.getDataFromDB(currentFragment);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(SEARCH_SUCCESS)
            }
    )
    public void searchSuccess(final RequirementModelDto response){
        if(currentPage == 1){
            pendingAdapterFragmentOne.setPendinglist(response.getData());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingOneBinding.ptr.finishRefreshing();
                }
            },200);
        }else {
            pendingAdapterFragmentOne.addFooter(response.getData());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                fragmentPendingOneBinding.ptr.finishLoadmore();
                }
            },200);
        }
        if(todo_true_order_false){
            saveDataToDB(0,pendingAdapterFragmentOne.getPendinglist());
        }
    }

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(SEARCH_FAIL)
            }
    )
    public void searchFail(RequirementModelDto requirementModelDto){

        if(requirementModelDto !=null && requirementModelDto.getUserMsg()!=null && !requirementModelDto.getUserMsg().equals("")){
            ToastUtils.showToast(requirementModelDto.getUserMsg());
        }

        if(currentPage ==1){
            fragmentPendingOneBinding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingOneBinding.ptr.finishRefreshing();
                }
            },500);
        }else {
            fragmentPendingOneBinding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingOneBinding.ptr.finishLoadmore();
                }
            },500);
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(PendingApprovalActivity.SEARCH)
            }
    )
    public void searchSuccessPendingApprovalViewModel(PendingApprovalViewModel pendingApprovalViewModel){
        if(pendingApprovalViewModel.getCurrentItem() == 0){
            searchKeyWord=pendingApprovalViewModel.getSearchKeyWord();
            searchTrueRefreshFalse = true;
            fragmentPendingOneBinding.ptr.startRefresh();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(PendingApprovalActivity.REFRESH)
            }
    )
    public void refresh(PendingApprovalViewModel pendingApprovalViewModel){
        if(pendingApprovalViewModel.getCurrentItem() == 0){
            searchTrueRefreshFalse = false;
            fragmentPendingOneBinding.ptr.startRefresh();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    //获取到activity发送过来的事件
                    @Tag(PendingApprovalActivity.DIASEARCH)
            }
    )
    public void diasearch(DiaSearchModel diaSearchModel){
        searchKeyWord="";
        if ( diaSearchModel.getCurrentItem()==0){
            if(!TextUtils.isEmpty(diaSearchModel.getContractNo())){
                contractNo =diaSearchModel.getContractNo();
            }else if(!TextUtils.isEmpty(diaSearchModel.getEleNo())){
                eleNo=diaSearchModel.getEleNo();
            }else if(!TextUtils.isEmpty(diaSearchModel.getMatnr())){
                matnr=diaSearchModel.getMatnr();
            }else if(!TextUtils.isEmpty(diaSearchModel.getMatnrName())){
                matnrName=diaSearchModel.getMatnrName();
            }else if(!TextUtils.isEmpty(diaSearchModel.getContactPerson())){
                contactPerson=diaSearchModel.getContactPerson();
            }
            queryBasic = false;

            searchTrueRefreshFalse = true;
            fragmentPendingOneBinding.ptr.startRefresh();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(WmsApi.removeRequireNo)
            }
    )
    public void removeNo(RemoveBean removeBean){
        Log.e("要删除的单号是",removeBean.getReceiptNo());
        pendingAdapterFragmentOne.removeNo(removeBean.getReceiptNo());
        fragmentPendingOneBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchTrueRefreshFalse = true;
                fragmentPendingOneBinding.ptr.startRefresh();
            }
        },300);
    }

    /**
     * true 表示高级查询或者普通查询，不用清空条件
     * @param isSearch true 表示是查询，false 表示刷新
     */
    private void _clearConditions(boolean isSearch){
        if (!isSearch){
            contractNo="";
            eleNo="";
            matnr="";
            matnrName="";
            contactPerson="";
        }
    }
}
