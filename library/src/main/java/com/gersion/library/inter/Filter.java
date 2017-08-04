package com.gersion.library.inter;

/**
 * Created by gersy on 2017/7/24.
 */

public interface Filter {
    //默认不选中，可以选中
    int NORMAL = -1;
    //默认选中,不可以取消选中
    int SELECTED = 1;
    //不能被选中
    int NO_CHOICE = 2;
    //不显示在列表
    int NOT_SHOW = 3;

    int getImageResource();

    int filter();

    boolean isSelected();

    void setSelected(boolean isSelected);

    boolean isMatch(String condition);
}
