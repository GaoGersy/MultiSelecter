package com.gersion.multiselecter;

import com.gersion.library.inter.Filter;
import com.gersion.library.utils.MatchUtils;

/**
 * Created by gersy on 2017/7/25.
 */

public class UserBean implements Filter {
    public String userName;
    public int icon;
    public int age;
    public boolean isSelected;
    public String iconUrl;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int getImageResource() {
        return icon;
    }

    @Override
    public String getImageUrl() {
        return iconUrl;
    }

    @Override
    public int filter() {
        if (age<3){
            return Filter.NO_CHOICE;
        }else if (age>=3&&age<100){
            return Filter.NORMAL;
        }
        return 0;
    }

    @Override
    public boolean isMatch(String condition) {
        if (MatchUtils.isMatch(userName,condition)){
            return true;
        }
        return false;
    }
}
