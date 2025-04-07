package com.example.movieapp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Interfaces.MovieClickListener;
import com.example.movieapp.R;

public class MovieFavViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageMoviePoster;
    public TextView title;
    public TextView year;
    private MovieClickListener clickListener;

    public MovieFavViewHolder(@NonNull View itemView, MovieClickListener clickListener) {
        super(itemView);

        imageMoviePoster = itemView.findViewById(R.id.imageMoviePoster);
        title = itemView.findViewById(R.id.textTitle);
        year = itemView.findViewById(R.id.textYear);
        this.clickListener = clickListener;

        itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        });
    }
}
