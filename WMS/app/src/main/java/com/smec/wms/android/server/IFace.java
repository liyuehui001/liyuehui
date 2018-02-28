package com.smec.wms.android.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Adobe on 2016/1/15.
 */
public class IFace {
    //public static final String BASE_URL="http://10.1.11.118:8011/Mobile_Service/";  //测试环境
    public static  String BASE_URL="http://bpmpublic.smec-cn.com:8011/WmsMobileServiceDEV/";  //测试环境 慢
    //public static final String BASE_URL="http://10.1.11.117:8891/sscapi/service/wms/";            //测试环境 快
//    public static String BASE_URL = "http://bpmpublic.smec-cn.com:8011/WmsMobileService/";      //正式环境

    public static final String GET_TOKEN="http://bpmdev.smec-cn.com:8011/InstDebugDev/instDebugAuthorization/instDebutAuthorizationService";//测试环境



    public static final String GET_TOKEN_TEST="http://bpmpublic.smec-cn.com:8011/WmsMobileServiceDEV/loginAuthorization/wmsLoginAuthorizationService";//测试环境
    public static final String WMS_LOGIN=BASE_URL+"loginAuthorization/wmsLoginAuthorizationService";

//    public static final String GET_TOKEN="";
    //RDC登录
    public static final String RF_LOGIN = "LoginServicePS";   //用户登录
    //RDC分公司入库
    public static final String RF_CHECK_RDC_STOCKIN_ORDER = "RF_Check_RDC_StockIn_Order";     //RDC入库_验证单号
    public static final String RF_CHECK_RDC_STOCKIN_BOXID = "RF_Check_RDC_StockIn_BoxID";     //RDC入库_验证箱号
    public static final String RF_CHECK_RDC_STOCKIN_ALLBOX = "RF_Check_RDC_StockIn_AllBox";   //RDC入库_验证箱号2
    public static final String RF_QUERY_RDC_STOCKLN_DETAILS = "RF_Query_RDC_StockIn_Details"; //RDC入库_收货明细
    public static final String RF_GETORDERNOWITHBOX = "RF_GetOrderNoWithBox";                 //根据箱号获取入库单号

    //RDC分公司上架
    public static final String CHECK_RDCFGS_PUTAWAY_ORDER_NOA = "CheckRDCFGSPutawayOrderNoPS";   //RDC分公司上架_验证单号
    public static final String CHECK_RDCFGS_PUTAWAY_BOX_NO = "CheckRDCFGSPutawayBoxNoPS";     //RDC分公司上架_验证箱号
    public static final String CHECK_RDCFGS_PUTAWAY_MATNR = "CheckRDCFGSPutawayMatnrPS";      //RDC分公司上架_验证物料号
    public static final String RF_COMMIT_RDC_SLOTIN_QTY = "CommitRDCFGSPutawayPS";            //RDC分公司上架_提交操作数量
    public static final String RF_QUERY_RDC_SLOTIN_DETAILS = "GetRDCFGSPutawayDetailPS";      //RDC分公司上架_获取未上架明细


    //RDC分公司收货清点
    public static final String GET_RDCFGS_INBOUND_DETAIL = "GetRdcfgsInboundDetailPS";        //收货清点_获取入库清单


    //盘点
    public static final String RF_CHECK_PD_ORDER = "CheckOrderNoPS";          //验证盘点单号是否有效
    public static final String RF_GET_PD_FINISH_QTY = "GetCheckOrderNoPS";    //获取已盘点的数量
    public static final String RF_COMMIT_PD_QTY = "SubmitCheckOperInfoPS";    //提交盘点操作信息


    //拣货
    public static final String PICK_CHECK_ORDER = "CheckPickOrderPS";         //验证拣货单
    public static final String PICK_CHECK_SHELF = "CheckPickShelfSpacePS";    //验证货位号
    public static final String PICK_CHECK_MATNR = "CheckPickMatnrPS";         //验证物料号
    public static final String PICK_COMMIT_PICK = "CommitPickQtyPS";          //提交拣货信息
    public static final String PICK_GET_DETAIL = "GetPickDetailPS";           //获取拣货操作明细


    //装箱
    public static final String PACK_CHECK_ORDER = "CheckOutboundOrderPS";         //验证拣货单是否完成
    public static final String PACK_CHECK_BOX = "CheckOutboundBoxNumPS";          //判断箱号规则
    public static final String PACK_CHECK_MATNR = "CheckOutboundOrderMatnrPS";    //验证装箱物料号
    public static final String PACK_COMMIT_QTY = "CommitOutboundQtyPS";           //提交出库装箱数量
    public static final String PACK_COMMIT_QTY_SNPS = "CommitOutboundQtySNPS";    //提交出库印版物料序列号
    public static final String PACK_GET_DETAIL = "GetOutboundDetailPS";           //查询装箱复核明细
    public static final String PACK_QUERY_UNBIND = "QueryUnbindBoxlistPS";        //未绑定箱子明细查询
    public static final String PACK_RELATION_BOX = "SetRelationOfBoxStandardPS";  //关联规格


