package com.gersion.multiselecter;

import com.gersion.library.inter.Filter;

/**
 * Created by gersy on 2017/8/6.
 */

public class TitleBean implements Filter {
    public String title;

    @Override
    public int getImageResource() {
        return 0;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public int filter() {
        return 0;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean isSelected) {

    }

    @Override
    public boolean isMatch(String condition) {
        return false;
    }
}
