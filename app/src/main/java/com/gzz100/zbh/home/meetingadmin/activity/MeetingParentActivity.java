package com.gzz100.zbh.home.meetingadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.gzz100.zbh.R;
import com.gzz100.zbh.home.meetingadmin.fragment.MeetingParentFragment;
import com.gzz100.zbh.res.SpeedHorizontalAnimator;
import com.orhanobut.logger.Logger;

import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MeetingParentActivity extends SupportActivity {

    private String mMeetingName;
    private String mMeetingId;
    private long mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_parent);
        initView();
    }

    private void initView() {
        mMeetingId = getIntent().getStringExtra("meetingId");
        mMeetingName = getIntent().getStringExtra("meetingName");
        mGroupId = getIntent().getLongExtra("groupId",0);
        Logger.i("mMeetingId="+mMeetingId+",mMeetingName"+mMeetingName);
        if (findFragment(MeetingParentFragment.class) == null) {
            loadRootFragment(R.id.container, MeetingParentFragment.getNewInstance(mMeetingId,mMeetingName,mGroupId));
        }

    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return  new SpeedHorizontalAnimator();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Logger.i("call back onActivityResult requestCode"+requestCode+",resultCode="+resultCode);
        FragmentManager fragmentManager=getSupportFragmentManager();
        for(int indext=0;indext<fragmentManager.getFragments().size();indext++) {
            Fragment fragment=fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if(fragment==null)
                Logger.i("Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment,requestCode,resultCode,data);
        }
    }

    /**
     * 递归调用 传值onActivityResult
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if(childFragment!=null)
            for(Fragment f:childFragment) {
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
    }
}
