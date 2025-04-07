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
import com.example.movieapp.databinding.FragmentMovieFavBinding;
import com.example.movieapp.databinding.FragmentMovieSearchBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.view.MovieDetailsActivity;
import com.example.movieapp.view.MovieFavDetailsActivity;
import com.example.movieapp.view.adapters.MovieAdapter;
import com.example.movieapp.view.adapters.MovieFavAdapter;
import com.example.movieapp.viewmodel.MovieViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentMovieFav extends Fragment implements MovieClickListener {
    FragmentMovieFavBinding binding;
    private ArrayList<Movie> favMovieList;
    private MovieFavAdapter movieFavAdapter; // Changed to MovieFavAdapter
    
    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private CollectionReference userFavMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentMovieFavBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize current user
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize favorites collection reference
        userFavMovies = db.collection("users")
                        .document(currentUser.getUid())
                        .collection("FavMovies");

        favMovieList = new ArrayList<>();

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerViewFavorites.setLayoutManager(layoutManager);

        // Initialize adapter
        movieFavAdapter = new MovieFavAdapter(requireContext(), favMovieList);
        binding.recyclerViewFavorites.setAdapter(movieFavAdapter);
        movieFavAdapter.setClickListener(this);

        // Load data
        loadFavoriteMovies();
    }

    private void loadFavoriteMovies() {
        userFavMovies.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                favMovieList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Movie movie = document.toObject(Movie.class);
                    favMovieList.add(movie);
                }
                movieFavAdapter.notifyDataSetChanged();

                // Show empty state if no favorites
                if (favMovieList.isEmpty()) {
                    binding.recyclerViewFavorites.setVisibility(View.GONE);
                    // You might want to add a TextView for empty state
                    // binding.emptyStateText.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerViewFavorites.setVisibility(View.VISIBLE);
                    // binding.emptyStateText.setVisibility(View.GONE);
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(requireContext(), 
                    "Error loading favorites: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
    }

    @Override
    public void onClick(View v, int pos) {
        Movie selectedMovie = favMovieList.get(pos);
        Log.d("FragmentMovieFav", "Selected movie: " + selectedMovie.getTitle());
        
        Intent intent = new Intent(requireActivity(), MovieFavDetailsActivity.class);
        intent.putExtra("MOVIE", selectedMovie);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload favorites when returning to this fragment
        if (currentUser != null) {
            loadFavoriteMovies();
        }
    }
}