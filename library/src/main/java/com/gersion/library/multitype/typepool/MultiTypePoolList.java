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

package com.gersion.library.multitype.typepool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gersion.library.multitype.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * An List implementation of TypePool.
 *
 * @author drakeet
 */
public class MultiTypePoolList implements TypePool {

    private List<Type> mTypes = new ArrayList<>();

    public void register(Class clazz, int layoutId) {
        if (checkIsRegistered(clazz)){
            throw new IllegalStateException(clazz + " 已经注册过，请不要重复注册");
        }
        Type type = new Type();
        type.layoutId = layoutId;
        type.itemType = mTypes.size();
        type.clazz = clazz;
        mTypes.add(type);
    }

    @Override
    public void register(int layoutId) {

    }

    private boolean checkIsRegistered(Class clazz) {
        for (Type type : mTypes) {
            if (type.clazz == clazz){
                return true;
            }
        }
        return false;
    }

    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int itemType) {
        Type type = mTypes.get(itemType);
        return type.getViewHolder(parent);
    }

    public int getItemType(Class clazz) {
        for (int i = 0; i < mTypes.size(); i++) {
            Type type = mTypes.get(i);
            if (type.clazz==clazz){
                return i;
            }
        }
        throw new IllegalStateException(clazz+" 没有注册,请检查..");
    }

    @Override
    public int getItemType(int layoutId) {
        return 0;
    }


    public static class Type {
        int itemType;
        int layoutId;
        Class clazz;
        private LayoutInflater inflater;

        public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(layoutId, parent, false);
            return new BaseViewHolder(view);
        }
    }
}
