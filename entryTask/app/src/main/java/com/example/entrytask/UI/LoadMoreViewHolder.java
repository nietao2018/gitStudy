package com.example.entrytask.UI;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entrytask.R;

/**
 * @author tao.nie
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    public Button loadMoreButton;
    public View parentView;

    public LoadMoreViewHolder(@NonNull View itemView) {
        super(itemView);
        parentView = itemView;
        loadMoreButton = itemView.findViewById(R.id.button_loadMore);

        if(loadMoreButton == null){
            Log.d("loadMore",": is null");
        }else{
            Log.d("loadMore",": is not null");
        }
    }

    public void onBind() {
        parentView.setTag(this);
        loadMoreButton.setText("load more");
    }

}
