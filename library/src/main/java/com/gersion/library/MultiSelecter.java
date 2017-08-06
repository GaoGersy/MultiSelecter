package com.gersion.library;

import android.content.Context;
import android.view.ViewGroup;

import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.inter.IImageLoader;
import com.gersion.library.multitype.typepool.MultiBeanListPool;
import com.gersion.library.multitype.typepool.MultiLayoutListPool;
import com.gersion.library.multitype.typepool.TypePool;
import com.gersion.library.view.MultiSelectView;

/**
 * Created by gersy on 2017/8/2.
 */

public class MultiSelecter {
    //单选
    public static final int SINGLE_SELECT = 100;
    //多选
    public static final int MULTI_SELECT = 101;

    private TypePool mTypePool;
    private Context mContext;
    private BaseMultiAdapter mMultiAdapter;
    public static IImageLoader mImageLoader;
    private int mSelectType;
    private MultiSelectView mMultiSelectView;
    private ViewGroup mViewContainer;

    private MultiSelecter(Builder builder) {
        if (builder.mImageLoader == null) {
            throw new NullPointerException("ImageLoader 不能为空");
        }
        if (builder.mMultiAdapter == null) {
            throw new NullPointerException("BaseMultiAdapter 不能为空");
        }
        if (builder.mTypePool == null) {
            throw new NullPointerException("TypePool 不能为空");
        }
        if (builder.mViewContainer ==null){
            throw new NullPointerException("ViewContainer 不能为空");
        }
        mContext = builder.mContext;
        mTypePool = builder.mTypePool;
        mMultiAdapter = builder.mMultiAdapter;
        mSelectType = builder.mSelectType;
        mViewContainer = builder.mViewContainer;
        mImageLoader = builder.mImageLoader;
    }

    private MultiSelectView build() {
        mMultiSelectView = new MultiSelectView(mContext);
        mMultiSelectView.setAdapter(this.mMultiAdapter);
        mMultiSelectView.setTypePool(mTypePool);
        mMultiSelectView.setSelectType(mSelectType);
        mMultiSelectView.init();
        return into(mViewContainer);
    }

    private MultiSelectView into(ViewGroup container){
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(mMultiSelectView,params);
        return mMultiSelectView;
    }

    public static class Builder {
        private Context mContext;
        private ViewGroup mViewContainer;
        private BaseMultiAdapter mMultiAdapter;
        private TypePool mTypePool;
        private IImageLoader mImageLoader;
        private int mSelectType = MULTI_SELECT;

        public Builder(Context context,ViewGroup viewContainer) {
            mContext = context;
            mViewContainer = viewContainer;
        }

        public Builder setMultiAdapter(BaseMultiAdapter multiAdapter) {
            mMultiAdapter = multiAdapter;
            return this;
        }

        public Builder setTypePool(TypePool typePool) {
            mTypePool = typePool;
            return this;
        }

        public Builder setImageLoader(IImageLoader imageLoader) {
            mImageLoader = imageLoader;
            return this;
        }

        public Builder setSelectType(int selectType) {
            mSelectType = selectType;
            return this;
        }

        public Builder register(Class clazz, int layoutId) {
            if (mTypePool == null) {
                mTypePool = new MultiBeanListPool();
            }
            if (mTypePool instanceof MultiLayoutListPool) {
                throw new IllegalStateException("已经注册过 MultiLayoutListPool 类型，不能再注册成MultiBeanListPool");
            }
            mTypePool.register(clazz, layoutId);
            return this;
        }

        public Builder register(int layoutId) {
            if (mTypePool == null) {
                mTypePool = new MultiLayoutListPool();
            }
            if (mTypePool instanceof MultiBeanListPool) {
                throw new IllegalStateException("已经注册过 MultiBeanListPool 类型，不能再注册成 MultiLayoutListPool");
            }
            mTypePool.register(layoutId);
            return this;
        }

        public MultiSelectView build() {
            return new MultiSelecter(this).build();
        }
    }
}
