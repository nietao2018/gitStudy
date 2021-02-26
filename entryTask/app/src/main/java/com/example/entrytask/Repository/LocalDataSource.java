package com.example.entrytask.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.entrytask.Entity.VideoEntity;
import com.example.entrytask.Room.VideoDao;
import com.example.entrytask.Room.VideoRoomDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.security.auth.callback.Callback;

public class LocalDataSource {

    private VideoDao mVideoDao;
    private List<VideoEntity> mAllVideos;


    private static final int NUMBEROFTHREADS = 4;
    static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBEROFTHREADS);

    public LocalDataSource(Application application){
        VideoRoomDatabase db = VideoRoomDatabase.getDatabase(application);
        mVideoDao = db.videoDao();
        mAllVideos = this.getLocalVideos();
    }

    public List<VideoEntity> getLocalVideos(){
        try{
            Future localData  = databaseExecutor.submit(new GetLocalData());
            mAllVideos =(List<VideoEntity>) localData.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mAllVideos;
    }

    public void insert(VideoEntity videoEntity){
        databaseExecutor.execute(() -> {
            mVideoDao.insertVideo(videoEntity);
        });
    }

    private class GetLocalData implements Callable{

        @Override
        public Object  call() {
            return mVideoDao.getLocalVideos();
        }
    }
}
