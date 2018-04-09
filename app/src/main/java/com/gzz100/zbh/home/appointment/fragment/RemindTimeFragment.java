package com.gzz100.zbh.home.appointment.fragment;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemindTimeFragment extends BaseFragment {


    @BindView(R.id.topbar)
    QMUITopBar mTopbar;
    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupListView;
    Unbinder unbinder;

    public RemindTimeFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_remind_time, null);

        unbinder = ButterKnife.bind(this, inflate);
        initTopBar();
        initGroupListView();
        return inflate;
    }

    private void initTopBar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        mTopbar.setTitle("提醒时间");
    }

    private void initGroupListView() {
        QMUICommonListItemView normalItem = mGroupListView.createItemView("Item 1");
        normalItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView("Item 2");
        itemWithDetail.setDetailText("在右方的详细信息");

        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");
        QMUICommonListItemView itemView = mGroupListView.createItemView(null, "15分钟", "提前15分钟提醒",
                QMUICommonListItemView.VERTICAL, QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);

        itemView.setRedDotPosition(QMUICommonListItemView.REDDOT_POSITION_RIGHT);
        itemView.showRedDot(true);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        };

        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .addItemView(normalItem, onClickListener)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemView, onClickListener)
                .addTo(mGroupListView);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
