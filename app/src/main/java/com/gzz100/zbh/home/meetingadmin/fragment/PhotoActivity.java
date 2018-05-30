package com.gzz100.zbh.home.meetingadmin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.gzz100.zbh.R;
import com.gzz100.zbh.utils.GlideApp;

public class PhotoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo);
        initView();
    }

    private void initView() {

        final PhotoView photoView = findViewById(R.id.iv_photo);
        String picUrl = getIntent().getStringExtra("picUrl");
        GlideApp.with(this)
                .load(picUrl)
                .encodeQuality(100)
                .into(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//返回咯
            }
        });

    }
}
