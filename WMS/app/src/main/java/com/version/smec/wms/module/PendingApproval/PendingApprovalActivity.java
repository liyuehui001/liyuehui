package com.version.smec.wms.module.PendingApproval;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.smec.wms.R;
import com.smec.wms.databinding.ActivityPendingApprovalBinding;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadListener;
import com.version.smec.wms.WmsManager.SmecDownloadManager.SmecDownloadManager;
import com.version.smec.wms.base.WmsBaseActivity;
import com.version.smec.wms.module.PendingApproval.bean.DiaSearchModel;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentHeadquarters;
import com.version.smec.wms.module.PendingApproval.fragment.PendingFragmentInside;
import com.version.smec.wms.module.PreviewFile.PreviewFileActivity;


import java.util.ArrayList;
import java.util.WeakHashMap;

public class PendingApprovalActivity extends WmsBaseActivity<PendingApprovalPresenter> {

    private ActivityPendingApprovalBinding mainBinding;
    private ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private PendingApprovalViewModel pendingApprovalViewModel;
    public static final String SEARCH="search";
    public static final String DIASEARCH="diasearch";
    public static final String REFRESH="refresh";
    private int currentItem=0;
    private TodoProjectsCustomDialog todoProjectsCustomDialog;
    private int count=0;
    private EditText edWuliao,ed_lianxiren,ed_wuliaoname,ed_tihao,ed_hetong;
    private DiaSearchModel diaSearchModel=new DiaSearchModel();
    private boolean todo_true_order_false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=DataBindingUtil.setContentView(this,R.layout.activity_pending_approval_);
        pendingApprovalViewModel=new PendingApprovalViewModel();
        setTitle();
        initView();
        chuanzhi();
        mainBinding.setPenViewModel(pendingApprovalViewModel);
    }

    @Override
    public PendingApprovalPresenter getPresenter() {
        return new PendingApprovalPresenter(this);
    }

    public void setTitle(){
        Intent intent=getIntent();
        todo_true_order_false = intent.getBooleanExtra("todo_true_order_false",false);
        if (todo_true_order_false){
            mainBinding.topar.getTvTextTitle().setText("待办详情");
        }else{
            mainBinding.topar.getTvTextTitle().setText("单据查询");
        }

    }

    public void chuanzhi(){

        Bundle bundle = new Bundle();
        bundle.putBoolean("todo_true_order_false",todo_true_order_false);
        for (int i = 0; i < fragmentArrayList.size(); i++) {
            fragmentArrayList.get(i).setArguments(bundle);
        }

    }

    public void initView(){
        fragmentArrayList.add(PendingFragmentHeadquarters.newInstance());
        fragmentArrayList.add(PendingFragmentInside.newInstance());
        mainBinding.viewpager.setAdapter(
                new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentArrayList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentArrayList.size();
            }
        });

        mainBinding.viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    mainBinding.imgHeadquart.setImageResource(R.mipmap.headquartblue);
                    mainBinding.imgInside.setImageResource(R.mipmap.insidegrey);
                    mainBinding.tvHeadquarters.setTextColor(getResources().getColor(R.color.smallblue));
                    mainBinding.tvInside.setTextColor(getResources().getColor(R.color.darkgray));
                }else {
                    mainBinding.imgHeadquart.setImageResource(R.mipmap.headquartgrey);
                    mainBinding.imgInside.setImageResource(R.mipmap.insidebule2);
                    mainBinding.tvHeadquarters.setTextColor(getResources().getColor(R.color.darkgray));
                    mainBinding.tvInside.setTextColor(getResources().getColor(R.color.smallblue));
                }

                currentItem = position;
                mainBinding.edGeneralsearch.setText("");
                pendingApprovalViewModel.setCurrentItem(position);
                mRxBus.post(SEARCH,pendingApprovalViewModel);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mainBinding.edGeneralsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){//搜索
                    generalSearch(mainBinding.edGeneralsearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        mainBinding.topar.getTvTextLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingApprovalActivity.this.finish();
            }
        });
        mainBinding.edGeneralsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && !s.toString().trim().equals("")){
                    mainBinding.ivClear.setVisibility(View.VISIBLE);
                }else {
                    mainBinding.ivClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        mainBinding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.edGeneralsearch.setText("");
            }
        });
    }

    public void  generalSearch(String searchText){
        /**
         * 设置查询字段
         */
        pendingApprovalViewModel.setSearchKeyWord(searchText);
        //发送的事件
        mRxBus.post(SEARCH,pendingApprovalViewModel);
    }

    public void tabClik(View view){
        switch (view.getId()){
            case R.id.btn_headquarters:
                currentItem=0;
                mRxBus.post(SEARCH,pendingApprovalViewModel);
                break;
            case R.id.btn_inside:
                currentItem=1;
                mRxBus.post(SEARCH,pendingApprovalViewModel);
                break;
            case R.id.iv_senior:
                dia();
                break;
        }
        pendingApprovalViewModel.setCurrentItem(currentItem);
        mainBinding.viewpager.setCurrentItem(currentItem);
        mainBinding.invalidateAll();
    }

    public void  dia(){
        todoProjectsCustomDialog=new TodoProjectsCustomDialog(PendingApprovalActivity.this,R.style.project_back);


        todoProjectsCustomDialog.getCustomView().findViewById(R.id.dia_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoProjectsCustomDialog.dismiss();
            }
        });
        todoProjectsCustomDialog.getCustomView().findViewById(R.id.dia_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                huoqu();
                resetDialogText();

            }
        });
        todoProjectsCustomDialog.getCustomView().findViewById(R.id.dia_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                huoqu();
                isnull("contractNo",ed_hetong.getText().toString().trim());
                isnull("eleNo",ed_tihao.getText().toString().trim());
                isnull("matnr",ed_wuliaoname.getText().toString().trim());
                isnull("matnrName",edWuliao.getText().toString().trim());
                isnull("contactPerson",ed_lianxiren.getText().toString().trim());
                mRxBus.post(DIASEARCH,diaSearchModel);
                todoProjectsCustomDialog.dismiss();
            }
        });
        todoProjectsCustomDialog.show();
    }

    public void isnull(String sss,String str){
        if(TextUtils.isEmpty(str)){
            count++;
        }else {
           if(sss.equals("contractNo")){
               diaSearchModel.setContractNo(str);
           }else if(sss.equals("eleNo")){
                diaSearchModel.setEleNo(str);
            }else if(sss.equals("matnr")){
               diaSearchModel.setMatnr(str);
           }else if(sss.equals("matnrName")){
               diaSearchModel.setMatnrName(str);
           }else if(sss.equals("contactPerson")){
               diaSearchModel.setContactPerson(str);
           }
        }

        diaSearchModel.setCurrentItem(currentItem);
    }

    public void huoqu(){
        ed_hetong=(EditText) todoProjectsCustomDialog.getCustomView().findViewById(R.id.ed_hetong);
        ed_tihao=(EditText) todoProjectsCustomDialog.getCustomView().findViewById(R.id.ed_tihao);
        ed_wuliaoname=(EditText) todoProjectsCustomDialog.getCustomView().findViewById(R.id.ed_wuliaoname);
        edWuliao=(EditText) todoProjectsCustomDialog.getCustomView().findViewById(R.id.ed_wuliao);
        ed_lianxiren=(EditText) todoProjectsCustomDialog.getCustomView().findViewById(R.id.ed_lianxiren);
    }

    public void resetDialogText(){
        edWuliao.setText("");
        ed_lianxiren.setText("");
        ed_wuliaoname.setText("");
        ed_tihao.setText("");
        ed_hetong.setText("");
    }

    public String getSearchKeyWord(){
        if(mainBinding.edGeneralsearch.getText()!=null){
            return mainBinding.edGeneralsearch.getText().toString().trim();
        }
        return "";
    }
}
