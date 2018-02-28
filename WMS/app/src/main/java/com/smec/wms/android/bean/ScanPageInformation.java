package com.smec.wms.android.bean;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.smec.wms.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adobe on 2016/1/14.
 */
public class ScanPageInformation {
    private List<ScanTimeLine> timeLine;    //时间线显示
    private int barcodeShowViewId;          //条形码扫描结果显示
    private int dataListViewId;             //明细数据显示
    private int summaryViewId;              //汇总信息显示
    private TextView barcodeView;

    private final static int[]TIMELINE_TEXT_3=new int[3];
    private final static int[]TIMELINE_IMAGE_3=new int[3];

    private final static int[]TIMELINE_TEXT_4=new int[4];
    private final static int[]TIMELINE_IMAGE_4=new int[4];

    private final static int[][]TIMELINE_TEXT=new int[5][];
    private final static int[][]TIMELINE_IMAGE=new int[5][];

    static {
        TIMELINE_TEXT_3[0]= R.id.time_line_text_31;
        TIMELINE_TEXT_3[1]= R.id.time_line_text_32;
        TIMELINE_TEXT_3[2]= R.id.time_line_text_33;
        TIMELINE_IMAGE_3[0]=R.id.time_line_image_31;
        TIMELINE_IMAGE_3[1]=R.id.time_line_image_32;
        TIMELINE_IMAGE_3[2]=R.id.time_line_image_33;

        TIMELINE_TEXT_4[0]= R.id.time_line_text_41;
        TIMELINE_TEXT_4[1]= R.id.time_line_text_42;
        TIMELINE_TEXT_4[2]= R.id.time_line_text_43;
        TIMELINE_TEXT_4[3]= R.id.time_line_text_44;
        TIMELINE_IMAGE_4[0]=R.id.time_line_image_41;
        TIMELINE_IMAGE_4[1]=R.id.time_line_image_42;
        TIMELINE_IMAGE_4[2]=R.id.time_line_image_43;
        TIMELINE_IMAGE_4[3]=R.id.time_line_image_44;

        TIMELINE_TEXT[3]=TIMELINE_TEXT_3;
        TIMELINE_IMAGE[3]=TIMELINE_IMAGE_3;
        TIMELINE_TEXT[4]=TIMELINE_TEXT_4;
        TIMELINE_IMAGE[4]=TIMELINE_IMAGE_4;
    }

    private TextView sumaryView;
    private Activity activity;

    public ScanPageInformation(Activity activity) {
        barcodeShowViewId=-1;
        summaryViewId=-1;
        dataListViewId=-1;
        this.activity=activity;
    }

    public int getBarcodeShowViewId() {
        return barcodeShowViewId;
    }

    public void setBarcodeShowViewId(int barcodeShowViewId) {
        this.barcodeShowViewId = barcodeShowViewId;
    }

    public int getDataListViewId() {
        return dataListViewId;
    }

    public void setDataListViewId(int dataListViewId) {
        this.dataListViewId = dataListViewId;
    }

    public int getSummaryViewId() {
        return summaryViewId;
    }

    public void setSummaryViewId(int summaryViewId) {
        this.summaryViewId = summaryViewId;
    }


    public void addTimeLine(String text){
        if(this.timeLine==null){
            this.timeLine=new ArrayList<>();
        }
        this.timeLine.add(new ScanTimeLine(text));
    }

    public TextView getSumaryView() {
        if(sumaryView==null&&this.summaryViewId!=-1){
            sumaryView=(TextView)activity.findViewById(summaryViewId);
        }
        return sumaryView;
    }

    public void setSumaryView(TextView sumaryView) {
        this.sumaryView = sumaryView;
    }

    public TextView getBarcodeView() {
        if(barcodeView==null&&this.barcodeShowViewId!=-1){
            barcodeView=(TextView)activity.findViewById(barcodeShowViewId);
        }
        return barcodeView;
    }

    public void setBarcodeView(TextView barcodeView) {
        this.barcodeView = barcodeView;
    }

    public void renderTimeLine(){
        int size=this.timeLine.size();
        int[]textViewIds=TIMELINE_TEXT[size];
        int[]imagetViewIds=TIMELINE_IMAGE[size];
        for(int i=0;i<size;++i){
            int textViewId=textViewIds[i];
            int imageViewId=imagetViewIds[i];
            ScanTimeLine item=this.timeLine.get(i);
            TextView textView=(TextView)activity.findViewById(textViewId);
            ImageView imageView=(ImageView)activity.findViewById(imageViewId);
            textView.setText(item.getContent());
            item.setTextView(textView);
            item.setImageView(imageView);
            item.setTextViewId(textViewId);
            item.setImageViewId(imageViewId);
        }
    }

    public List<ScanTimeLine> getTimeLine(){
        return this.timeLine;
    }

    public int getTimeLineCount(){
        return this.timeLine.size();
    }


}
