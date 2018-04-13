package com.gzz100.zbh.account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.gzz100.zbh.data.entity.ApplyEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.CompanyRequest;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GuideFragment extends BaseFragment {


    @BindView(R.id.btn_guide_joinCompany)
    Button mBtnGuideJoinCompany;
    @BindView(R.id.tv_guide_applyingCompany)
    TextView tvApplyingCompany;
    @BindView(R.id.tv_guide_applyStatus)
    TextView tvApplyStatus;
    Unbinder unbinder;
    boolean isCancleApply;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }


    @OnClick(R.id.btn_guide_joinCompany)
    public void onClick(View view){
        if (isCancleApply) {
            new QMUIDialog.MessageDialogBuilder(getContext())
                    .setTitle("撤回申请")
                    .setMessage("确定撤回对企业的申请吗？")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            cancelApply();
                            dialog.dismiss();
                        }
                    })
                    .show();

        }else {
            startParentFragment(new SearchCompFragment());
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        User user = User.getUserFromCache();
        if (user.getApply()!=null) {
            User.ApplyBean apply = user.getApply();
            tvApplyStatus.setVisibility(View.VISIBLE);
            tvApplyingCompany.setText(apply.getCompanyNameX());
            tvApplyStatus.setText("你已申请加入公司, 等待审核中");
            mBtnGuideJoinCompany.setText("取消申请");
            isCancleApply= true;
        }else {
            tvApplyStatus.setVisibility(View.GONE);
            tvApplyingCompany.setText("你还未加入企业");
            mBtnGuideJoinCompany.setText("马上加入");
            isCancleApply= false;
        }
    }

    /**
     * 申请加入公司后的信息更新
     * @param applyEntity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJoined(ApplyEntity applyEntity){
        initView();
    }

    private void cancelApply(){
        Observer<HttpResult> observer=new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                if (result.getCode()==1) {
                    User user = User.getUserFromCache();
                    user.setApply(null);
                    User.save(user);
                    Toasty.normal(getContext(),"已取消申请").show();
                    //MineFragment也会收到消息更新
                    EventBus.getDefault().post(new ApplyEntity());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        CompanyRequest request=new CompanyRequest();
        request.cancelApplyCompany(observer);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
