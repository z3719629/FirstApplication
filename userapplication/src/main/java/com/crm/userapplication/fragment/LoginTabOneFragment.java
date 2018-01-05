package com.crm.userapplication.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.userapplication.R;
import com.crm.userapplication.adapter.RecyclerViewAdapter;
import com.crm.userapplication.adapter.SimplePaddingDecoration;
import com.crm.userapplication.databinding.FragmentLoginBinding;
import com.crm.userapplication.rxbus.Events;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2018/1/4.
 */
public class LoginTabOneFragment extends BaseFragment {

    private FragmentLoginBinding mBinding;

    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04","List Item 01", "List Item 02", "List Item 03", "List Item 04"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView(mBinding.recyclerView);
    }

    @Override
    protected void rxBusEventProcess(@NonNull Events<?> events) throws Exception {

    }

    private void initRecyclerView(RecyclerView recyclerView) {

        List<String> l = new ArrayList();
        for(String s : this.lvs) {
            l.add(s);
        }

        RecyclerViewAdapter recycleAdapter = new RecyclerViewAdapter<String>(l) {

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.recycle_view_item;
            }

            @Override
            public void convert(RecyclerViewAdapter.RecycleViewHolder holder, String data, int position) {
                final Drawable recycleViewNormal = ContextCompat.getDrawable(getContext(), R.drawable.recycle_view_normal);
                final Drawable recycleViewPressed = ContextCompat.getDrawable(getContext(), R.drawable.recycle_view_pressed);
                holder.setText(R.id.id_num, data);
                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    private float pointerX = 0;
                    private float pointerY = 0;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction() == MotionEvent.ACTION_UP){
                            v.setBackground(recycleViewNormal);
                        }
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            pointerX = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
                            pointerY = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
                            v.setBackground(recycleViewPressed);
                        }
                        if(event.getAction() == MotionEvent.ACTION_MOVE){
                            float pointerXTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
                            float pointerYTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
                            if(pointerXTmp != pointerX || pointerYTmp != pointerY) {
                                v.setBackground(recycleViewNormal);
                            }
                        }
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //View view = ((ViewGroup)v).getChildAt(1);
                        //Toast.makeText(getActivity(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
                        //v.setBackgroundColor(Color.parseColor("#000000"));
                    }
                });
                Button b = holder.getView(R.id.recycler_view_button);
                final TextView tv = holder.getView(R.id.id_num);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), tv.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置分隔线
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        recyclerView.setAdapter(recycleAdapter);
        //recyclerView.setOnItemClickListener(this);

        recyclerView.addItemDecoration(new SimplePaddingDecoration(getActivity()));
    }
}
