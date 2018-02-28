package com.smec.wms.android.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.smec.wms.R;
import com.smec.wms.android.adapter.MatnrTypeAdapter;
import com.smec.wms.android.adapter.MatterAdapterItemListener;
import com.smec.wms.android.bean.ClassType;
import com.smec.wms.android.bean.ClassTypeResponse;
import com.smec.wms.android.bean.ElevatorType;
import com.smec.wms.android.bean.ElevatorTypeResponse;
import com.smec.wms.android.bean.Matnr;
import com.smec.wms.android.bean.MatnrTypeResponse;
import com.smec.wms.android.fragment.WmsMatterFragment;
import com.smec.wms.android.server.IFace;
import com.smec.wms.android.server.WmsRequest;
import com.smec.wms.android.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ImageQueryActivity extends BasePageActivity implements WmsMatterFragment.FragmentListener,MatterAdapterItemListener {

    private WmsMatterFragment wmsMatterFragment ;

    private FragmentManager fragmentManager;

    private EditText elevatorTypeNumText ;
    private EditText classTypeNumText ;
    private EditText matnrTypeNumText ;

    private Button clearQueryBtn ;
    private Button imageQueryBtn ;

    private PhotoView photoView ;

    String elevatorType ;
    String classType ;
    String matnrType ;

    int currentPage = 1;
    boolean moreLoad = true ;

    Stack<StackObject> stackFragment ;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_image_query;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        initView();
        this.enableBackAction();
        this.setTitle("常用备件图片查询");
        stackFragment = new Stack<>();
    }

    @Override
    public boolean backExecute() {

        if(photoView.getVisibility() == View.VISIBLE){
            photoView.setVisibility(View.GONE);
            return false ;
        }

        if (stackFragment.empty()){
            return true ;
        }else {
            stackFragment.pop();
            if (stackFragment.empty()){
                return true;
            }
            StackObject stackObject = stackFragment.peek();

            if("ELEVATOR".equals(stackObject.type)){
                wmsMatterFragment.setElevatorTypeList(stackObject.list,ImageQueryActivity.this);
            }else if("CLASS".equals(stackObject.type)){
                wmsMatterFragment.setClassTypeList(stackObject.list,ImageQueryActivity.this);
            }else {
                return true;
            }
            return false ;
        }
    }

    @Override
    public void onBackPressed() {
        this.clickActionHandler(findViewById(R.id.common_back));
    }

    private void pushToStack(StackObject stackObject){
        if (stackFragment.empty()){
            stackFragment.push(stackObject);
        }else if(!stackFragment.peek().type.equals(stackObject.type)){
            stackFragment.push(stackObject);
        }
    }

    private void initView() {
        elevatorTypeNumText = (EditText)findViewById(R.id.elevatorTypeNumText);
        classTypeNumText = (EditText)findViewById(R.id.classTypeNumText);
        matnrTypeNumText = (EditText)findViewById(R.id.matnrTypeNumText);

        elevatorTypeNumText.setTransformationMethod(new InputLowerToUpper());
        classTypeNumText.setTransformationMethod(new InputLowerToUpper());
        matnrTypeNumText.setTransformationMethod(new InputLowerToUpper());

        clearQueryBtn = (Button)findViewById(R.id.clear_query_btn);
        imageQueryBtn = (Button)findViewById(R.id.image_query_btn);

        photoView = (PhotoView)findViewById(R.id.zoomBigImage);
        photoView.enable();

        imageQueryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentPage = 1 ;
                elevatorType =elevatorTypeNumText.getText().toString();
                classType = classTypeNumText.getText().toString();
                matnrType = matnrTypeNumText.getText().toString();
                requestMatnrType(elevatorType,classType,matnrType);
            }
        });

        clearQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = 1 ;
                elevatorTypeNumText.setText("");
                classTypeNumText.setText("");
                matnrTypeNumText.setText("");
                wmsMatterFragment.clearAll();
            }
        });

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (wmsMatterFragment == null) {
            // 如果ContactsFragment为空，则创建一个并添加到界面上
            wmsMatterFragment = new WmsMatterFragment();
            wmsMatterFragment.fragmentListener = this ;
            transaction.replace(R.id.matter_fragment,wmsMatterFragment);
            transaction.commit();
        }

    }

    private void requestElevatorType(){
        wmsMatterFragment.clearAll();
        WmsRequest<ElevatorTypeResponse> elevatorTypeResponseWmsRequest = new WmsRequest<>(IFace.getFullUrl(IFace.ELEVATOR_TYPE,null),new HashMap<String, String>());
        elevatorTypeResponseWmsRequest.doGet(new WmsRequest.WmsCallback<ElevatorTypeResponse>() {
            @Override
            public void success(ElevatorTypeResponse response) {
                pushToStack(new StackObject("ELEVATOR",response.getEleTypeList()));
                if (response.getEleTypeList() != null && response.getEleTypeList().size() > 0) {
                    wmsMatterFragment.setElevatorTypeList(response.getEleTypeList(),ImageQueryActivity.this);
                }else {
                    ToastUtil.show(ImageQueryActivity.this,response.ErrMsg);
                }
            }

            @Override
            public void error(VolleyError error) {
                ToastUtil.show(ImageQueryActivity.this,"网络出了一点小问题,请稍后再试");
            }

            @Override
            public TypeToken getTypeToken() {
                return new TypeToken<ElevatorTypeResponse>(){};
            }
        });
    }

    private void requestClassType(String elevatorType){
        wmsMatterFragment.clearAll();
        Map map = new HashMap();
        map.put("elevatorType",elevatorType);
        WmsRequest<ClassTypeResponse> elevatorTypeResponseWmsRequest = new WmsRequest<>(IFace.getFullUrl(IFace.CLASS_TYPE,map),new HashMap<String, String>());
        elevatorTypeResponseWmsRequest.doGet(new WmsRequest.WmsCallback<ClassTypeResponse>() {
            @Override
            public void success(ClassTypeResponse response) {
                pushToStack(new StackObject("CLASS",response.getClassList()));
                if (response.getClassList() != null && response.getClassList().size() > 0) {
                    wmsMatterFragment.setClassTypeList(response.getClassList(),ImageQueryActivity.this);
                }else {
                    ToastUtil.show(ImageQueryActivity.this,response.ErrMsg);
                }
            }

            @Override
            public void error(VolleyError error) {
                ToastUtil.show(ImageQueryActivity.this,"网络出了一点小问题,请稍后再试");
            }

            @Override
            public TypeToken getTypeToken() {
                return new TypeToken<ClassTypeResponse>(){};
            }
        });
    }

    private void requestMatnrType(String elevatorType,String classType ,String matra){
        wmsMatterFragment.clearAll();
        elevatorType = elevatorType == null ? "" : elevatorType.toUpperCase();
        classType = classType == null ? "" : classType.toUpperCase();
        matra = matra == null ? "" : matra.toUpperCase();
        Map map = new HashMap();
        map.put("elevatorType",elevatorType);
        map.put("class",classType);
        map.put("matnr",matra);
        map.put("pageNum",currentPage+"");
        map.put("pageSize","30");


//        String url = "http://bpmpublic.smec-cn.com:8011/Dev_Open_Services/?ElevatorType={0}&class={1}&matnr={2}&PageNum={3}&PageSize={4}";
        String fullUrl = IFace.getFullUrl(IFace.MATNR_TYPE,map);
        WmsRequest<MatnrTypeResponse> elevatorTypeResponseWmsRequest = new WmsRequest<>(fullUrl,new HashMap<String, String>());
        elevatorTypeResponseWmsRequest.doGet(new WmsRequest.WmsCallback<MatnrTypeResponse>() {
            @Override
            public void success(MatnrTypeResponse response) {
                pushToStack(new StackObject("MATNR",response.getMatnrList()));
                if (response.getMatnrList() != null && response.getMatnrList().size() > 0) {
                    if (currentPage == 1){
                        wmsMatterFragment.clearMatnrList();
                        moreLoad = true ;
                    }
                    wmsMatterFragment.setMatnrList(response.getMatnrList(),ImageQueryActivity.this);
                }else {
                    if (currentPage > 1){
                        ToastUtil.show(ImageQueryActivity.this,"没有更多的数据");
                        moreLoad = false ;
                    }
                    ToastUtil.show(ImageQueryActivity.this,response.ErrMsg);
                }
            }

            @Override
            public void error(VolleyError error) {
                ToastUtil.show(ImageQueryActivity.this,"网络出了一点小问题,请稍后再试");
            }

            @Override
            public TypeToken getTypeToken() {
                return new TypeToken<MatnrTypeResponse>(){};
            }
        });
    }

    @Override
    public void viewCreated() {
        requestElevatorType();
    }

    @Override
    public void loadNextPageMatnr() {
        if (moreLoad){
            currentPage ++ ;
            requestMatnrType(elevatorType,classType,matnrType);
        }

    }

    @Override
    public void itemClick(Object object, @LayoutRes int itemResId) {
        switch (itemResId){
            //进入品目
            case R.layout.layout_elevator_type :
            {
                ElevatorType elevatorType = (ElevatorType) object ;
                elevatorTypeNumText.setText(elevatorType.getEleType());
                this.elevatorType = elevatorType.getEleType();
                requestClassType(elevatorType.getEleType());
            }
                break ;
            case R.layout.layout_class_type :
            {
                ClassType classType = (ClassType) object ;
                classTypeNumText.setText(classType.getClassType());
                this.classType = classType.getClassType();
                requestMatnrType(elevatorTypeNumText.getText().toString().toUpperCase(),classTypeNumText.getText().toString().toUpperCase(),matnrTypeNumText.getText().toString().toUpperCase());
            }
            break ;
            default:
                break;
        }
    }

    @Override
    public void itemClickWithViewHolder(Object object, @LayoutRes int itemResId, RecyclerView.ViewHolder viewHolder) {
        final Info mRectF ;
        switch (itemResId){
            case R.layout.layout_matnr_type :
            {
                Matnr matnr = (Matnr) object ;
                MatnrTypeAdapter.MatnrTypeViewHolder matnrTypeViewHolder = (MatnrTypeAdapter.MatnrTypeViewHolder) viewHolder;
                photoView.setVisibility(View.VISIBLE);
                mRectF = matnrTypeViewHolder.matnrImage.getInfo();
                Glide.with(ImageQueryActivity.this)
                        .load(Uri.parse(matnr.getDownloadUrl()))
                        .fitCenter()
                        .crossFade()
                        .dontAnimate()
                        .into(photoView);
//                photoView.animaFrom(mRectF);
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        photoView.animaTo(mRectF, new Runnable() {
//                            @Override
//                            public void run() {
                                photoView.setVisibility(View.GONE);
//                            }
//                        });
                    }
                });
            }
            break;
        }
    }

    class InputLowerToUpper extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
            return lower;
        }
        @Override
        protected char[] getReplacement() {
            char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
            return upper;
        }
    }

    class StackObject {
        ArrayList list ;
        String type ;

        public StackObject(){

        }

        public StackObject(String type, ArrayList list) {
            this.type = type;
            this.list = list;
        }
    }
}
