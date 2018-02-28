package com.version.smec.wms.module.Requirements.RequirementsAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smec.wms.BR;
import com.smec.wms.R;
import com.smec.wms.databinding.ItemTodolistViewadapterBinding;
import com.version.smec.wms.api.WmsApi;
import com.version.smec.wms.base.WmsBaseRecycleViewHolder;
import com.version.smec.wms.module.Requirements.bean.RequirementMatnrsModel;
import com.version.smec.wms.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xupeizuo on 2017/8/14.
 */

public class RequirementsAdapter extends RecyclerView.Adapter<WmsBaseRecycleViewHolder> {

    private Context mContext;
    private ArrayList<RequirementMatnrsModel> arrayList=new ArrayList<>();
    private boolean todoTrueBillFalse;
    private String userRole;
    private String isUrgent;

    private HashMap<Integer,String> radioMap=new HashMap<>();



    public RequirementsAdapter(Context context,boolean todoTrueBillFalse,
        String userRolestr,String isUrgentstr){
        this.mContext=context;
        this.todoTrueBillFalse = todoTrueBillFalse;
        this.userRole = userRolestr;
        this.isUrgent = isUrgentstr;
    }

    @Override
    public WmsBaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTodolistViewadapterBinding itemTodolistViewadapterBinding=
                DataBindingUtil.inflate(LayoutInflater.from(mContext),
                        R.layout.item_todolist_viewadapter,parent,false);
        WmsBaseRecycleViewHolder wmsBaseRecycleViewHolder
                =new WmsBaseRecycleViewHolder(itemTodolistViewadapterBinding.getRoot());
        wmsBaseRecycleViewHolder.setBinding(itemTodolistViewadapterBinding);
        wmsBaseRecycleViewHolder.setViewType(viewType);
        return wmsBaseRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(WmsBaseRecycleViewHolder holder, final int position) {
        final ItemTodolistViewadapterBinding itlvab = (ItemTodolistViewadapterBinding)holder.getBinding();
        holder.getBinding().setVariable(BR.matnrs,arrayList.get(position));

        if (arrayList.get(position).getChangedMatnr()==null
                || arrayList.get(position).getChangedMatnr().equals("")){
            itlvab.llMiddleXia.setVisibility(View.GONE);//可替代物料，为空时，不可见
        }

        if (todoTrueBillFalse){//待办详情
            itlvab.llReasonForSend.setVisibility(View.GONE);
            itlvab.llStatusInBoxNum.setVisibility(View.GONE);
            itlvab.llDaiYiJianhuoNum.setVisibility(View.GONE);
            itlvab.llHdqtyNumber.setVisibility(View.VISIBLE);
            if (!userRole.contains(WmsApi.RoleCode.WMSMaintenanceClerk)){

                itlvab.llBottom.removeAllViews();
                TextView tv = new TextView(mContext);
                if (CommonUtils.notEmptyStr(arrayList.get(position).getRemark())){
                    tv.setText("备注："+arrayList.get(position).getRemark());
                }else{
                    tv.setText("备注：");
                }
                itlvab.llBottom.addView(tv);
                itlvab.rgTodoListsIsvalid.removeAllViews();
                if (arrayList.get(position).getLineStatus().equals("Enabled")){
                    itlvab.tvLineStatusHide.setText("行状态：有效");
                }else{
                    itlvab.tvLineStatusHide.setText("行状态：无效");
                }

            }else{

                if (arrayList.get(position).getLineStatus().equals(WmsApi.requirementLineStatus.ENABLED)){
                    itlvab.rbValid.setChecked(true);
                    itlvab.rbInvalid.setChecked(false);
                }else{
                    itlvab.rbValid.setChecked(false);
                    itlvab.rbInvalid.setChecked(true);
                }

                itlvab.etTodoListComments.setFocusable(true);
                itlvab.etTodoListComments.setFocusableInTouchMode(true);
            }

        }else{//单据详情
            itlvab.llReasonForSend.setVisibility(View.VISIBLE);
            itlvab.llStatusInBoxNum.setVisibility(View.VISIBLE);
            itlvab.llDaiYiJianhuoNum.setVisibility(View.VISIBLE);
            itlvab.llHdqtyNumber.setVisibility(View.GONE);
            itlvab.llBottom.setVisibility(View.GONE);

            itlvab.rgTodoListsIsvalid.setVisibility(View.GONE);

        }


        final boolean[] isSpread = {false};
        itlvab.llBottom.setVisibility(View.GONE);
        itlvab.llShuoming.setVisibility(View.GONE);
        if (!todoTrueBillFalse){
            itlvab.llReasonForSend.setVisibility(View.GONE);
            itlvab.llStatusInBoxNum.setVisibility(View.GONE);
            itlvab.llDaiYiJianhuoNum.setVisibility(View.GONE);
        }

        itlvab.tvPackupSpread.setText("查看详情");
        itlvab.ivPackupSpread.setBackgroundResource(R.mipmap.wms_requirement_spread);

        itlvab.rbInvalid.setClickable(false);
        itlvab.rbValid.setClickable(false);

        itlvab.llValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itlvab.rbValid.setChecked(true);
                itlvab.rbInvalid.setChecked(false);
                radioMap.put(position, WmsApi.requirementLineStatus.ENABLED);

            }
        });

        itlvab.llInvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itlvab.rbValid.setChecked(false);
                itlvab.rbInvalid.setChecked(true);
                radioMap.put(position, WmsApi.requirementLineStatus.DISABLED);
            }
        });




        itlvab.llPackupApread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpread[0]){
                    itlvab.llShuoming.setVisibility(View.VISIBLE);
                    itlvab.llBottom.setVisibility(View.VISIBLE);
                    itlvab.tvPackupSpread.setText("收起");
                    itlvab.ivPackupSpread
                            .setBackgroundResource(R.mipmap.wms_requirement_packup);

                    if (!todoTrueBillFalse){
                        itlvab.llReasonForSend.setVisibility(View.VISIBLE);
                        itlvab.llStatusInBoxNum.setVisibility(View.VISIBLE);
                        itlvab.llDaiYiJianhuoNum.setVisibility(View.VISIBLE);
                        itlvab.llBottom.setVisibility(View.GONE);
                    }

                    isSpread[0] = true;
                }else{
                    itlvab.llBottom.setVisibility(View.GONE);
                    itlvab.llShuoming.setVisibility(View.GONE);
                    itlvab.llBottom.clearFocus();
                    itlvab.tvPackupSpread.setText("查看详情");
                    itlvab.ivPackupSpread
                            .setBackgroundResource(R.mipmap.wms_requirement_spread);

                    if (!todoTrueBillFalse){
                        itlvab.llReasonForSend.setVisibility(View.GONE);
                        itlvab.llStatusInBoxNum.setVisibility(View.GONE);
                        itlvab.llDaiYiJianhuoNum.setVisibility(View.GONE);
                        itlvab.llBottom.setVisibility(View.GONE);
                    }
                    isSpread[0] = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<RequirementMatnrsModel> list) {
        if(CommonUtils.notEmpty(list)){
            this.arrayList = list;
            for(int i =0;i<arrayList.size();i++){
                radioMap.put(i,arrayList.get(i).getLineStatus());
            }
        }
    }

    public ArrayList<RequirementMatnrsModel> getArrayList() {
        return arrayList;
    }


    public HashMap<Integer,String> getMap(){
        return radioMap;
    }


}
