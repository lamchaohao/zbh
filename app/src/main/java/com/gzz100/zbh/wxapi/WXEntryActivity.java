package com.gzz100.zbh.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gzz100.zbh.App;
import com.gzz100.zbh.R;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import es.dmoral.toasty.Toasty;

/**
 * Created by Lam on 2018/5/23.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_entry);
        api=((App)getApplication()).getWxapi();
        handleIntent();
    }

    private void handleIntent() {
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
//        switch (baseReq.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) baseReq);
//                break;
//            default:
//                break;
//        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        String errmsg="";
        switch (baseResp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                errmsg="正确返回";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                errmsg="用户取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                errmsg="认证被否决";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                errmsg="发送失败";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                errmsg="不支持错误";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                errmsg="一般错误";
                break;
            default:
                //其他不可名状的情况
                errmsg="其他不可名状的情况";
                break;
        }
        finish();
        Logger.i("baseResp.errCode="+baseResp.errCode);
        Toasty.error(this,errmsg).show();

    }
}
