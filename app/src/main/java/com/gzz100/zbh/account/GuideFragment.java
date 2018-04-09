package com.gzz100.zbh.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GuideFragment extends BaseFragment {


    @BindView(R.id.btn_guide_joinCompany)
    Button mBtnGuideJoinCompany;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @OnClick(R.id.btn_guide_joinCompany)
    public void onClick(View view){
        startParentFragment(new SearchCompFragment());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
