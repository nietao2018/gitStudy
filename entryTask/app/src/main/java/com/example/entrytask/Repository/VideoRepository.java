package com.example.entrytask.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.entrytask.Entity.VideoEntity;

import java.util.List;

public class VideoRepository {

    public MutableLiveData<List<VideoEntity> > allVideos;

    private LocalDataSource mLocalDataSource;

    private RemoteDataSource mRemoteDataSource;

    private Application mApplication;


    public VideoRepository(Application application){
        mLocalDataSource = new LocalDataSource(application);
        mRemoteDataSource = RemoteDataSource.getInstance();
        mApplication = application;
        allVideos = new MutableLiveData<>();
        getRemoteVideos(0);
    }

    public MutableLiveData<List<VideoEntity>> getRemoteVideos(int offset) {
        mRemoteDataSource.getDataSource(offset, new CallBack<List<VideoEntity>>() {
            @Override
            public void returnResult(List<VideoEntity> videoEntities) {
                if(offset == 0){
                    videoEntities.add(null);
                    Log.d("onNext",":"+videoEntities.size());
                    allVideos.setValue(videoEntities);
                }else{
                    List<VideoEntity> currentValue = allVideos.getValue();
                    currentValue.remove(currentValue.size()-1);
                    for(VideoEntity videoEntity : videoEntities){
                        currentValue.add(videoEntity);
                    }
                    currentValue.add(null);
                    allVideos.setValue(currentValue);
                }
                onUpdateLocalCache(videoEntities);
            }
            @Override
            public void returnError(String message) {
                Log.d("returnError",":error");
            }
        });
        return allVideos;
    }

    public MutableLiveData<List<VideoEntity>> getLocalVideos(){
        List<VideoEntity> videoList = mLocalDataSource.getLocalVideos();
        if(videoList != null){
            videoList.add(null);
        }

        allVideos.setValue(videoList);
        return allVideos;
    }

    public void onUpdateLocalCache(List<VideoEntity> videoEntityList){
        if(videoEntityList.size() == 0){
            return;
        }else{
            for(int i = 0; i< videoEntityList.size() -1; i++){
                mLocalDataSource.insert(videoEntityList.get(i));
            }
        }
    }

    public boolean onLocalDataSourceIsEmpty(){
        if(mLocalDataSource.getLocalVideos() != null && mLocalDataSource.getLocalVideos().size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
