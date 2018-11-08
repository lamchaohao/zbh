package com.gzz100.zbh.home.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.TbsCoreLoadStat;

import java.lang.reflect.Method;

/**
 * Created by Lam on 2018/9/19.
 */

public class RefectFragment extends BaseBackFragment {


    @Override
    protected View onCreateView(LayoutInflater inflater) {

        return inflater.inflate(R.layout.fragment_reg,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        TbsCoreLoadStat instance = TbsCoreLoadStat.getInstance();
        Class<? extends TbsCoreLoadStat> aClass = instance.getClass();
        Logger.i(aClass.getName());
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            StringBuilder sb=new StringBuilder();
            sb.append("paraType=");
            for (Class<?> parameterType : parameterTypes) {
                sb.append(parameterType.getName()).append(",");
            }
            Logger.i("declaredMethod:"+declaredMethod.getName()+","+sb.toString());
        }


    }


}
