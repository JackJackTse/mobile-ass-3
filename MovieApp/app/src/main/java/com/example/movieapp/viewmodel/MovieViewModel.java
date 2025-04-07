package com.example.movieapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    // Attributes
    ArrayList<Movie> movieList;

    // list of movies
    private final MutableLiveData<ArrayList<Movie>> moviesLiveData  = new MutableLiveData<ArrayList<Movie>>();
    public LiveData<ArrayList<Movie>> getMovieData() {
        return moviesLiveData ;
    }

    // single movie details
    private final MutableLiveData<Movie> movieDetailsLiveData = new MutableLiveData<>();
    public LiveData<Movie> getMovieDetailsLiveData() { return movieDetailsLiveData; }

    // error message
    private final MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<String> getError() { return error; }


    // API Call Methods
    public void searchMovies(String query) {
        String getUrl = "https://www.omdbapi.com/?apikey=2bf62933&s="+query;

        ApiClient.get(getUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                error.postValue("Network error: " + e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() !=null;
                String responseData = response.body().string();


                JSONObject json = null;
                try {
                    json = new JSONObject(responseData);

                    // Handle Error
                    if (json.getString("Response").equals("False")) {
                        error.postValue(json.getString("Error"));
                        return;
                    }

                    JSONArray results = json.getJSONArray("Search");
                    movieList = new ArrayList<Movie>();

                    for(int i = 0; i < results.length(); i++) {
                        JSONObject item = results.getJSONObject(i);

                        // Storing data to a Movie model
                        movieList.add(new Movie(
                                item.getString("imdbID"),
                                item.getString("Poster"),
                                item.getString("Title"),
                                item.getString("Year")
                        ));
                    }

                    Log.d("Movie List Size: ", String.valueOf(movieList.size()));

                    // Save movie's details to LiveData
                    moviesLiveData.postValue(movieList);

                } catch (JSONException e) {
                    error.postValue("Invalid response format");
                }

            }
        });
    }
    public void fetchMovieDetail(String imdbID){
        String getUrl = "https://www.omdbapi.com/?apikey=2bf62933&i="+imdbID;

        ApiClient.get(getUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                error.postValue("Network error: " + e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    assert response.body() !=null;

                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        // Handle Error
                        if (json.getString("Response").equals("False")) {
                            error.postValue(json.getString("Error"));
                            return;
                        }

                        // Storing data to a Movie model
                        Movie movie = new Movie(
                                json.getString("imdbID"),
                                json.getString("Poster"),
                                json.getString("Title"),
                                json.getString("Year"),
                                json.getString("Director"),
                                json.getString("Genre"),
                                json.getString("Plot"),
                                json.getString("imdbRating"),
                                json.getString("Runtime")
                        );

                        // Save movie's details to LiveData
                        movieDetailsLiveData.postValue(movie);

                    } catch (JSONException e) {
                        error.postValue("Invalid response format");
                    }


                }


            }
        });
    }

}
