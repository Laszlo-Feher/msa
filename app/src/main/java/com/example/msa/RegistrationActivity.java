package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();

    EditText username;
    EditText email;
    EditText password;
    EditText passwordConfirm;
    String message = "";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        passwordConfirm = findViewById(R.id.editTextPasswordConfirm);
    }

    public void register(View view) {
        String usernameValue = username.getText().toString();
        String emailValue = email.getText().toString();
        String passwordValue = password.getText().toString();
        String passwordConfirmValue = passwordConfirm.getText().toString();

        if (!usernameValue.isEmpty() && !emailValue.isEmpty() && !passwordValue.isEmpty() && !passwordConfirmValue.isEmpty() && passwordValue.equals(passwordConfirmValue))
        firebaseAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    message = "User created!";
                    resetInputFields();
                    openMusicList();
                } else {
                    String message = "An error occured during registration! ";
                    Toast.makeText(RegistrationActivity.this, message + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

                Log.d(LOG_TAG, message);
             }
        });
    }

    private void resetInputFields() {
        username.setText("");
        email.setText("");
        password.setText("");
        passwordConfirm.setText("");
    }

    public void openLogin(View view) {
        finish();
    }

    public void openMusicList() {
        Intent intent = new Intent(this, MusicListActivity.class);
        startActivity(intent);
    }
}