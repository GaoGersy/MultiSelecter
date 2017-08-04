package com.gersion.multiselecter;

import android.view.View;
import android.widget.CheckBox;

import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.multitype.viewholder.BaseViewHolder;

/**
 * Created by gersy on 2017/8/2.
 */

public class UserAdapter extends BaseMultiAdapter<UserBean> {

    private CheckBox mCheckBox;

    @Override
    protected void convert(final BaseViewHolder helper, final UserBean item) {
        mCheckBox = (CheckBox) helper.getView(R.id.cb_selectmember);
        helper.setText(R.id.tv_name,item.userName);
        helper.setImageResource(R.id.iv_icon,item.icon);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = mCheckBox.isChecked();
                mCheckBox.setChecked(!checked);
                item.setSelected(!checked);
                if (mListener != null) {
                    mListener.onItemClick(helper.itemView, item, !checked);
                }
            }
        });
    }

    @Override
    protected CheckBox getCheckBox() {
        return mCheckBox;
    }
}
