package com.example.entrytask.MVP;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.entrytask.R;
import com.example.entrytask.Utils.Constant;
import com.example.entrytask.Utils.PlayerUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class DetailPageActivity extends AppCompatActivity implements DetailPageView {



    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private TextView mTextView;

    private DetailPagePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_page);
        getSupportActionBar().hide();

        mPresenter = new DetailPagePresenter(this);
        mPlayerView = findViewById(R.id.media_view);
        mTextView = findViewById(R.id.textview_title);

        Intent intent = getIntent();

        mPlayer = PlayerUtils.onInitialPlayer(getApplicationContext());
        mPresenter.onParseIntent(intent);

        mPlayerView.setPlayer(mPlayer);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        Log.d("detail","onCreate");

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        boolean playbackState = mPlayer.getPlayWhenReady();
        long playbackPosition = mPlayer.getCurrentPosition();
        intent.putExtra(Constant.PLAYBACK_STATE, playbackState);
        intent.putExtra(Constant.PLAYBACK_POSITION,playbackPosition);
        setResult(Constant.REQUEST_CODE,intent);
        Log.d("onBack",": is backed");
        finish();

    }

    @Override
    public void setPlayPosition(int mCurrentWindowIndex, long playPosition) {
        Log.d("set",":" + playPosition);
        mPlayer.seekTo(mCurrentWindowIndex, playPosition);
        
    }

    @Override
    public void setPlayState(boolean playState) {
        mPlayer.setPlayWhenReady(playState);
    }

    @Override
    public void setVideoTitle(String videoTitle) {
        mTextView.setText(videoTitle);
    }

    @Override
    public void setVideoUrl(String url) {
        MediaSource mediaSource = PlayerUtils.buildMediaSource(Uri.parse(url), getApplicationContext());
        mPlayer.prepare(mediaSource);
    }
}
