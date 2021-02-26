package com.example.entrytask.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.entrytask.Room.VideoRoomDatabase;
import com.example.entrytask.WebService.APIClient;
import com.example.entrytask.WebService.APIInterface;
import com.example.entrytask.Entity.ResponseEntity;
import com.example.entrytask.Entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {

    private static RemoteDataSource mRemoteDatabase;
    private APIInterface mAPIInterface;

    public static RemoteDataSource getInstance(){
        if (mRemoteDatabase == null) {
            synchronized (RemoteDataSource.class) {
                if (mRemoteDatabase == null) {
                    mRemoteDatabase = new RemoteDataSource();
                }
            }
        }
        return mRemoteDatabase;
    }

    public RemoteDataSource(){
        mAPIInterface = APIClient.getClient()
                .create(APIInterface.class);
    }
    /**
     * get Remote dataSource
     * @param offset
     * @param callback
     * @return
     */

    public void getDataSource(int offset ,final CallBack<List<VideoEntity>> callback){
        mAPIInterface.getVideoList(offset,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onNext(ResponseEntity value) {
                        callback.returnResult(value.getData().getVideo());
                    }
                    @Override
                    public void onError(Throwable e) {
                        callback.returnError(e.getMessage());
                    }
                    @Override
                    public void onComplete() {}
                });

    }
}
