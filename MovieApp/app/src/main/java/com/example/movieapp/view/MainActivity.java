package com.example.movieapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.view.adapters.MainFragmentAdapter;
import com.example.movieapp.view.fragments.FragmentMovieFav;
import com.example.movieapp.view.fragments.FragmentMovieSearch;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initial ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fragments Create
        fragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(new FragmentMovieSearch());
        fragmentAdapter.addFragment(new FragmentMovieFav());

        // View Pager
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(fragmentAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Search");
                            break;
                        case 1:
                            tab.setText("Favorites");
                            break;
                    }
                }
        ).attach();
    }
}