package com.version.smec.wms.api;

/**
 * Created by xupeizuo on 2017/8/7.
 */

public interface WmsApi {

    /**
     * 审批成功后删除列表数据,防止在无网条件下还能看到列表数据
     */
    String removeReceiptNo="removeReceiptNo";
    String removeRequireNo="removeRequireNo";

    interface RoleCode{
        String WMSMaintenanceClerk="WMSMaintenanceClerk";//总部备件管理员
        String REMES_STATION_CAPTAIN="REMES_STATION_CAPTAIN";//维保站站长
        String MNT_DEPT_APPROVER = "MNT_DEPT_APPROVER";//维保部科级审核人
        String WmsWHAdministrators = "WmsWHAdministrators";//分公司备件管理员
        String WMS_BRANCH_MGR = "WMS_BRANCH_MGR"; //总经理包含的字符串，
    }

    interface  billForm{
        String requireForm = "WMS_XQ";//需求单
        String borrowForm = "WMS_JY";//借y单
        String acceptForm = "WMS_LY";//领y单
        String saleForm = "WMS_XS";//销售求单

    }

    interface requestCode{
        String requestSuccess="SUCCESS";
        String requestFailed="FAILED";
        String reqtestConfirm="CONFIRM";
    }

    interface queryModule{
        String headOrderList="headOrderList";//(总部订单查询列表）
        String headTaskList="headTaskList";//（总部订单待办列表）
        String branchOrderList="branchOrderList";//分公司订单查询列表）
        String branchTaskList="branchTaskList";//分公司订单待办列表）
    }

    interface docStatus{
        String closeStatus = "CLOSED";
    }


    interface tip {
        String noRecordPhone = "没有记录电话号码";
        String NowNotAvailable = "暂无";
    }
    interface approveCode{
        String APPROVED="APPROVED";
        String REJECTED="REJECTED";
    }


    interface logsOperation{
        String REFUSE = "驳回";
        String REFUSE_APPLY = "驳回申请";
    }

    interface saleType{
        String SALESTYPE_tripartite_agreement1= "三方协议";
        String getSALESTYPE_tripartite_agreement2 = "三方合同";
    }

    interface requirementLineStatus{
        String ENABLED = "Enabled";
        String DISABLED = "Disabled";
    }
}

