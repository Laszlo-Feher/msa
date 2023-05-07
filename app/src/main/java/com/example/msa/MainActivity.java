package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
    }

    public void login(View view) {
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    message = "User logged in!";
                    openMusicList();
                } else {
                    String message = "An error occured during the login process! ";
                    Toast.makeText(MainActivity.this, message + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openMusicList() {
        Intent intent = new Intent(this, MusicListActivity.class);
        startActivity(intent);
    }

    public void openRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}