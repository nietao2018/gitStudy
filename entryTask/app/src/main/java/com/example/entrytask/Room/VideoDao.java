package com.example.entrytask.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.entrytask.Entity.VideoEntity;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECt * from video_table")
    List<VideoEntity> getLocalVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(VideoEntity videoEntity);

    @Query("DELETE FROM video_table")
    void deleteAll();
}
