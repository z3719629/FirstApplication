package com.crm.userapplication.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.userapplication.R;
import com.crm.userapplication.activity.base.BaseActivity;
import com.crm.userapplication.data.DataLoadingSubject;
import com.crm.userapplication.listener.RecyclerViewItemOnTouchListener;
import com.crm.userapplication.util.ZUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */
public class PageingViewAdapter<T, V extends BaseActivity> extends RecyclerViewAdapter<T> implements DataLoadingSubject.DataLoadingCallbacks {

    private V context;

    private boolean showLoadingMore;

    public PageingViewAdapter(V context, List datas, boolean isHaveHeader, boolean isHaveFooter) {
        super(datas, isHaveHeader, isHaveFooter);
        this.context = context;
    }

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
    public void convert(RecyclerView.ViewHolder holderOld, T data, int position) {

        final RecyclerViewAdapter.RecycleViewHolder holder = (RecyclerViewAdapter.RecycleViewHolder)holderOld;

        final Drawable recycleViewNormal = ZUtil.getInstance().getDrawable(R.drawable.recycle_view_normal);
        final Drawable recycleViewPressed = ZUtil.getInstance().getDrawable(R.drawable.recycle_view_pressed);

        holder.itemView.setOnTouchListener(new RecyclerViewItemOnTouchListener(context, recycleViewNormal, recycleViewPressed) {
            @Override
            protected void doOnLongPress(MotionEvent e, int x, int y) {
                // 弹框左上角显示在触摸点，偏移x 50 y 50
                holder.getPopUpView().showAsDropDown(holder.itemView, x + 50, y + 50 - holder.itemView.getHeight());
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
            holder.setText(R.id.id_num, (String)data);
            Button b = holder.getView(R.id.recycler_view_button);
            final TextView tv = holder.getView(R.id.id_num);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, tv.getText(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void dataStartedLoading() {
        if (showLoadingMore) return;
        showLoadingMore = true;

    }

    @Override
    public void dataFinishedLoading() {
        if (!showLoadingMore) return;
        int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemInserted(loadingPos);
    }

    public int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }
}