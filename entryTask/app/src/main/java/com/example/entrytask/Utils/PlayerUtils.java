package com.example.entrytask.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerUtils {

    public static MediaSource buildMediaSource(Uri uri, Context context) {

        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "entryTask"));
        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);
        Handler mainHandler = new Handler();
        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
        return new HlsMediaSource(uri, dataSourceFactory, mainHandler, null);
    }

    public static SimpleExoPlayer onInitialPlayer(Context context){
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // create a new instance of SimpleExoPlayer
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            //loadControl
            LoadControl loadControl = new DefaultLoadControl(
                    new DefaultAllocator(true, 16),
                    Constant.MIN_BUFFER_DURATION,
                    Constant.MAXBUFFER_DURATION,
                    Constant.MIN_PLAYBACK_START_BUFFER,
                    Constant.MIN_PLAYBACK_RESUME_BUFFER, -1, true);
            // 2. Create the player
            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector,loadControl);

        return  player;
    }
}