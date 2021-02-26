package com.example.entrytask.MVP;

public interface DetailPageView {

    void setPlayPosition(int currentWindowIndex, long playPosition);
    void setPlayState(boolean playState);
    void setVideoTitle(String videoTitle);
    void setVideoUrl(String url);
}
