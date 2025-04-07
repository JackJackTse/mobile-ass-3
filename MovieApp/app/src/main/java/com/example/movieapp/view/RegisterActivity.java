package com.example.movieapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set click listener for register button
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        binding.textBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // This will close the RegisterActivity
            }
        });
    }

    private void registerUser() {
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            binding.editTextEmail.setError("Email is required");
            binding.editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.setError("Please enter a valid email");
            binding.editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.editTextPassword.setError("Password is required");
            binding.editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.editTextPassword.setError("Password should be at least 6 characters");
            binding.editTextPassword.requestFocus();
            return;
        }

        // Show loading state
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Creating Account...");

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(RegisterActivity.this,
                                    "Registration Successful", Toast.LENGTH_SHORT).show();


                            // Navigate to MainActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this,
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            // Reset button state
                            binding.btnRegister.setEnabled(true);
                            binding.btnRegister.setText("Create account");
                        }
                    }
                });
    }

}
