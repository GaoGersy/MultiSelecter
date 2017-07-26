package com.gersion.multiselecter;

import com.gersion.library.listener.Filter;

/**
 * Created by gersy on 2017/7/25.
 */

public class UserBean implements Filter {
    public String userName;
    public String icon;
    public String isSelectMode;
    public int age;
    public boolean isSelected;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int filter() {
        if (age<3){
            return Filter.NO_CHOICE;
        }else if (age>=3&&age<12){
            return Filter.NORMAL;
        }else if (age >=12 && age<30){
//            setSelected(true);
            return Filter.NORMAL;
        }else if (age>=30){
            return Filter.NOT_SHOW;
        }
        return 0;
    }
}
