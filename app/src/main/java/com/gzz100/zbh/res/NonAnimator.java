package com.gzz100.zbh.res;

import android.os.Parcel;
import android.os.Parcelable;

import com.gzz100.zbh.R;

import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Lam on 2018/4/23.
 */

public class NonAnimator extends FragmentAnimator implements Parcelable {

    public NonAnimator() {
        enter = R.anim.no_anim;
        exit = R.anim.no_anim;
        popEnter = R.anim.no_anim;
        popExit = R.anim.no_anim;
    }

    protected NonAnimator(Parcel in) {
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

    public static final Creator<NonAnimator> CREATOR = new Creator<NonAnimator>() {
        @Override
        public NonAnimator createFromParcel(Parcel in) {
            return new NonAnimator(in);
        }

        @Override
        public NonAnimator[] newArray(int size) {
            return new NonAnimator[size];
        }
    };
}
