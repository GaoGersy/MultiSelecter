package com.gersion.library.inter;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by gersy on 2017/5/28.
 */

public interface IImageLoader {
    void showImage(Context context, String url, ImageView imageView);
}
