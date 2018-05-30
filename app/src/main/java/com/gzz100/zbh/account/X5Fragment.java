package com.gzz100.zbh.account;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzz100.zbh.R;
import com.gzz100.zbh.base.BaseBackFragment;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class X5Fragment extends BaseBackFragment {

//    @BindView(R.id.x5Webview)
//    WebView mWebView;
    @BindView(R.id.listview)
    ListView mListView;
    private File mDir;

    @Override
    protected View onCreateView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_x5, null);
        ButterKnife.bind(this,inflate);
        X5FragmentPermissionsDispatcher.initViewWithPermissionCheck(this);
        initView();
        return attachToSwipeBack(inflate);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE})
    public void initView() {

        mDir = new File(Environment.getExternalStorageDirectory()+"/Download");
        Logger.i("file dir="+mDir.getAbsolutePath());

        mListView.setAdapter(new FileAdapter(mDir));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mDir.listFiles()[position].isDirectory()) {
                    HashMap<String,String> configValue = new HashMap<>();
                    //“true”表示是进入打开方式选择界面，如果不设置或设置为“false” ，则进入 miniqb 浏览器模式。
                    configValue.put("style","1");
                    configValue.put("local","false");
                    configValue.put("topBarBgColor","#2196F3");
                    try{
                        QbSdk.openFileReader(getContext(),mDir.listFiles()[position].getAbsolutePath(),configValue,null );
                    }catch (Exception e){
                        Logger.e(e.getMessage());
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "这是文件夹", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        X5FragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    class FileAdapter extends BaseAdapter{

        File[] fileList ;

        public FileAdapter(File fileDir) {
            this.fileList = fileDir.listFiles();
        }

        @Override
        public int getCount() {
            Logger.i("getCount="+fileList.length);
            return fileList==null?0:fileList.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            View rootView = null;
            if (convertView==null){
                rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_file_name, null);
                textView = rootView.findViewById(R.id.tv_fileName);
                rootView.setTag(textView);
            }else {
                rootView = convertView;
                textView = (TextView) convertView.getTag();
            }
            textView.setText(fileList[position].getName());

            return rootView;
        }
    }

}
