package com.gersion.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;

import com.gersion.library.inter.Filter;
import com.gersion.library.multitype.adapter.MultiTypeAdapter;
import com.gersion.library.multitype.viewholder.BaseViewHolder;
import com.gersion.library.smartrecycleview.IRVAdapter;

import java.util.List;

/**
 * Created by gersy on 2017/7/25.
 */

public abstract class BaseMultiAdapter<T extends Filter> extends MultiTypeAdapter<T,BaseViewHolder<Filter>> implements IRVAdapter<Filter> {

    private int mTotalCount;
    private CheckBox mCheckBox;

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    public List<T> getList() {
        return items;
    }

    public List<T> getSelectionList() {
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

    public void updateItem(T bean){
        for (T friendDataBean : items) {
            if (friendDataBean==bean){
                friendDataBean.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    public T getItem(int position){
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
    public List getData() {
        return items;
    }



    @Override
    protected abstract void convert(BaseViewHolder helper, T item);

    @Override
    protected abstract CheckBox getCheckBox();
}
