package com.gzz100.zbh.home.mine.bean;

import java.io.File;

/**
 * Created by Lam on 2018/7/13.
 */

public class FileEntity {
    File mFile;
    boolean isSelect;

    public FileEntity(File file) {
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setChange(){
        isSelect = !isSelect;
    }
}
