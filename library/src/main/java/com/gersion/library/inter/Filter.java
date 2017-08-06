package com.gersion.library.inter;

/**
 * Created by gersy on 2017/7/24.
 */

public interface Filter {

    //专为标题而生，因为系统默认给的是0，这样title Bean类里面都不用做过多的修改了
    int TITLE_NO_CHOICE = 0;

    //默认不选中，可以选中
    int NORMAL = 1;

    //默认选中,不可以取消选中
    int SELECTED_NOCANCEL = 2;

    //默认不选中,不能被选中
    int NO_CHOICE = 3;

    //不显示在列表
    int NOT_SHOW = 4;

    int getImageResource();

    String getImageUrl();

    int filter();

    boolean isSelected();

    void setSelected(boolean isSelected);

    boolean isMatch(String condition);
}
