package com.gersion.multiselecter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtil {

    public static void loadRetangleImage(Context context, String url, ImageView imag) {
        Glide
                .with(context)
                .load(url)
                .placeholder(R.mipmap.ic_backup_purple_200_24dp) //设置占位图
                .error(R.mipmap.ic_archive_purple_200_24dp) //设置错误图片
                .crossFade(200) //设置淡入淡出效果，默认300ms，可以传参
                //.dontAnimate() //不显示动画效果
                .into(imag);
    }

}
