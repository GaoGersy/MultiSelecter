package com.gersion.library.adapter;

import android.support.v7.widget.RecyclerView;

import com.gersion.library.listener.Filter;
import com.gersion.library.listener.OnItemClickListener;
import com.gersion.library.multitype.MultiTypeAdapter;
import com.gersion.library.smartrecycleview.IRVAdapter;
import com.gersion.library.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by gersy on 2017/7/25.
 */

public class ShowMultiAdapter extends MultiTypeAdapter<Filter,BaseViewHolder<Filter>> implements IRVAdapter<Filter> {

    private int mTotalCount;
    private OnItemClickListener mListener;

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    public List<Filter> getList() {
        return items;
    }

    public List<Filter> getSelectionList() {
        return mSelectionList;
    }

    public void setTotalCount(int count) {
        mTotalCount = count;
    }

    public void changeAllDataStatus(boolean selected) {
        for (Filter friendDataBean : items) {
            if (friendDataBean.filter()!=Filter.NO_CHOICE) {
                friendDataBean.setSelected(selected);
            }
        }
        notifyDataSetChanged();
    }

    public void updateItem(Filter bean){
        for (Filter friendDataBean : items) {
            if (friendDataBean==bean){
                friendDataBean.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    public Filter getItem(int position){
        return items.get(position);
    }

    @Override
    public void setNewData(List data) {
        items = getFilterItems(data);
        notifyDataSetChanged();
    }

    @Override
    public void addData(List data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void removeAll(List data) {
        items.removeAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Filter data) {
        items.remove(data);
        notifyDataSetChanged();
    }

    @Override
    public List<Filter> getData() {
        return items;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
