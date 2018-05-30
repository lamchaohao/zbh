package com.gzz100.zbh.home.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.AttachRequest;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/5/10.
 */

public class FeedbackFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.et_feedback)
    EditText mEtFeedback;
    @BindView(R.id.tv_textCount)
    TextView mTvCount;
    Unbinder unbinder;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_feedback, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
        initView();
    }

    private void initView() {

        mEtFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvCount.setText(s.toString().length()+"/200");
            }
        });

    }

    private void initTopbar() {
        mTopbar.setTitle("意见反馈");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        Button textButton = mTopbar.addRightTextButton("反馈",R.id.textButtonId);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadContent();
            }
        });
    }

    private void uploadContent() {
        if (TextUtils.isEmpty(mEtFeedback.getText())){

            Toasty.normal(getContext().getApplicationContext(),"还没写反馈哦").show();
        }else {
            PackageInfo packageInfo=null;
            try {
                packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            AttachRequest request=new AttachRequest();
            request.submitFeedback(new Observer<HttpResult>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(HttpResult result) {
                    final QMUITipDialog dialog = new QMUITipDialog.Builder(getContext())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                            .setTipWord("感谢您的反馈,我们会努力完善")
                            .create();
                    dialog.show();
                    mEtFeedback.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            pop();
                        }
                    },1500);
                }

                @Override
                public void onError(Throwable e) {
                    Toasty.error(getContext().getApplicationContext(),e.getMessage()).show();
                }

                @Override
                public void onComplete() {

                }
            },packageInfo.versionCode+"",mEtFeedback.getText().toString());

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
        unbinder.unbind();
    }
}
