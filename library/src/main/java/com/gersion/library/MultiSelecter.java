package com.gersion.library;

import android.app.Activity;

import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.multitype.typepool.TypePool;

/**
 * Created by gersy on 2017/8/2.
 */

public class MultiSelecter {
    private TypePool mTypePool;
    private BaseMultiAdapter mMultiAdapter;

    public static class Builder{
        private Activity mActivity;
        private BaseMultiAdapter mMultiAdapter;
        private TypePool mTypePool;

        public Builder(Activity activity, BaseMultiAdapter multiAdapter,TypePool typePool){
            mActivity = activity;
            mMultiAdapter = multiAdapter;
            mTypePool = typePool;
        }
        public MultiSelecter build(){
            return new MultiSelecter();
        }
    }
}
