package com.example.entrytask.WebService;

import com.example.entrytask.Entity.ResponseEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("replay")
    Observable<ResponseEntity> getVideoList(@Query("offset") int offset, @Query("limit") int limit);

}

