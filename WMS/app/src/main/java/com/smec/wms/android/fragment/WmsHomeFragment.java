package com.smec.wms.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.android.activity.BoxBindActivity;
import com.smec.wms.android.activity.BoxOnlineScanActivity;
import com.smec.wms.android.activity.CheckMatnrActivity;
import com.smec.wms.android.activity.DeliveryActivity;
import com.smec.wms.android.activity.HomeListActivity;
import com.smec.wms.android.activity.MoveActivity;
import com.smec.wms.android.activity.PackActivity;
import com.smec.wms.android.activity.PickActivity;
import com.smec.wms.android.activity.PutAwayScanActivity;
import com.smec.wms.android.bean.ListRouterBean;
import com.version.smec.wms.module.PendingApproval.PendingApprovalActivity;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentHeadquarters;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentInside;
import com.version.smec.wms.module.Requirements.RequirementsActivity;
import com.version.smec.wms.widget.TopBarLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WmsHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WmsHomeFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TopBarLayout topBarLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup homeTodo ;
    private ViewGroup homeList ;
    private ViewGroup homeIn ;
    private ViewGroup homeOut ;
    private ViewGroup homeBorrow ;
    private ViewGroup homeOther ;

    public WmsHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WmsHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WmsHomeFragment newInstance(String param1, String param2) {
        WmsHomeFragment fragment = new WmsHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wms_home, container, false);
        initView(view);
        return view ;
    }

    private void initView(View view) {
        homeTodo = (ViewGroup) view.findViewById(R.id.home_todo);
        homeList = (ViewGroup) view.findViewById(R.id.home_list);
        homeBorrow = (ViewGroup) view.findViewById(R.id.home_borrow);
        homeIn = (ViewGroup) view.findViewById(R.id.home_in);
        homeOut = (ViewGroup) view.findViewById(R.id.home_out);
        homeOther = (ViewGroup) view.findViewById(R.id.home_other);

        homeTodo.setOnClickListener(this);
        homeList.setOnClickListener(this);
        homeBorrow.setOnClickListener(this);
        homeIn.setOnClickListener(this);
        homeOut.setOnClickListener(this);
        homeOther.setOnClickListener(this);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onClick(){

    }

    @Override
    public void onClick(View view) {
        ArrayList<ListRouterBean> routerBeenList = new ArrayList<>();
        Intent intent = new Intent(this.getActivity(), HomeListActivity.class) ;

        switch (view.getId()) {
            case R.id.home_todo ://待办事项
                Intent intent1 = new Intent(getActivity(), PendingApprovalActivity.class);
                intent1.putExtra("todo_true_order_false",true);
                getActivity().startActivity(intent1);
                break;
            case R.id.home_in :
                //出库
                ListRouterBean pickRouterBean = new ListRouterBean(R.mipmap.packing,"拣货", PickActivity.class);
                ListRouterBean packRouterBean = new ListRouterBean(R.mipmap.boxing,"装箱", PackActivity.class);
                ListRouterBean boxBindBean = new ListRouterBean(R.mipmap.link,"箱子绑定", BoxBindActivity.class);
                ListRouterBean DeliveryBean = new ListRouterBean(R.mipmap.logistics,"物流管理", DeliveryActivity.class);

                routerBeenList.add(pickRouterBean);
                routerBeenList.add(packRouterBean);
                routerBeenList.add(boxBindBean);
                routerBeenList.add(DeliveryBean);

                intent.putExtra(HomeListActivity.HOME_LIST_PARAM,routerBeenList);
                intent.putExtra(HomeListActivity.HOME_LIST_TITLE_PARAM,"出库管理");
                startActivity(intent);

                break;
//            case R.id.home_borrow :
//                break;
            case R.id.home_list :
                Intent intentlist = new Intent(getActivity(), PendingApprovalActivity.class);
                intentlist.putExtra("todo_true_order_false",false);
                getActivity().startActivity(intentlist);
                break;
            case R.id.home_out :
                //入库
                ListRouterBean boxOnlineRouterBean = new ListRouterBean(R.mipmap.ruku_icon,"入库", BoxOnlineScanActivity.class);
                ListRouterBean putAwayRouterBean = new ListRouterBean(R.mipmap.shangjia_icon,"上架", PutAwayScanActivity.class);
                routerBeenList.add(boxOnlineRouterBean);
                routerBeenList.add(putAwayRouterBean);

                intent.putExtra(HomeListActivity.HOME_LIST_PARAM,routerBeenList);
                intent.putExtra(HomeListActivity.HOME_LIST_TITLE_PARAM,"入库管理");

                startActivity(intent);
                break;
            case R.id.home_other :
                ListRouterBean moveRouterBean = new ListRouterBean(R.mipmap.yiku_icon,"移库", MoveActivity.class);
                ListRouterBean checkMatnrRouterBean = new ListRouterBean(R.mipmap.pandian,"盘点", CheckMatnrActivity.class);
                routerBeenList.add(moveRouterBean);
                routerBeenList.add(checkMatnrRouterBean);

                intent.putExtra(HomeListActivity.HOME_LIST_PARAM,routerBeenList);
                intent.putExtra(HomeListActivity.HOME_LIST_TITLE_PARAM,"其他功能");

                startActivity(intent);
                break;
        }
    }
}

