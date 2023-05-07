package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class MusicListActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private FirebaseUser user;

    private EditText title, url;
    private Button save, show;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        checkAuth();

        database = FirebaseFirestore.getInstance();

        getViewElements();
        setClickEvents();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Come back notification!", "Come back notification!", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sendNotification();
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Come back notification!")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Come back")
                .setContentText("Check out the new content here!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MusicListActivity.this);
        managerCompat.notify(1, builder.build());
    }

    private void getViewElements() {
        title = findViewById(R.id.editTextTitle);
        url = findViewById(R.id.editTextURL);
        save = findViewById(R.id.buttonSave);
        show = findViewById(R.id.button);
    }

    private void checkAuth() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "User logged in!");
        } else {
            Log.d(LOG_TAG, "No authenticated user!");
            finish();
        }
    }

    private void setClickEvents(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleValue = title.getText().toString();
                String urlValue = url.getText().toString();
                String id = UUID.randomUUID().toString();

                saveToFireStore(id, titleValue, urlValue);
            }
        });
    }

    private void saveToFireStore(String id, String title, String url) {
        if (!title.isEmpty() && !url.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("url", url);

            database.collection("Music").document(id).set(map).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MusicListActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MusicListActivity.this, "Failed!",Toast.LENGTH_SHORT).show();
                            }
                        });
        } else {
            Toast.makeText(this,"Empty fields not allowed", Toast.LENGTH_SHORT).show();
        }
    }
}