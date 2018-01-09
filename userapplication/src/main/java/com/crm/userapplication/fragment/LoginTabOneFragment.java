package com.crm.userapplication.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.userapplication.R;
import com.crm.userapplication.adapter.RecyclerViewAdapter;
import com.crm.userapplication.adapter.SimplePaddingDecoration;
import com.crm.userapplication.contract.BaseContract;
import com.crm.userapplication.databinding.FragmentLoginBinding;
import com.crm.userapplication.listener.DataLoadingSubject;
import com.crm.userapplication.listener.InfiniteScrollListener;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
public class LoginTabOneFragment extends BaseFragment {

    private FragmentLoginBinding mBinding;

    private GestureDetector mGestureDetector;

    private RecyclerViewAdapter mRecycleAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        initSwipeRefresh(mBinding.layoutSwipeRefresh);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == ContextMenu.FIRST + 1) {

            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            RxBus.getInstance().send(this, Events.EVENT_TAP, item.getTitle().toString() + menuInfo.position);
        }
        return super.onContextItemSelected(item);
    }

    private void initRecyclerView(RecyclerView recyclerView) {

        List<String> l = new ArrayList();
        for(String s : this.lvs) {
            l.add(s);
        }

        // 可以不写
        // registerForContextMenu(recyclerView);
        //设置菜单弹出事件
//        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0, ContextMenu.FIRST+1, 0, "删除");
//            }
//        });

        mRecycleAdapter = new RecyclerViewAdapter<String>(l, true, true) {

            @Override
            public int getLayoutId(int viewType) {
                if(viewType == RecyclerViewAdapter.TYPE_BODY) {
                    return R.layout.recycle_view_item;
                } else if(viewType == RecyclerViewAdapter.TYPE_FOOTER) {
                    return R.layout.recycle_view_item_footer;
                } else if(viewType == RecyclerViewAdapter.TYPE_HEADER){
                    return R.layout.recycle_view_item_header;
                } else {
                    return -1;
                }
            }

            @Override
            public int getPopupId(int viewType) {
                return R.layout.layout_popwindow_recyclerview;
            }

            @Override
            public void convert(RecyclerView.ViewHolder holderOld, String data, int position) {

                final RecyclerViewAdapter.RecycleViewHolder holder = (RecyclerViewAdapter.RecycleViewHolder)holderOld;

                final Drawable recycleViewNormal = util.getDrawable(R.drawable.recycle_view_normal);
                final Drawable recycleViewPressed = util.getDrawable(R.drawable.recycle_view_pressed);

                mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        int x = (int)e.getRawX();
                        int y = (int)e.getRawY();
                        Toast.makeText(getContext(), "x:" + x+ " y:" + y, Toast.LENGTH_SHORT).show();

                        holder.getPopUpView().showAsDropDown(holder.itemView, x-300, y-300);
                    }


                });

                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    private float pointerX = 0;
                    private float pointerY = 0;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            v.setBackground(recycleViewNormal);
                            Log.i("TTTTTTTTTTTTTT", "ACTION_UP");
                        } else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                            v.setBackground(recycleViewNormal);
                            Log.i("TTTTTTTTTTTTTT", "ACTION_CANCEL");
                        } else if(event.getAction() == MotionEvent.ACTION_DOWN){

                            pointerX = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
                            pointerY = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
                            //v.setBackground(recycleViewPressed);
                            Log.i("TTTTTTTTTTTTTT", "ACTION_DOWN");

                        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
                            float pointerXTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
                            float pointerYTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
                            if (pointerXTmp != pointerX || pointerYTmp != pointerY) {
                                v.setBackground(recycleViewNormal);
                                Log.i("TTTTTTTTTTTTTT", "ACTION_MOVE");
                            } else {
                                v.setBackground(recycleViewPressed);
                            }
                        }

                        mGestureDetector.onTouchEvent(event);
                        return true;
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

                if(holder.getViewType() == RecyclerViewAdapter.TYPE_BODY) {
                    holder.setText(R.id.id_num, data);
                    Button b = holder.getView(R.id.recycler_view_button);
                    final TextView tv = holder.getView(R.id.id_num);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), tv.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(holder.getViewType() == RecyclerViewAdapter.TYPE_FOOTER) {
                    final TextView tv = holder.getView(R.id.textViewFooter);
                    tv.setText("无法加载更多");

                    // 刷新太快 所以使用Hanlder延迟两秒
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.getP().getmDatas().add("234");
//                            notifyDataSetChanged();
//                        }
//                    }, 2000);
                } else if(holder.getViewType() == RecyclerViewAdapter.TYPE_HEADER) {
                    final TextView tv = holder.getView(R.id.textViewHeader);
                    tv.setText("Header");
                    //final ProgressBar progressBar = holder.getView(R.id.headerProgressBar);

                }

            }
        };

        DataLoadingSubject mDataManager = new DataLoadingSubject() {

            @Override
            public boolean isDataLoading() {
                return false;
            }

            @Override
            public void registerCallback(DataLoadingCallbacks callBack) {

            }

            @Override
            public void unregistereCallBack(DataLoadingCallbacks callBack) {

            }
        };

        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mRecycleAdapter.isHaveHeader() && position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        layoutManager.scrollToPositionWithOffset(1, 0);
        //layoutManager.setStackFromEnd(true);
        //设置分隔线
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        recyclerView.setAdapter(mRecycleAdapter);
        //recyclerView.setOnItemClickListener(this);

        recyclerView.addItemDecoration(new SimplePaddingDecoration(getActivity()));

        recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, mDataManager) {
            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecycleAdapter.getmDatas().add("aaaaaaa");
                        mRecycleAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });
    }

    private void initSwipeRefresh(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getInstance().send(LoginTabOneFragment.this, Events.EVENT_UPDATE_WIDGET, 1000);
            }
        });
    }


    @Override
    protected void rxBusEventProcess(Events events) {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            mRecycleAdapter.notifyDataSetChanged();
            this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void rxBusDoNext(Events events) throws Exception {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            Thread.sleep((int)events.getContent().get("content"));
        }
    }

    @Override
    protected BaseContract.IBasePresenter getP() {
        return null;
    }
}
