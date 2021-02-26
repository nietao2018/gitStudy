package com.example.entrytask.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrytask.R;
import com.example.entrytask.Entity.VideoEntity;

import java.util.List;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_VIDEO = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<VideoEntity> mVideoEntityList;
    private final View.OnClickListener mOnClickListener;

    public VideoPlayerRecyclerAdapter(List<VideoEntity> videoEntityList, View.OnClickListener mOnClickListener) {
        this.mVideoEntityList = videoEntityList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_VIDEO) {
            View  view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
            return new VideoPlayerViewHolder(view);
        }else{

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more, parent, false);
            view.findViewById(R.id.button_loadMore).setOnClickListener(mOnClickListener);

            return new LoadMoreViewHolder(view);
        }
    }

    public void setVideos(List<VideoEntity> videos){
        this.mVideoEntityList = videos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  LoadMoreViewHolder){
            ((LoadMoreViewHolder) holder).onBind();
        }
        if(holder instanceof VideoPlayerViewHolder){
            ((VideoPlayerViewHolder) holder).onBind(mVideoEntityList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mVideoEntityList.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_VIDEO;
    }

    @Override
    public int getItemCount() {
        return mVideoEntityList == null ? 0 : mVideoEntityList.size();
    }

    /**
     * clear all data when pull down to load more
     */
    public void clear() {
        mVideoEntityList.clear();
        notifyDataSetChanged();
    }

}
