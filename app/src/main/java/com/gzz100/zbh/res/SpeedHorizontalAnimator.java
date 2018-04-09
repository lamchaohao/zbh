package com.gzz100.zbh.res;

import android.os.Parcel;
import android.os.Parcelable;

import com.gzz100.zbh.R;

import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Lam on 2018/1/23.
 */

public class SpeedHorizontalAnimator extends FragmentAnimator implements Parcelable{

    public SpeedHorizontalAnimator() {
        enter = R.anim.h_fragment_enter;
        exit = R.anim.h_fragment_exit;
        popEnter = R.anim.h_fragment_pop_enter;
        popExit = R.anim.h_fragment_pop_exit;
    }

    protected SpeedHorizontalAnimator(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpeedHorizontalAnimator> CREATOR = new Creator<SpeedHorizontalAnimator>() {
        @Override
        public SpeedHorizontalAnimator createFromParcel(Parcel in) {
            return new SpeedHorizontalAnimator(in);
        }

        @Override
        public SpeedHorizontalAnimator[] newArray(int size) {
            return new SpeedHorizontalAnimator[size];
        }
    };

}
