package com.example.entrytask.Helper;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrytask.Utils.Constant;
import com.example.entrytask.R;
import com.example.entrytask.Entity.VideoEntity;
import com.example.entrytask.UI.LoadMoreViewHolder;
import com.example.entrytask.UI.VideoPlayerViewHolder;
import com.example.entrytask.UI.VideoRecycleView;
import com.example.entrytask.Utils.NetWorkUtils;
import com.example.entrytask.Utils.PlayerUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * @author tao.nie
 */
public class ExoPlayerHelper {


    private VideoRecycleView mVideoRecyclerView;
    //ui

    private ProgressBar mProgressBar;
    private View mViewHolderParent;
    private FrameLayout mFrameLayout;



    //vars
    private List<VideoEntity> mVideoEntityList;
    private int mVideoSurfaceDefaultheigh = 0;
    private int mScreenDefaultHeight = 0;
    private Context mContext;
    private int mPlayPosition = -1;
    private boolean mIsVideoViewAdded;

    public String videoUrl;
    public String title;

    private PlayerView exoplayerView;
    private SimpleExoPlayer simplePlayer;


    public ExoPlayerHelper(VideoRecycleView mVideoRecyclerView, SimpleExoPlayer player, PlayerView exoplayerView ){

        this.mVideoRecyclerView = mVideoRecyclerView;
        this.mContext = mVideoRecyclerView.getContext();
        this.simplePlayer = player;
        this.exoplayerView = exoplayerView;
        init();
    }

    private void init(){

        paramterInit();

        setVideoPlayerListener();
        setRecyclerViewScrollerListen();
        exoplayerView.setPlayer(simplePlayer);


    }
    private  void  paramterInit(){

        Display display = ((WindowManager) mVideoRecyclerView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mVideoSurfaceDefaultheigh = point.x;
        mScreenDefaultHeight = point.y;


    }


    private void setVideoPlayerListener(){
        simplePlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }
            @Override
            public void onLoadingChanged(boolean isLoading) {

            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(VISIBLE);
                        }
                        break;
                    case Player.STATE_ENDED:
                        simplePlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(GONE);
                        }
                        if(!mIsVideoViewAdded){
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {}
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}
            @Override

            public void onPlayerError(ExoPlaybackException error) {
                if(NetWorkUtils.isNetworkAvailable(mContext)){
                    Toast.makeText(mContext,"Play Error, Url is not valid", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"Play Error, Network is error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPositionDiscontinuity(int reason) {}

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

            @Override
            public void onSeekProcessed() {}
        });
    }

    private void playVideo(boolean isEndOfList) {
        int targetPosition;
        if(!isEndOfList){
            int startPosition = ((LinearLayoutManager) mVideoRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) mVideoRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            }
            else {
                targetPosition = startPosition;
            }
        }
        else{
            targetPosition = mVideoEntityList.size() - 2;
        }

        // targetPosition is out of range, return
        if( targetPosition < 0  && targetPosition > mVideoEntityList.size() - 1){
            return;
        }

            // video is already playing so return
        if (targetPosition == mPlayPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        mPlayPosition = targetPosition;
        if (exoplayerView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        exoplayerView.setVisibility(INVISIBLE);
        removeVideoView(exoplayerView);

        int currentPosition = targetPosition - ((LinearLayoutManager) mVideoRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        View child = mVideoRecyclerView.getChildAt(currentPosition);
        if (child == null) {
            return;
        }
        if(child.getTag() instanceof LoadMoreViewHolder) {
            return;
        }
        VideoPlayerViewHolder holder = (VideoPlayerViewHolder) child.getTag();
        if (holder == null) {
            mPlayPosition = -1;
            return;
        }
        mProgressBar = holder.videoLoadingProgressBar;
        mViewHolderParent = holder.itemView;
        mFrameLayout = holder.itemView.findViewById(R.id.video_container);

        exoplayerView.setPlayer(simplePlayer);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                mContext, Util.getUserAgent(mContext, "RecyclerView VideoPlayer"));

        if(mVideoEntityList.get(targetPosition) == null) {
            return;
        }

        videoUrl = mVideoEntityList.get(targetPosition).getUrl();
        title = mVideoEntityList.get(targetPosition).getTitle();

        if (videoUrl != null) {
            MediaSource videoSource = new HlsMediaSource(Uri.parse(videoUrl),
                    dataSourceFactory, new Handler(), null);

            simplePlayer.prepare(videoSource);
            simplePlayer.setPlayWhenReady(true);
        }
    }
    private void setRecyclerViewScrollerListen(){
        mVideoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("RecylerView init", "onScrollStateChanged: called.");
                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if(!recyclerView.canScrollVertically(1)){
                        playVideo(true);
                    }else{
                        playVideo(false);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mVideoRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {}
            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (mViewHolderParent != null && mViewHolderParent.equals(view)) {
                    resetVideoView();
                }
            }
        });
    }

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) mVideoRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        View child = mVideoRecyclerView.getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + mVideoSurfaceDefaultheigh;
        } else {
            return mScreenDefaultHeight - location[1];
        }
    }

    private void removeVideoView(com.google.android.exoplayer2.ui.PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }
        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            mIsVideoViewAdded = false;
            mViewHolderParent.setOnClickListener(null);
        }
    }

    private void addVideoView(){
        mFrameLayout.addView(exoplayerView);
        mIsVideoViewAdded = true;
        exoplayerView.requestFocus();
        exoplayerView.setVisibility(VISIBLE);
        exoplayerView.setAlpha(1);
    }

    private void resetVideoView(){
        if(mIsVideoViewAdded){
            removeVideoView(exoplayerView);
            mPlayPosition = -1;
            exoplayerView.setVisibility(INVISIBLE);
        }
    }

    public void setVideos(List<VideoEntity> list){
        mVideoEntityList = list;
    }

    public SimpleExoPlayer getSimplePlayer() {
        return simplePlayer;
    }

}
