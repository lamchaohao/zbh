package com.gzz100.zbh.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.gzz100.zbh.App;
import com.gzz100.zbh.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.File;

import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FileUtils;

import static com.gzz100.zbh.res.Common.AUTHORITIES;

/**
 * Created by Lam on 2018/5/21.
 */

public class ShareToWeixin {
    @Deprecated
    public static void shareUrlToWX(Context context, String text, String url) {
        if (!uninstallSoftware(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_SEND);

        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setType("text/*");
        intent.putExtra("Kdescription", text);
        intent.putExtra(Intent.EXTRA_TEXT, "给你分享一个链接:"+url);
        context.startActivity(intent);
    }


    public static void shareDocumentToWechat(Activity context, String docUrl, String docName, String docType,boolean isTimeLine){
        if (!uninstallSoftware(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webobject=new WXWebpageObject();
        webobject.webpageUrl = docUrl;
        WXMediaMessage msg=new WXMediaMessage(webobject);
        msg.description=docName;
        FilePickerConst.FILE_TYPE fileType = FileUtils.getFileType(docType);
        Bitmap bitmapIcon=null;
        switch (fileType){
            case PDF:
                bitmapIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pdf);
                break;
            case PPT:
                bitmapIcon=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_power_point);
                break;
            case WORD:
                bitmapIcon=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_word);
                break;
            case EXCEL:
                bitmapIcon=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_excel);
                break;
            case UNKNOWN:
                bitmapIcon=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_home_message_selected);
                break;
        }
//        bitmapIcon=BitmapFactory.decodeResource(getResources(),R.drawable.ic_message_primary_24dp);
        msg.thumbData = WechatUtil.bmpToByteArray(bitmapIcon,true);
        msg.title=docName;
        SendMessageToWX.Req req= new SendMessageToWX.Req();
        req.transaction=buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeLine?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
        ((App)context.getApplication()).getWxapi().sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static void shareFileToWX(Context context,File file){
        if (!uninstallSoftware(context, "com.tencent.mm")) {
            Toast.makeText(context, "微信没有安装！", Toast.LENGTH_SHORT).show();
            return;
        }
        //com.tencent.mm.ui.tools.ShareImgUi   是分享到微信好友

        //com.tencent.mm.ui.tools.ShareToTimeLineUI  是分享到微信朋友圈

        //intent.put("kdescription","分享朋友圈的图片说明");
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);

        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            contentUri = FileProvider.getUriForFile(context, AUTHORITIES, file);
        } else {
            contentUri=Uri.fromFile(file);
        }

        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        try {
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }



    }



    private static boolean uninstallSoftware(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
