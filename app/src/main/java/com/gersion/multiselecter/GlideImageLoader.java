package com.gersion.multiselecter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gersion.library.inter.IImageLoader;

/**
 * Created by gersy on 2017/8/6.
 */

public class GlideImageLoader implements IImageLoader {
    @Override
    public void showImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.ic_access_time_purple_200_24dp)
                .into(imageView);
    }
}
