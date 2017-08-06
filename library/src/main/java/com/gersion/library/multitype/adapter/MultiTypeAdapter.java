/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gersion.library.multitype.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.gersion.library.inter.Filter;
import com.gersion.library.inter.OnItemClickListener;
import com.gersion.library.multitype.inter.IMultiLayout;
import com.gersion.library.multitype.typepool.MultiBeanListPool;
import com.gersion.library.multitype.typepool.MultiLayoutListPool;
import com.gersion.library.multitype.typepool.TypePool;
import com.gersion.library.multitype.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author drakeet
 */
public abstract class MultiTypeAdapter<T extends Filter, K> extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "MultiTypeAdapter";
    protected OnItemClickListener mListener;
    protected List<T> items;
    private TypePool typePool;
    protected Context mContext;

    //用来判断是使用了哪种注册类型< -1-还没使用过，0-多布局 ，1- 多Bean>
    private int registerType = -1;

    private static final int MULTI_LAYOUT = 0;
    private static final int MULTI_BEAN = 1;

    protected List<T> mSelectionList = new ArrayList<>();
    private int mSelectType;

    public MultiTypeAdapter() {
        this(new ArrayList<T>());
    }


    public MultiTypeAdapter(@NonNull List<T> items) {
        this(items, null);
    }


    public MultiTypeAdapter(@NonNull List<T> items, @NonNull TypePool pool) {
        this.items = getFilterItems(items);
        this.typePool = pool;
    }

    //注册多个bean 对应多个或单个布局
    public void registerMultiBean(Class clazz, int layoutId) {
        if (registerType == MULTI_LAYOUT) {
            throw new IllegalStateException("您已经使用过registerMultiLayout()方法，不能再使用registerMultiBean()注册，二者互斥");
        }
        registerType = MULTI_BEAN;
        if (typePool == null) {
            typePool = new MultiBeanListPool();
        }
        typePool.register(clazz, layoutId);
    }

    //注册一个bean对应多个布局
    public void registerMultiLayout(int layoutId) {
        if (registerType == MULTI_BEAN) {
            throw new IllegalStateException("您已经使用过registerMultiBean()方法，不能再使用registerMultiLayout()注册，二者互斥");
        }
        registerType = MULTI_LAYOUT;
        if (typePool == null) {
            typePool = new MultiLayoutListPool();
        }
        typePool.register(layoutId);
    }

    public void setItems(@NonNull List<T> items) {
        this.items = getFilterItems(items);
        notifyDataSetChanged();
    }

    @NonNull
    public List<?> getItems() {
        return items;
    }


    public void setTypePool(@NonNull TypePool typePool) {
        this.typePool = typePool;
    }


    @NonNull
    public TypePool getTypePool() {
        return typePool;
    }

    public void setSelectType(int selectType){
        mSelectType = selectType;
    }

    @Override
    public final int getItemViewType(int position) {
        int itemType = -1;
        if (registerType == MULTI_LAYOUT) {
            IMultiLayout item = (IMultiLayout) items.get(position);
            int layoutId = item.getLayoutId();
            itemType = typePool.getItemType(layoutId);
        } else {
            Object item = items.get(position);
            itemType = typePool.getItemType(item.getClass());
        }
        return itemType;
    }


    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int indexViewType) {
        if (mContext==null) {
            mContext = parent.getContext();
        }
        ViewHolder viewHolder = typePool.getViewHolder(parent, indexViewType);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.setAdapter(this);
        convert(baseViewHolder, items.get(position));
        baseViewHolder.setCheckBox(getCheckBox());
        baseViewHolder.setData(items.get(position));
        baseViewHolder.setOnItemClickListener(mListener);
        baseViewHolder.setSelectType(mSelectType);
    }

    protected List<T> getFilterItems(List<T> items) {
        mSelectionList.clear();
        if (items != null) {
            List<T> data = new ArrayList<>();
            for (T item : items) {
                int type = item.filter();
                if (type != Filter.NOT_SHOW) {
                    data.add(item);
                    if (type != Filter.NO_CHOICE&&type!=Filter.TITLE_NO_CHOICE) {
                        mSelectionList.add(item);
                    }
                }
            }
            return data;
        } else {
            return null;
        }
    }

    protected abstract void convert(BaseViewHolder helper, T item);

    protected abstract CheckBox getCheckBox();

    @Override
    public final int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
