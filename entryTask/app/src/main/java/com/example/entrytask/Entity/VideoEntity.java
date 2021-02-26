package com.example.entrytask.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "video_table")
public class VideoEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "record_id")
    @SerializedName("record_id")
    @Expose
    private int record_id;
    @ColumnInfo(name = "session_id")
    @SerializedName("session_id")
    @Expose
    private int session_id;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @ColumnInfo(name = "uid")
    @SerializedName("uid")
    @Expose
    private int uid;
    @ColumnInfo(name = "shop_id")
    @SerializedName("shop_id")
    @Expose
    private int shop_id;
    @ColumnInfo(name = "username")
    @SerializedName("username")
    @Expose
    private String username;
    @ColumnInfo(name = "avatar")
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @ColumnInfo(name = "cover")
    @SerializedName("cover")
    @Expose
    private String cover;
    @ColumnInfo(name = "view_count")
    @SerializedName("view_count")
    @Expose
    private int view_count;
    @ColumnInfo(name = "session_mem_count")
    @SerializedName("session_mem_count")
    @Expose
    private int session_mem_count;
    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;
    @ColumnInfo(name = "format")
    @SerializedName("format")
    private String format;

    public VideoEntity(int record_id, int session_id, String title, int uid, int shop_id,
                       String username, String avatar, String cover, int view_count, int session_mem_count,
                       String url, String format){
        this.record_id = record_id;
        this.session_id = session_id;
        this.title = title;
        this.uid = uid;
        this.shop_id = shop_id;
        this.username = username;
        this.avatar = avatar;
        this.cover = cover;
        this.view_count = view_count;
        this.session_mem_count = session_mem_count;
        this.url = url;
        this.format = format;
    }

    public void setRecord_id(int record_id){
        this.record_id = record_id;
    }
    public int getRecord_id(){
        return this.record_id;
    }
    public void setSession_id(int session_id){
        this.session_id = session_id;
    }
    public int getSession_id(){
        return this.session_id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setUid(int uid){
        this.uid = uid;
    }
    public int getUid(){
        return this.uid;
    }
    public void setShop_id(int shop_id){
        this.shop_id = shop_id;
    }
    public int getShop_id(){
        return this.shop_id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public void setCover(String cover){
        this.cover = cover;
    }
    public String getCover(){
        return this.cover;
    }
    public void setView_count(int view_count){
        this.view_count = view_count;
    }
    public int getView_count(){
        return this.view_count;
    }
    public void setSession_mem_count(int session_mem_count){
        this.session_mem_count = session_mem_count;
    }
    public int getSession_mem_count(){
        return this.session_mem_count;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setFormat(String format){
        this.format = format;
    }
    public String getFormat(){
        return this.format;
    }

    @Override
    public VideoEntity clone() {
        return new VideoEntity(this.record_id,
        this.session_id ,
        this.title,
        this.uid,
        this.shop_id,
        this.username,
        this.avatar,
        this.cover,
        this.view_count,
        this.session_mem_count,
        this.url,
        this.format);
    }
}
