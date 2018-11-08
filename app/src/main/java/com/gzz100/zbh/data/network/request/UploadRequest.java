package com.gzz100.zbh.data.network.request;

import com.google.gson.Gson;
import com.gzz100.zbh.account.User;
import com.gzz100.zbh.data.entity.FileUploadEntity;
import com.gzz100.zbh.data.network.HttpResult;
import com.gzz100.zbh.data.network.client.HttpClient;
import com.gzz100.zbh.data.network.service.FileUploadService;
import com.gzz100.zbh.home.appointment.entity.VoteOption;
import com.gzz100.zbh.home.appointment.entity.VoteWrap;
import com.gzz100.zbh.utils.MD5Utils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Lam on 2018/3/22.
 */

public class UploadRequest {

    private final FileUploadService mFileUploadService;
    private final User mUser;

    public UploadRequest() {
        mUser = User.getUserFromCache();
        mFileUploadService = HttpClient.getInstance()
                .getRetrofit()
                .create(FileUploadService.class);
    }

    public void uploadFileList(Observer<HttpResult> observer, List<File> fileList, String meetingId){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        List<FileUploadEntity> uploadList = new ArrayList<>();
        for (File file : fileList){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), file);
            String fileMD5 = MD5Utils.getFileMD5(file);
            Logger.i(fileMD5);
            uploadList.add(new FileUploadEntity(2,2,fileMD5) );
            builder.addFormDataPart("file",file.getName(),requestFile);
        }
        Gson gson=new Gson();
        String uploadInfoJson = gson.toJson(uploadList);
        RequestBody requestBody = builder
                .addFormDataPart("userId", mUser.getUserId())
                .addFormDataPart("token", mUser.getToken())
                .addFormDataPart("meetingId",meetingId)
                .addFormDataPart("companyId",mUser.getCompanyId())
                .addFormDataPart("fileList",uploadInfoJson)
                .build();
        mFileUploadService.uploadFileList(requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public void uploadVoteList(Observer<HttpResult> observer, VoteWrap vote,String meetingId){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (VoteOption option : vote.getOptionList()){
            String picFilePath = option.getPicFile();
            if (picFilePath!=null){
                if (picFilePath.startsWith("http")) {
                    continue;
                }

                File picFile = new File(picFilePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), picFile);
                String fileMD5 = MD5Utils.getFileMD5(picFile);
                Logger.i(fileMD5);
                //有文件才设置md5值
                option.setFileMD5Value(fileMD5);
                builder.addFormDataPart("file",picFile.getName(),requestFile);
            }

        }
        Gson gson=new Gson();
        String uploadInfoJson = gson.toJson(vote.getOptionList());
        RequestBody requestBody = builder
                .addFormDataPart("userId", mUser.getUserId())
                .addFormDataPart("token", mUser.getToken())
                .addFormDataPart("companyId",mUser.getCompanyId())
                .addFormDataPart("meetingId",meetingId)
                .addFormDataPart("voteName",vote.getVoteName())
                .addFormDataPart("voteSelectableNum",vote.getMaxCount()+"")
                .addFormDataPart("voteDescription",vote.getVoteDespc())
                .addFormDataPart("voteOptionNameList",uploadInfoJson)
                .addFormDataPart("voteModel",vote.isAutoStart()?"1":"2")
                .addFormDataPart("voteAnonymous",vote.isHideName()?"1":"2")
                .build();

        mFileUploadService.uploadVote(requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    public void updateVote(Observer<HttpResult> observer, VoteWrap vote,String meetingId,String voteId){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (VoteOption option : vote.getOptionList()){
            String picFilePath = option.getPicFile();
            if (picFilePath!=null){
                if (picFilePath.startsWith("http")) {
                    continue;
                }
                Logger.i("picFilePath="+picFilePath);
                File picFile = new File(picFilePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), picFile);
                String fileMD5 = MD5Utils.getFileMD5(picFile);
                Logger.i(fileMD5);
                //有文件才设置md5值
                option.setFileMD5Value(fileMD5);
                builder.addFormDataPart("file",picFile.getName(),requestFile);
            }

        }
        Gson gson=new Gson();
        String uploadInfoJson = gson.toJson(vote.getOptionList());
        RequestBody requestBody = builder
                .addFormDataPart("userId", mUser.getUserId())
                .addFormDataPart("token", mUser.getToken())
                .addFormDataPart("companyId",mUser.getCompanyId())
                .addFormDataPart("meetingId",meetingId)
                .addFormDataPart("voteName",vote.getVoteName())
                .addFormDataPart("voteSelectableNum",vote.getMaxCount()+"")
                .addFormDataPart("voteDescription",vote.getVoteDespc())
                .addFormDataPart("voteOptionNameList",uploadInfoJson)
                .addFormDataPart("voteModel",vote.isAutoStart()?"1":"2")
                .addFormDataPart("voteAnonymous",vote.isHideName()?"1":"2")
                .addFormDataPart("voteId",voteId)
                .build();

        mFileUploadService.updateVote(requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    public void deleteFile(Observer<HttpResult> observer,String meetingId,String documentId){
        User user= User.getUserFromCache();
        mFileUploadService.deleteFile(user.getUserId(),user.getToken(),meetingId,documentId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



}
