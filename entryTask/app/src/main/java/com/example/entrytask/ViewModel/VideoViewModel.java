package com.example.entrytask.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.entrytask.Entity.VideoEntity;
import com.example.entrytask.Repository.VideoRepository;
import com.example.entrytask.Utils.NetWorkUtils;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {


    private VideoRepository mRepository;
    private Application mApplication;
    private int offset = 0;

    public MutableLiveData<Boolean> isRefreshing;
    public MutableLiveData<Boolean> netOfRefreshing;
    public MutableLiveData<List<VideoEntity>> videosList;
    public MutableLiveData<Boolean> netOfLoadingMore;
    public MutableLiveData<Boolean> isLocalCacheEmpty;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
        this.mRepository = new VideoRepository(application);
        this.isRefreshing = new MutableLiveData<>(false);
        this.netOfRefreshing = new MutableLiveData<>(true);
        this.netOfLoadingMore = new MutableLiveData<>(true);
        this.isLocalCacheEmpty = new MutableLiveData<>(false);
        init();
    }

    public void init(){
        if(mRepository.onLocalDataSourceIsEmpty()){
            // local cache is empty
            if (NetWorkUtils.isNetworkAvailable(mApplication.getApplicationContext())) {
                videosList  = mRepository.getRemoteVideos(0);
            }else{
                //error page;
            }
        }else{
            videosList = mRepository.getLocalVideos();
            if (NetWorkUtils.isNetworkAvailable(mApplication.getApplicationContext())) {
                videosList = mRepository.getRemoteVideos(0);
            }
        }
    }

    public void onRefresh(){
        offset = 0;
        isRefreshing.setValue(true);
        if (NetWorkUtils.isNetworkAvailable(mApplication.getApplicationContext())) {
            // network is ok
            netOfRefreshing.setValue(true);
            mRepository.getRemoteVideos(offset);
            Log.d("onRefresh:","" + videosList.getValue().size());
        } else {
            // network is error
            netOfRefreshing.setValue(false);
            if(mRepository.onLocalDataSourceIsEmpty()){
                // local cache is empty
                isLocalCacheEmpty.setValue(true);
            }else{
                // local cache is not empty
                isLocalCacheEmpty.setValue(false);
                videosList = mRepository.getLocalVideos();
            }
        }
        isRefreshing.setValue(false);
    }

    /**
     * pull up to load more
     */
    public void onLoadMore(){
        if (NetWorkUtils.isNetworkAvailable(mApplication.getApplicationContext())) {
            // network is ok
            netOfLoadingMore.setValue(true);
            mRepository.getRemoteVideos(++offset);
        } else {
            netOfLoadingMore.setValue(false);
        }
    }
}
