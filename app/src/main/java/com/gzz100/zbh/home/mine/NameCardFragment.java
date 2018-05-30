package com.gzz100.zbh.home.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gzz100.zbh.R;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.base.BaseBackFragment;
import com.gzz100.zbh.data.entity.ApplyEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.request.CompanyRequest;
import com.gzz100.zbh.data.network.request.UserLoginRequest;
import com.gzz100.zbh.utils.TextHeadPicUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lam on 2018/4/9.
 */

public class NameCardFragment extends BaseBackFragment {
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.iv_userPic)
    ImageView mIvUserPic;
    @BindView(R.id.tv_userName_mine)
    TextView mTvUserNameMine;
    @BindView(R.id.tv_companyName)
    TextView mTvCompanyName;
    @BindView(R.id.tv_name_position)
    TextView mTvNamePosition;
    @BindView(R.id.btn_quit_company)
    Button mBtnQuitCompany;
    Unbinder unbinder;
    @BindView(R.id.tv_name_depart)
    TextView mTvDepartName;
    private User mUser;
    private EditText mEditText;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_name_card, null);

        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopbar();
    }

    private void initTopbar() {
        mTopbar.setTitle("个人信息");
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {

        mUser = User.getUserFromCache();
        if (mUser.getCompanyName() != null) {
            mTvCompanyName.setText(mUser.getCompanyName());
            mTvNamePosition.setText(mUser.getPositionName());
            mTvDepartName.setText(mUser.getDepartmentName());
        }
        mTvUserNameMine.setText(mUser.getUserName());
        TextDrawable headPic = TextHeadPicUtil.getHeadPic(mUser.getUserName());
        mIvUserPic.setImageDrawable(headPic);

        if (mUser.getCompanyId().equals("0")) {
            mBtnQuitCompany.setVisibility(View.GONE);
        } else {
            mBtnQuitCompany.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.btn_quit_company, R.id.rl_userName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_quit_company:
                quitCompanyDialog();
                break;
            case R.id.rl_userName:
                updateUserNameDialog();
                break;
        }
    }

    private void updateUserNameDialog() {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
        mEditText = builder.getEditText();
        builder.setPlaceholder(mUser.getUserName())
                .setTitle("更改用户名字")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        updateNamePost();
                    }
                })
                .show();
    }

    private void quitCompanyDialog() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("退出当前企业")
                .setMessage("退出后,将预约不了此企业的会议室并且无法浏览此企业的会议记录,确定要退出吗?")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        quitPost();
                    }
                })
                .show();
    }

    private void quitPost() {

        Observer<HttpResult> observer = new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                if (result.getCode() == 1) {
                    User user = User.getUserFromCache();
                    user.setCompanyId("0");
                    User.save(user);
                    _mActivity.finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        CompanyRequest request = new CompanyRequest();
        request.quitCompany(observer);

    }

    private void updateNamePost() {
        final String theNewUserName = mEditText.getText().toString();
        UserLoginRequest request = UserLoginRequest.getInstance();


        Observer<HttpResult> observer = new Observer<HttpResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HttpResult result) {
                if (result.getCode() == 1) {
                    User user = User.getUserFromCache();
                    user.setUserName(theNewUserName);
                    User.save(user);
                }
                initView();
                EventBus.getDefault().post(new ApplyEntity());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        request.updateUserName(observer, theNewUserName);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
