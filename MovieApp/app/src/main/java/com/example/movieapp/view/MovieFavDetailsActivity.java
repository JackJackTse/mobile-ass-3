package com.example.movieapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movieapp.databinding.ActivityMovieFavDetailsBinding;
import com.example.movieapp.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MovieFavDetailsActivity extends AppCompatActivity {
    private ActivityMovieFavDetailsBinding binding;
    private Movie movie;
    
    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private CollectionReference userFavMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMovieFavDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get current user
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firestore reference
        userFavMovies = db.collection("users")
                .document(currentUser.getUid())
                .collection("FavMovies");

        // Get Movie object from intent
        movie = (Movie) getIntent().getSerializableExtra("MOVIE");
        Log.d("MovieFavDetailsActivity", "Received movie: " + (movie != null ? movie.getTitle() : "null"));

        if (movie == null) {
            Toast.makeText(this, "Error loading movie details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display movie details
        displayMovieDetails();

        // Set click listeners
        setupClickListeners();
    }

    private void displayMovieDetails() {
        binding.textMovieTitle.setText(movie.getTitle());
        binding.textMovieDetails.setText(
            String.format("%s | %s | %s | ★ %s Ratings",
                movie.getYear(),
                movie.getGenre(),
                movie.getRuntime(),
                movie.getImdbRating())
        );
        binding.textMovieDirector.setText("Director - " + movie.getDirector());
        binding.editTextMovieDescription.setText(movie.getDescription());
        
        // Load movie poster
        Glide.with(this)
             .load(movie.getPoster())
             .into(binding.imageMoviePoster);
    }

    private void setupClickListeners() {
        // Update button
        binding.btnUpdate.setOnClickListener(v -> updateMovie());

        // Delete button
        binding.btnDelete.setOnClickListener(v -> deleteMovie());

        // Back button
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void updateMovie() {
        String newDescription = binding.editTextMovieDescription.getText().toString();
        
        // Query to find the movie document
        userFavMovies.whereEqualTo("imdbID", movie.getImdbID())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Get the first document (should be only one)
                    String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    
                    // Update the description
                    userFavMovies.document(docId)
                        .update("description", newDescription)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Movie updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to update movie: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        });
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error finding movie: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
    }

    private void deleteMovie() {
        // Query to find the movie document
        userFavMovies.whereEqualTo("imdbID", movie.getImdbID())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Get the first document (should be only one)
                    String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    
                    // Delete the document
                    userFavMovies.document(docId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Movie deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to delete movie: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        });
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error finding movie: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
    }
}