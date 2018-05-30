package com.gzz100.zbh.utils;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by Lam on 2018/2/27.
 */
@GlideModule
public final  class ZbhGlideModule extends AppGlideModule {
//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        super.applyOptions(context, builder);
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        //设置磁盘缓存目录（和创建的缓存目录相同）
//        File storageDirectory = Environment.getExternalStorageDirectory();
//        String downloadDirectoryPath=storageDirectory+"/GlideCache";
//        //设置缓存的大小为100M
//        int cacheSize = 100*1000*1000;
//        builder.setDiskCache( new DiskLruCacheFactory(downloadDirectoryPath, cacheSize) );
//
//    }
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        super.registerComponents(context, glide, registry);
//    }
}
