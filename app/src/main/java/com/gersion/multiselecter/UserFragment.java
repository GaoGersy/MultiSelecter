package com.gersion.multiselecter;

import com.gersion.library.fragment.SelectionFragment;
import com.gersion.library.multitype.ItemViewBinder;

/**
 * Created by gersy on 2017/7/25.
 */

public class UserFragment extends SelectionFragment {
    UserViewBinder userViewBinder = new UserViewBinder();
    @Override
    public ItemViewBinder getBinder() {
        return userViewBinder;
    }

    @Override
    public Class getBeanClass() {
        return UserBean.class;
    }
}
