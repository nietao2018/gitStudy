package com.example.entrytask;

import android.content.Intent;
import android.os.Bundle;

import com.example.entrytask.Helper.ExoPlayerHelper;
import com.example.entrytask.MVP.DetailPageActivity;
import com.example.entrytask.UI.VideoPlayerRecyclerAdapter;
import com.example.entrytask.UI.VideoRecycleView;
import com.example.entrytask.Utils.Constant;
import com.example.entrytask.Utils.PlayerUtils;
import com.example.entrytask.ViewModel.VideoViewModel;
import com.example.entrytask.Entity.VideoEntity;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideoRecycleView mVideoRecylerView;
    private SwipeRefreshLayout mSwipeContainer;
    private ExoPlayerHelper mExoPlayerBusiness;
    private View.OnClickListener mLoadMoreClickListener;
    private VideoPlayerRecyclerAdapter mAdapter;
    private VideoViewModel mVideoViewModel;
    private SimpleExoPlayer mPlayer;
    private PlayerView exoplayerView;
    private ImageView mFullScreenImageView;

    private boolean mFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoRecylerView = findViewById(R.id.recycler_view);
        mSwipeContainer = findViewById(R.id.swipeContainer);

        mPlayer = PlayerUtils.onInitialPlayer(this);

        exoplayerView = new PlayerView(this);
        exoplayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        exoplayerView.setUseController(true);

        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVideoViewModel.onRefresh();
            }
        });

        mVideoViewModel.isRefreshing.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mSwipeContainer.setRefreshing(aBoolean);
            }
        });

        mVideoViewModel.netOfRefreshing.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    Toast.makeText(getApplicationContext(),
                            "NetworkError",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mVideoViewModel.isLocalCacheEmpty.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    //setContentView(R.layout.error_page);
                }
            }
        });

        mVideoViewModel.netOfLoadingMore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getApplicationContext(),
                            "Load Success",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Network Error, Cannot Load More",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLoadMoreClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVideoViewModel.onLoadMore();
            }
        };

        mExoPlayerBusiness = new ExoPlayerHelper(mVideoRecylerView, mPlayer, exoplayerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVideoRecylerView.setLayoutManager(layoutManager);
        mAdapter = new VideoPlayerRecyclerAdapter(new ArrayList<VideoEntity>(), mLoadMoreClickListener);
        mVideoRecylerView.setAdapter(mAdapter);
        mFullScreenImageView = exoplayerView.findViewById(R.id.exo_fullscreen_icon);


        mFullScreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFullScreenButtonClicked();
            }
        });

        mVideoViewModel.videosList.observe(this, new Observer<List<VideoEntity>>() {
            @Override
            public void onChanged(List<VideoEntity> videoEntities) {
                Log.d("Videos",":" + videoEntities.size());
                mExoPlayerBusiness.setVideos(videoEntities);
                mAdapter.setVideos(videoEntities);
            }
        });

    }
    /**
     * get player state result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constant.REQUEST_CODE && intent != null) {
            long playerback_position = intent.getLongExtra(Constant.PLAYBACK_POSITION,0);
            boolean playback_state = intent.getBooleanExtra(Constant.PLAYBACK_STATE,false);
            mPlayer.setPlayWhenReady(playback_state);
            mPlayer.seekTo(playerback_position);
        }
    }

    /**
     * full Screen button click Listener
     */
    private void onFullScreenButtonClicked(){
        if(mFullscreen) {
            mFullscreen = false;
        }else{
            mFullscreen = true;
            Intent intent = new Intent(MainActivity.this, DetailPageActivity.class);
            int currentWindowIndex = mExoPlayerBusiness.getSimplePlayer().getCurrentWindowIndex();
            long playbackPosition = mExoPlayerBusiness.getSimplePlayer().getCurrentPosition();
            boolean playbackState = mExoPlayerBusiness.getSimplePlayer().getPlayWhenReady();
            String videoUrl = mExoPlayerBusiness.videoUrl;
            String videoTitle = mExoPlayerBusiness.title;

            intent.putExtra(Constant.VIDEO_URI, videoUrl);
            intent.putExtra(Constant.VIDEO_TITLE, videoTitle);
            intent.putExtra(Constant.CURRENT_WINDOW_INDEX, currentWindowIndex);
            intent.putExtra(Constant.PLAYBACK_POSITION, playbackPosition);
            intent.putExtra(Constant.PLAYBACK_STATE, playbackState);

            startActivityForResult(intent, Constant.REQUEST_CODE);
        }
    }

}