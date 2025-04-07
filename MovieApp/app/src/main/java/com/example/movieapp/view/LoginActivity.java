package com.example.movieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.databinding.ActivityLoginBinding;
import com.example.movieapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for the register link
        binding.textGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for login button
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        // Set click listener for the register link
        binding.textGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    // Login logic
    private void loginUser() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            binding.editTextEmail.setError("Email is required");
            binding.editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.editTextPassword.setError("Password is required");
            binding.editTextPassword.requestFocus();
            return;
        }

        // Show loading state (you might want to add a progress bar)
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        // Authenticate with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            // Navigate to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a Toast message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            // Reset button state
                            binding.btnLogin.setEnabled(true);
                            binding.btnLogin.setText("Log in");
                        }
                    }
                });
    }



}
