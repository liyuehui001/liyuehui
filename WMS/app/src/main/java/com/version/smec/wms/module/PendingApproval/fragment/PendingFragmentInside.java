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
import com.smec.wms.databinding.FragmentPendingTwoBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.api.WmsConstant;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.base.WmsBaseFragment;
import com.version.smec.wms.bean.RemoveBean;
import com.version.smec.wms.module.PendingApproval.PendingApprovalActivity;
import com.version.smec.wms.module.PendingApproval.PendingApprovalContract;
import com.version.smec.wms.module.PendingApproval.PendingApprovalPresenter;
import com.version.smec.wms.module.PendingApproval.PendingApprovalViewModel;
import com.version.smec.wms.module.PendingApproval.adapter.PendingAdapterFragmentInside;
import com.version.smec.wms.module.PendingApproval.bean.DiaSearchModel;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;
import com.version.smec.wms.module.PendingApproval.bean.RequirementModelDto;
import com.version.smec.wms.utils.CommonUtils;
import com.version.smec.wms.utils.NetworkState;
import com.version.smec.wms.utils.ToastUtils;

import java.util.ArrayList;
import java.util.WeakHashMap;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/8/14.
 */
public class PendingFragmentInside extends WmsBaseFragment<PendingApprovalPresenter> implements PendingApprovalContract{

    private FragmentPendingTwoBinding fragmentPendingTwoBinding;
    private PendingAdapterFragmentInside pendingAdapterFragmentInside;
    private Handler mHandler=new Handler();

    public static final String BRANCH_SEARCH_FAIL="BRANCH_SEARCH_FAIL";
    public static final String BRANCH_SEARCH_SUCCESS="BRANCH_SEARCH_SUCCESS";
    public static final String QUERY_MODEL_BASIC="basic";
    public static final String QUERY_MODEL_ADVANCE="advanced";

    private boolean queryBasic=true;
    private String searchKeyWord="";
    private String contractNo="";
    private String eleNo="";
    private String matnr="";
    private String matnrName="";
    private String contactPerson="";
    private boolean todo_true_order_false=false;
    private int currentPage=1;
    private final int pageSize=10;
    private WeakHashMap<String, String> params = new WeakHashMap<>();

