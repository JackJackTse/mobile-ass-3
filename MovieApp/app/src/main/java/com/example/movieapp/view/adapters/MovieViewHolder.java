package com.example.movieapp.view.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.Interfaces.MovieClickListener;
import com.example.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    // Attributes
    ImageView imageMoviePoster;
    TextView title;
    TextView year;
    MovieClickListener clickListener;

    public MovieViewHolder(@NonNull View itemView, MovieClickListener clickListener) {
        super(itemView);

        imageMoviePoster =itemView.findViewById(R.id.imageMoviePoster);
        title = itemView.findViewById(R.id.textTitle);
        year = itemView.findViewById(R.id.textYear);

        this.clickListener = clickListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getAdapterPosition());

            }
        });
    }


}