    //箱子绑定
    public static final String BIND_CHECK_BOX_STANDARD = "CheckBoxStandardPS";    //验证箱子规格

    //移库
    public static final String MOVE_COMMIT_QTY = "CommitMoveQtyPS";               //提交移库信息


    //common
    public static final String COMMON_CHECK_SHELF_NO = "CommonCheckShelfSpaceNoPS";   //验证货位
    public static final String COMMON_QUERY_MATNR = "CommonQueryMatnrStocksPS";       //请求物料信息
    public static final String COMMON_QUERY_SHELF = "CommonQueryShelfSapceInfoPS";    //请求货位库存

    //版本检查
    public static final String WMS_VERSION_CHECK = "VersionCheckPS";                  //版本检查

    //库存清单查询
    public static final String STOCK_QUERY_SERVICE = "QueryWmsStockServicePS";        //查询库存清单

    //物料查询
    public static final String MATNR_OTHER_QUERY_SERVICE = "WmsGetMatnrByDwgnoPS";        //查询库存清单

    //物料查询
    public static final String MATNR_QUERY_SERVICE = "QueryMatnrListServicePS";       //查询物料
    public static final String MATNR_QUERY_PRICE = "QueryProductPriceServicePS";      //获取物料价格

    //发运信息查询
    public static final String DELIVERY_GET_DELIVERY_DATA="GetDeliveryDataPS";      //通过箱号返回出库单号和发运地址
    public static final String DELIVERY_COMMIT="CommitDeliveryDataPS";              //提交发运信息
    public static final String DELIVERY_QUERY_PRICE="QueryBoxTotalPricePS";         //查询报价

    //成品装箱清单查询
    public static final String CPZX_QUERY="MntPackPortPS";

    //获取梯种信息
    public static final String ELEVATOR_TYPE = "getEleType/getEleTypeService";

    //获取品目信息
    public static final String CLASS_TYPE = "getMatnrClass/getMatnrClassService";

    //获取物料信息
    public static final String MATNR_TYPE = "getMatnrImage/getMatnrImageService";

    public static final String GET_PROJECT_LIST="getOrderList/getOrderListService"; //获取单据列表
    public static final String GET_PROJECT_DETAILS="getOrderDetail/getOrderDetailService"; //获取单据详情

    public static final String GET_LOGISTICS_LIST="getLogisticsList/getLogisticsListService";//获取物流列表
    // getLogisticsList/getLogisticsListService

    public static final String GET_LOGISTICS_DETAIL="getLogisticsDetail/getLogisticsDetailService";//获取物流详细


    public static final String GET_LOGS_LIST = "getProcessHist/getProcessHistService";//获取日志列表

    public static final String APPROVE_CHECK="approvalVerification/approvalVerificationService";//审批校验 接口

    public static final String GET_REQUIREMENT_APPROVE="approveReqOrder/approveReqOrderRest";//需求单审批

    public static final String GET_SALES_APPROVE="approveSalesOrder/approveSalesOrderRest";//销售单审批

    public static final String GET_BORROW_APPROVE="approveBorrowOrder/approveBorrowOrderRest";//借用单审批

    public static final String GET_ACCEPTANCE_APPROVE="approveReceiveOrder/approveReceiveOrderRest";//领用单审批

    public static final int NETWORK_INTERNET=0;
    public static final int NETWORK_WLAN=1;
    public static final int NETWORK_DEV=2;

    /**
     * 返回接口完整路径
     *
     * @param uri
     * @param params
     * @return
     */
    public static String getFullUrl(String uri, Map<String, String> params) {
        StringBuffer buf = new StringBuffer(BASE_URL + uri);
        if (RF_LOGIN.equals(uri)) {
            buf = new StringBuffer(BASE_URL + uri);
        }
        if (params != null && !params.isEmpty()) {
            buf.append("?");
        }
        if (params == null) {
            return buf.toString();
        }
        boolean flag = false;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null || "null".equals(value)) {
                value = "";
            }
            if (flag) {
                buf.append("&");
            }
            flag = true;
            buf.append(key);
            buf.append("=");
            try {
                buf.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buf.toString();
    }

    public static void setNetwork(int type) {
        if (type==NETWORK_WLAN) {
            BASE_URL = "http://bpm.smec-cn.com:8011/WmsMobileService/";
        } else if (type==NETWORK_DEV) {
            BASE_URL = "http://bpmpublic.smec-cn.com:8011/WmsMobileServiceDEV/";
        } else {
            BASE_URL = "http://bpmpublic.smec-cn.com:8011/WmsMobileService/";
        }
    }
}
