package com.gersion.multiselecter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gersion.library.multitype.ItemViewBinder;
import com.gersion.library.viewholder.BaseViewHolder;

/**
 * Created by gersy on 2017/7/25.
 */

public class UserViewBinder extends ItemViewBinder<UserBean,UserViewBinder.UserViewHolder> {
    @NonNull
    @Override
    protected UserViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, @NonNull UserBean item) {
        holder.setOnItemClickListener(mListener);
        holder.setData(item);
    }

    static class UserViewHolder extends BaseViewHolder<UserBean>{

        private final CheckBox mCbSelectmember;
        private final TextView mTvName;
        private View mView;

        public UserViewHolder(View view) {
            super(view);
            mCbSelectmember = view.findViewById(R.id.cb_selectmember);
            mTvName = view.findViewById(R.id.tv_name);
            mView = view;
        }

        @Override
        protected void data2View(UserBean userBean) {
            mTvName.setText(userBean.userName);
        }

        @Override
        protected CheckBox getCheckBox() {
            return mCbSelectmember;
        }

        @Override
        protected int getNormalBackgroundResource() {
            return R.drawable.btn_check;
        }

        @Override
        protected int getNoChoiceBackgroundResource() {
            return R.drawable.geycheck;
        }

    }
}
