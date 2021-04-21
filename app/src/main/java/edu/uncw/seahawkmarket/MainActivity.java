package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String userEmail = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //Get the intent and userEmail info
        Intent intent = getIntent();
        String email = (String) intent.getExtras().get(userEmail);

        //Modify the mainActivity welcome message
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        String welcomeText = "Hi, " + email;
        welcomeMessage.setText(welcomeText);

    }

    public void signOut(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
