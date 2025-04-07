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

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavViewHolder> {
    private List<Movie> favMovieList;
    private Context context;
    private MovieClickListener clickListener;

    public MovieFavAdapter(Context context, List<Movie> movieList) {
        this.favMovieList = movieList;
        this.context = context;
    }

    public void setClickListener(MovieClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_movie, parent, false);
        return new MovieFavViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavViewHolder holder, int position) {
        Movie movie = favMovieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        
        // Load image using Glide
        Glide.with(context)
            .load(movie.getPoster())
            .into(holder.imageMoviePoster);
    }

    @Override
    public int getItemCount() {
        return  favMovieList.size();
    }
}