    private boolean searchTrueRefreshFalse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPendingTwoBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_pending_two,container,false);
        mPresenter=(PendingApprovalPresenter)((WmsBaseActivity)getActivity()).getPresenter();
        _getDataFromIntent();
        _initView();
        return fragmentPendingTwoBinding.getRoot();
    }

    public void _getDataFromIntent(){
        Bundle bundle = getArguments();
        todo_true_order_false = bundle.getBoolean("todo_true_order_false");
    }

    private void _initView() {
        pendingAdapterFragmentInside=new PendingAdapterFragmentInside(getActivity(),todo_true_order_false);
        if(todo_true_order_false && !NetworkState.networkConnected(WmsApplication.getContext())){
            pendingAdapterFragmentInside.setPendinglist(getDataFromDB(1));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        fragmentPendingTwoBinding.myRecyclerViewInsde.setLayoutManager(layoutManager);
        fragmentPendingTwoBinding.myRecyclerViewInsde.setAdapter(pendingAdapterFragmentInside);
        fragmentPendingTwoBinding.ptrInside.setHeaderView(WmsConstant.getSinaRefreshView());
        fragmentPendingTwoBinding.ptrInside.setBottomView(new LoadingView(getContext()));
        fragmentPendingTwoBinding.ptrInside.setAutoLoadMore(true);
        fragmentPendingTwoBinding.ptrInside.setEnableOverScroll(false);
        fragmentPendingTwoBinding.ptrInside.setEnableLoadmore(true);
        fragmentPendingTwoBinding.ptrInside.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                fragmentPendingTwoBinding.myRecyclerViewInsde.scrollToPosition(0);//每次刷新滚动到顶部
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

    }

    @Override
    protected PendingApprovalPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void search(WeakHashMap<String, String> map) {
        CommonUtils.hideImmManager(fragmentPendingTwoBinding.getRoot());
        map.put("queryModule",todo_true_order_false ? WmsApi.queryModule.branchTaskList : WmsApi.queryModule.branchOrderList);
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
    public boolean saveDataToDB(int currentFragment, ArrayList<RequirementModel> data) {
        return mPresenter.saveDataToDB(currentFragment, data);
    }

    @Override
    public ArrayList<RequirementModel> getDataFromDB(int currentFragment) {
        return mPresenter.getDataFromDB(currentFragment);
    }

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BRANCH_SEARCH_SUCCESS)
            }
    )

    public void searchSuccess(final RequirementModelDto response){
        if(currentPage == 1){
            pendingAdapterFragmentInside.setPendinglist(response.getData());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingTwoBinding.ptrInside.finishRefreshing();
                }
            },200);
        }
        else {
            pendingAdapterFragmentInside.addFooter(response.getData());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingTwoBinding.ptrInside.finishLoadmore();
                }
            },200);
        }
        if(todo_true_order_false){
            saveDataToDB(1,pendingAdapterFragmentInside.getPendinglist());
        }
    }

    @Subscribe(

            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BRANCH_SEARCH_FAIL)
            }
    )

    public void searchFail(RequirementModelDto requirementModelDto){

        if(requirementModelDto !=null && requirementModelDto.getUserMsg()!=null && !requirementModelDto.getUserMsg().equals("")){
            ToastUtils.showToast(requirementModelDto.getUserMsg());
        }

        if(currentPage ==1){
            fragmentPendingTwoBinding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingTwoBinding.ptrInside.finishRefreshing();
                }
            },500);
        }else {
            fragmentPendingTwoBinding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentPendingTwoBinding.ptrInside.finishLoadmore();
                }
            },500);
        }
    }

    public static PendingFragmentInside newInstance() {
        Bundle args = new Bundle();
        PendingFragmentInside fragment = new PendingFragmentInside();
        fragment.setArguments(args);
        return fragment;
    }
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    //获取到activity发送过来的事件
                    @Tag(PendingApprovalActivity.SEARCH)
            }
    )
    public void search(PendingApprovalViewModel pendingApprovalViewModel){
        if(pendingApprovalViewModel.getCurrentItem() == 1){
            searchKeyWord=pendingApprovalViewModel.getSearchKeyWord();
            searchTrueRefreshFalse = true;
            fragmentPendingTwoBinding.ptrInside.startRefresh();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    //获取到activity发送过来的事件
                    @Tag(PendingApprovalActivity.REFRESH)
            }
    )
    public void refresh(PendingApprovalViewModel pendingApprovalViewModel){
        if(pendingApprovalViewModel.getCurrentItem() == 1){
            searchTrueRefreshFalse = false;
            fragmentPendingTwoBinding.ptrInside.startRefresh();
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
        if (diaSearchModel.getCurrentItem()==1){
            if(!TextUtils.isEmpty(diaSearchModel.getContractNo())){
                contractNo = diaSearchModel.getContractNo();
            }else if(!TextUtils.isEmpty(diaSearchModel.getEleNo())){
                eleNo = diaSearchModel.getEleNo();
            }else if(!TextUtils.isEmpty(diaSearchModel.getMatnr())){
                matnr = diaSearchModel.getMatnr();
            }else if(!TextUtils.isEmpty(diaSearchModel.getMatnrName())){
                matnrName = diaSearchModel.getMatnrName();
            }else if(!TextUtils.isEmpty(diaSearchModel.getContactPerson())){
                contactPerson = diaSearchModel.getContactPerson();
            }
            queryBasic = false;

            searchTrueRefreshFalse = true;
            fragmentPendingTwoBinding.ptrInside.startRefresh();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {

                    @Tag(WmsApi.removeReceiptNo)
            }
    )
    public void removeNo(RemoveBean removeBean){
        Log.e("要删除的单号是",removeBean.getReceiptNo());
        pendingAdapterFragmentInside.removeNo(removeBean.getReceiptNo());
        fragmentPendingTwoBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchTrueRefreshFalse = false;
                fragmentPendingTwoBinding.ptrInside.startRefresh();
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
