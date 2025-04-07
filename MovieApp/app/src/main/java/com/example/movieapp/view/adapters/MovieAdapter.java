package com.example.movieapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Interfaces.MovieClickListener;
import com.example.movieapp.R;
import com.example.movieapp.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    //Attributes
    List<Movie> movieList;
    Context context;
    public MovieClickListener clickListener;

    // Constructor
    public MovieAdapter(Context context, List<Movie> movieList) {
        this.movieList=movieList;
        this.context=context;
    }

    // Reference clickListener to MainActivity
    public void setClickListener(MovieClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        MovieViewHolder movieViewHolder = new MovieViewHolder(itemView, this.clickListener);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        // Use Glide to load image
        Glide.with(context).load(movie.getPoster()).into(holder.imageMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
