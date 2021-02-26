package com.example.entrytask.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataEntity {

    @SerializedName("list")
    @Expose
    private List<VideoEntity> video;

    @SerializedName("has_more")
    @Expose
    private boolean has_more;

    public DataEntity(List<VideoEntity> video, boolean has_more){
        this.video = video;
        this.has_more = has_more;
    }
    public void setVideo(List<VideoEntity> video){
        this.video = video;
    }
    public List<VideoEntity> getVideo(){
        return this.video;
    }
    public void setHas_more(boolean has_more){
        this.has_more = has_more;
    }
    public boolean getHas_more(){
        return this.has_more;
    }
}
