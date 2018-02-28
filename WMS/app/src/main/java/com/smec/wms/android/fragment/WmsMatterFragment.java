package com.smec.wms.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.wms.R;
import com.smec.wms.android.adapter.ClassTypeAdapter;
import com.smec.wms.android.adapter.ElevatorTypeAdapter;
import com.smec.wms.android.adapter.MatnrTypeAdapter;
import com.smec.wms.android.adapter.MatterAdapterItemListener;
import com.smec.wms.android.bean.ClassType;
import com.smec.wms.android.bean.ElevatorType;
import com.smec.wms.android.bean.Matnr;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WmsMatterFragment extends Fragment {

    public static final String ELEVATOR_TYPE_LIST = "ELEVATOR_TYPE_LIST";

    private RecyclerView recyclerView ;

//    private List<ElevatorType> elevatorTypeList ;
    private ElevatorTypeAdapter elevatorTypeAdapter ;

//    private List<ClassType> itemTypeList ;
    private ClassTypeAdapter classTypeAdapter ;

//    private List<Matnr> matnrList ;
    private MatnrTypeAdapter matnrAdapter ;

    public FragmentListener fragmentListener ;

    public WmsMatterFragment() {
        // Required empty public constructor
    }

    public static WmsMatterFragment newInstance() {
        WmsMatterFragment fragment = new WmsMatterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wms_matter, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                        if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {

                        }
                    }
                }
            }
        });


        elevatorTypeAdapter = new ElevatorTypeAdapter(this.getActivity());
        classTypeAdapter = new ClassTypeAdapter(this.getActivity());
        matnrAdapter = new MatnrTypeAdapter(this.getActivity());
        if (fragmentListener != null){
            fragmentListener.viewCreated();
        }
        return view;
    }

    public void setElevatorTypeList(ArrayList<ElevatorType> elevatorTypeList, MatterAdapterItemListener listener){
        this.elevatorTypeAdapter.setElevatorTypeList(elevatorTypeList);
        this.elevatorTypeAdapter.setListener(listener);
        if(this.recyclerView == null) {
            return;
        }
        this.recyclerView.setAdapter(elevatorTypeAdapter);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
    }

    public void setClassTypeList(ArrayList<ClassType> classTypeList, MatterAdapterItemListener listener){
        this.classTypeAdapter.setClassTypeList(classTypeList);
        this.classTypeAdapter.setListener(listener);
        if(this.recyclerView == null) {
            return;
        }
        this.recyclerView.setAdapter(classTypeAdapter);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
    }

    public void setMatnrList(ArrayList<Matnr> matnrList,MatterAdapterItemListener listener) {

        this.matnrAdapter.addMatnrList(matnrList);
        this.matnrAdapter.setListener(listener);
        if(this.recyclerView == null) {
            return;
        }
        this.recyclerView.setAdapter(matnrAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    public void clearMatnrList(){
        this.matnrAdapter.setMatnrList(new ArrayList<Matnr>());
    }

    public void clearAll(){
        this.matnrAdapter.setMatnrList(new ArrayList<Matnr>());
        if(this.recyclerView == null) {
            return;
        }
        this.recyclerView.setAdapter(matnrAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    public interface FragmentListener {
        void viewCreated();
        void loadNextPageMatnr();
    }

}
