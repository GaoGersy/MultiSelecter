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

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gersion.library.multitype.viewholder.BaseViewHolder;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An List implementation of TypePool.
 *
 * @author drakeet
 */
public class MultiTypePoolMap implements TypePool {

    private ArrayMap<Class, Type> mTypeArrayMap = new ArrayMap<>();
    private static int count = 0;

    public void register(Class clazz, int layoutId) {
        Type type = new Type();
        type.layoutId = layoutId;
        type.itemType = count++;
        mTypeArrayMap.put(clazz, type);
    }

    @Override
    public void register(int layoutId) {

    }

    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int itemType) {

        Set<Map.Entry<Class, Type>> entries = mTypeArrayMap.entrySet();
        Iterator<Map.Entry<Class, Type>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, Type> entry = iterator.next();
            Type type = entry.getValue();
            if (type.itemType == itemType) {
                return type.getViewHolder(parent);
            }
        }
        return null;
    }

    public int getItemType(Class clazz) {
        Set<Map.Entry<Class, Type>> entries = mTypeArrayMap.entrySet();
        Iterator<Map.Entry<Class, Type>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, Type> entry = iterator.next();
            Class key = entry.getKey();
            if (key == clazz) {
                return entry.getValue().itemType;
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
        private LayoutInflater inflater;

        public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }
            View view = inflater.inflate(layoutId, parent, false);
            return new BaseViewHolder<>(view);
        }
    }
}
