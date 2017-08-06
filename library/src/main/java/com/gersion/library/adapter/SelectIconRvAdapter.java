package com.gersion.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gersion.library.MultiSelecter;
import com.gersion.library.R;
import com.gersion.library.inter.Filter;
import com.gersion.library.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SelectIconRvAdapter extends RecyclerView.Adapter<SelectIconRvAdapter.ViewHolder> {

    private List<Filter> itemFilters = new ArrayList<>();
    private OnItemClickListener mListener;
    private static Context mContext;


    public void setItemFilters(@NonNull List<Filter> itemFilters) {
        this.itemFilters = itemFilters;
    }

    public int add(Filter bean) {
        itemFilters.add(bean);
        int i = itemFilters.size() - 1;
        notifyItemChanged(i);
        return i;
    }

    public void remove(Filter bean) {
        itemFilters.remove(bean);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        itemFilters.remove(position);
        notifyItemChanged(position);
    }

    public void addAllData(List<Filter> data) {
        itemFilters.clear();
        itemFilters.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Filter> data) {
        itemFilters.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        itemFilters.clear();
        notifyDataSetChanged();
    }

    public void setData(List<Filter> data) {
        itemFilters = data;
        notifyDataSetChanged();
    }


    public void addHiddenItem(Filter bean) {
        itemFilters.add(bean);
        int i = itemFilters.size() - 1;
        notifyItemChanged(i);
    }

    public Filter getItem(int position) {
        return itemFilters.get(position);
    }

    public List<Filter> getList() {
        return itemFilters;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_icon, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Filter itemFilter = itemFilters.get(position);
        holder.setOnItemClickListener(mListener);
        holder.setData(itemFilter, position);
    }


    @Override
    public int getItemCount() {
        return itemFilters.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        private OnItemClickListener mListener;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.icon);
        }

        public void setData(final Filter data, int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, data, false);
                    }
                }
            });
            MultiSelecter.mImageLoader.showImage(mContext,data.getImageUrl(),cover);

        }

        public View getItemView() {
            return itemView;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}