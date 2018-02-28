package com.version.smec.wms.module.PendingApproval;

import com.version.smec.wms.module.PendingApproval.bean.RequirementModel;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * Created by xupeizuo on 2017/8/15.
 */

public interface PendingApprovalContract {

    void search(WeakHashMap<String,String> map);

    boolean saveDataToDB(int currentFragment,ArrayList<RequirementModel> data);

    ArrayList<RequirementModel> getDataFromDB(int currentFragment);
}
