package edu.uncw.seahawkmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "$0.0f";
    public static final String USER = "user";
    public static String userEmail;
    public static String itemTitle;

    //TODO: Add if statement so that is the current user is the poster, they can click a button to delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Log.d(TAG, "ItemDetailsActivity started.");

        auth = FirebaseAuth.getInstance();

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get intent and item info from intent extras
        Intent intent = getIntent();
        String title = (String) intent.getExtras().get(TITLE);
        itemTitle = title;
        Log.d(TAG, "Item viewed = " + dB.collection("Items for sale").document(title));
        String description = (String) intent.getExtras().get(DESCRIPTION);
        String price = (String) intent.getExtras().get(String.valueOf(PRICE));
        String user = (String) intent.getExtras().get(USER);
        userEmail = user;
        Log.d(TAG, "User = " + user);

        //Change the text in text views to reflect this item data
        TextView titleTextView = findViewById(R.id.itemDetailTitle);
        TextView descriptionTextView = findViewById(R.id.itemDetailDescription);
        TextView priceTextView = findViewById(R.id.itemDetailPrice);
        TextView userTextView = findViewById(R.id.itemDetailEmail);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priceTextView.setText(price);
        userTextView.setText(user);
    }

    public void createListing(View view){
        Intent intent = new Intent(ItemDetailsActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

    //Set up menu and actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        //If the currentUser matches the user who posted the item, make the delete button visible
        Log.d("TAG", "Auth current user: " + auth.getCurrentUser().getEmail() + " User var: " + userEmail);
        if(auth.getCurrentUser().getEmail().equals(userEmail)){
            getMenuInflater().inflate(R.menu.menu_item_details, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //Code that runs when delete button is clicked
            case R.id.action_delete:
                deleteItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteItem(){ //Delete the item being viewed, return to Main Activity
        Log.d(TAG, "Delete button clicked");
        //Remove info from Fire store
        DocumentReference docRef = dB.collection("Items for sale").document(itemTitle);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document = " + dB.collection("Items for sale").document(itemTitle));
                Log.d(TAG, "Document deleted!");
                Toast.makeText(ItemDetailsActivity.this, "Item deleted!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Issue deleting document!");
                Toast.makeText(ItemDetailsActivity.this, "Issue deleting item!", Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}