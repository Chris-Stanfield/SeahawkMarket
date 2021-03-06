package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth auth;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.enterEmail);
        passwordField = findViewById(R.id.enterPassword);
        emailField.setText("");
        passwordField.setText("");

        //Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    //On start, make sure the user is logged out and the login fields are reset to empty
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        emailField.getText().clear();
        passwordField.getText().clear();
    }

    //Check that email and password are valid
    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    public void signIn(View view) {
        if (!validateForm()) {
            return;
        }

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, launch main activity with intent and send user email
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(MainActivity.userEmail, user.getEmail());
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(LoginActivity.this, "Login failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void createAccount(View view) {
        if (!validateForm()) {
            return;
        }

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, start main activity with user email
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(MainActivity.userEmail, user.getEmail());
                            startActivity(intent);

                        } else {
                            // If registration fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(LoginActivity.this, "Registration failed: " + e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void createListing(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateListingActivity.class);
        startActivity(intent);

    }

}