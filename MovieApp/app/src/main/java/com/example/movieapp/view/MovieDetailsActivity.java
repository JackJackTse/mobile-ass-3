package com.example.movieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.databinding.ActivityMovieDetailsBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.viewmodel.MovieViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    private MovieViewModel movieViewModel;

    private String _imdbId;
    private String _poster;
    private String _title;
    private String _year;
    private String _director;
    private String _genre;
    private String _plot;
    private String _imdbRating;
    private String _runtime;


    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userFavMovies;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize mAuth here
    private FirebaseUser currentUser;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Initialize Current User
        currentUser = mAuth.getCurrentUser();

        // Get Movie Object data from MainActivity
        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE");
        if (movie == null) {
            Toast.makeText(this, "Error loading movie", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch details using IMDb ID
        _imdbId = movie.getImdbID();
        movieViewModel.fetchMovieDetail(_imdbId);


        // Movie Details Data Observer
        movieViewModel.getMovieDetailsLiveData().observe(this, movieDetails -> {
            if (movieDetails != null) {
                _poster = movieDetails.getPoster();
                _title = movieDetails.getTitle();
                _year = movieDetails.getYear();
                _director = movieDetails.getDirector();
                _genre = movieDetails.getGenre();
                _plot = movieDetails.getDescription();
                _imdbRating = movieDetails.getImdbRating();
                _runtime = movieDetails.getRuntime();

                binding.textMovieTitle.setText(_title);
                binding.textMovieDetails.setText(_year + " | " + _runtime + " | " + _genre + " | " + "★ " + _imdbRating + " Ratings");
                binding.textMovieDirector.setText("Director - "+ _director);
                binding.textMovieDescription.setText(_plot);
                Glide.with(this).load(_poster).into(binding.imageMoviePoster);

            }
        });

        // Observe errors
        movieViewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());


        binding.btnAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataToNewDocument(_imdbId,_poster, _title, _year, _director, _genre, _plot, _imdbRating, _runtime);
            }
        });

        // Set up Back button listener
        binding.btnBack.setOnClickListener(view -> finish());
    }



    private void SaveDataToNewDocument(String imdbId, String poster, String title, String year,
                                       String director, String genre, String plot, String imdbRating, String runtime) {

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        userFavMovies = db.collection("users").document(userId).collection("FavMovies");

        Movie movieFav = new Movie(imdbId, poster, title, year, director, genre, plot, imdbRating, runtime);

        userFavMovies.add(movieFav)
                .addOnSuccessListener(documentReference -> {
                    String docId = documentReference.getId();
                    Log.d("tag", "DocumentSnapshot added with ID: " + docId);
                    Toast.makeText(MovieDetailsActivity.this,
                            "Movie added to favorites", Toast.LENGTH_SHORT).show();

                    // Create intent to return to MainActivity
                    Intent intent = new Intent(MovieDetailsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("tag", "Error adding document", e);
                    Toast.makeText(MovieDetailsActivity.this,
                            "Failed to add movie", Toast.LENGTH_SHORT).show();
                });
    }



}
