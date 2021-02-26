package com.example.entrytask.UI;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrytask.R;
import com.example.entrytask.Entity.VideoEntity;
import com.squareup.picasso.Picasso;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    private final static String mUrlPrefix;
    static {
        mUrlPrefix = "https://cf.shopee.co.id/file/";
    }

    public FrameLayout videoContainer;
    public TextView videoTitleTextView;
    public ImageView videoImageView;
    public ProgressBar videoLoadingProgressBar;
    public View parentView;
    public ImageView fullScreenImageView;



    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);

        parentView = itemView;
        videoContainer = itemView.findViewById(R.id.video_container);
        videoTitleTextView = itemView.findViewById(R.id.title);
        videoImageView = itemView.findViewById(R.id.image_view);
        videoLoadingProgressBar = itemView.findViewById(R.id.progressBar);
        fullScreenImageView = videoImageView.findViewById(R.id.exo_fullscreen_icon);
    }

    public void onBind(VideoEntity videoEntity){
        parentView.setTag(this);
        if(videoEntity== null) {
            return;
        }
        videoTitleTextView.setText(videoEntity.getTitle());
        //Picasso set image
        String imageUri = mUrlPrefix + videoEntity.getCover();
        Picasso.get().load(imageUri).into(videoImageView);
    }
}
