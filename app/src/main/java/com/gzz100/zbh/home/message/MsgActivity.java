package com.gzz100.zbh.home.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.res.NonAnimator;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MsgActivity extends SupportActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.tv_msg_title)
    TextView mTvMsgTitle;
    @BindView(R.id.tv_msg_body)
    TextView mTvMsgBody;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        unbinder = ButterKnife.bind(this);
        initTopbar();
        initView();
    }

    /**
     * 设置动画，也可以使用setFragmentAnimator()设置
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
        return new NonAnimator();
//        return new SpeedHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }


    private void initTopbar() {
        mTopbar.setTitle("消息详情");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        mTvMsgTitle.setText(title);
        mTvMsgBody.setText(description);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
