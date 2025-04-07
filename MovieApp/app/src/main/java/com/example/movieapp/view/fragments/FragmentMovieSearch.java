package com.example.movieapp.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieapp.Interfaces.MovieClickListener;
import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentMovieSearchBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.view.MovieDetailsActivity;
import com.example.movieapp.view.adapters.MovieAdapter;
import com.example.movieapp.viewmodel.MovieViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentMovieSearch extends Fragment implements MovieClickListener {

    private FragmentMovieSearchBinding binding;
    private ArrayList<Movie> movieList;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initial data ArrayList
        movieList = new ArrayList<>();

        // Set up the layout design for recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        // Initial Adapter
        movieAdapter = new MovieAdapter(requireContext(), movieList);
        binding.recyclerView.setAdapter(movieAdapter);
        movieAdapter.setClickListener(this);

        // Initial ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Data Observes
        movieViewModel.getMovieData().observe(getViewLifecycleOwner(), movieData -> {
            movieList.clear();
            movieList.addAll(movieData);
            movieAdapter.notifyDataSetChanged();
        });

        // Error updates
        movieViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        // Search Button
        binding.btnSearch.setOnClickListener(v -> {
            String searchTxt = binding.editTextSearch.getText().toString();
            movieViewModel.searchMovies(searchTxt);
        });
    }

    @Override
    public void onClick(View v, int pos) {
        Intent intent = new Intent(requireActivity(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE", movieList.get(pos));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}