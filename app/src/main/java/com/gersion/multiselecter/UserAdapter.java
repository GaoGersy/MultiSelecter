package com.gersion.multiselecter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.inter.Filter;
import com.gersion.library.multitype.viewholder.BaseViewHolder;

/**
 * Created by gersy on 2017/8/2.
 */

public class UserAdapter extends BaseMultiAdapter<Filter> {

    private CheckBox mCheckBox;

    @Override
    protected void convert(final BaseViewHolder helper, final Filter item) {
        if (item instanceof UserBean) {
            final UserBean bean = (UserBean) item;
            mCheckBox = (CheckBox) helper.getView(R.id.cb_selectmember);
            helper.setText(R.id.tv_name, bean.userName);
//            helper.setImageResource(R.id.iv_icon, bean.icon);
            final ImageView icon = (ImageView) helper.getView(R.id.iv_icon);
//            MultiSelecter.mImageLoader.showImage(mContext,item.getImageUrl(),icon);
            ImageUtil.loadRetangleImage(mContext,item.getImageUrl(),icon);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = mCheckBox.isChecked();
                    mCheckBox.setChecked(!checked);
                    bean.setSelected(!checked);
                    if (mListener != null) {
                        mListener.onItemClick(helper.itemView, bean, !checked);
                    }
                }
            });
        }else if (item instanceof TitleBean){
            TitleBean bean = (TitleBean) item;
            helper.setText(R.id.tv_title,bean.title);
        }
    }

    @Override
    protected CheckBox getCheckBox() {
        return mCheckBox;
    }
}
