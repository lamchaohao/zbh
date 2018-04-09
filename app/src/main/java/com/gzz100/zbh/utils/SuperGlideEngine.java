package com.gzz100.zbh.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by Lam on 2018/2/27.
 */

public class SuperGlideEngine implements ImageEngine {
    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .load(uri)
                .placeholder(placeholder)
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .load(uri)
                .placeholder(placeholder)
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
