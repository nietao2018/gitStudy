package com.example.entrytask.MVP;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.entrytask.Utils.Constant;

import java.net.BindException;


public class DetailPagePresenter{

    private DetailPageView view;

    public DetailPagePresenter(DetailPageView view){
        this.view = view;
    }

    public void onParseIntent(Intent intent){
        updateURI(intent);
        updatePlayState(intent);
        updatePlayPosition(intent);
        updateTitle(intent);
    }

    private void updatePlayPosition(Intent intent){
        long mPlayPosition = intent.getLongExtra(Constant.PLAYBACK_POSITION, 0);
        int mCurrentWindowIndex = intent.getIntExtra(Constant.CURRENT_WINDOW_INDEX,0);
        Log.d("intent",":playback_positon:"+ mPlayPosition);
        view.setPlayPosition(mCurrentWindowIndex, mPlayPosition);
    }


    private void updatePlayState(Intent intent){
        boolean playState = intent.getBooleanExtra(Constant.PLAYBACK_STATE,false);
        view.setPlayState(playState);
    }
    private void updateTitle(Intent intent){
        String title = intent.getStringExtra(Constant.VIDEO_TITLE);
        if(title != null && !title.trim().equals("")){
            view.setVideoTitle(title);
        }

    }

    private void updateURI(Intent intent){
        String url = intent.getStringExtra(Constant.VIDEO_URI);
        if(url == null) Log.d("Url","is null");
        view.setVideoUrl(url);
    }



}
