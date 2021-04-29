package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> prices;
    private ArrayList<String> users;
    private ArrayList<Date> dates;

    //TODO: Add look out menu option in this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        //Set email in text view
        TextView emailTextView = findViewById(R.id.profileEmail);
        emailTextView.setText(auth.getCurrentUser().getEmail());

        //Get reference to recycler view in main layout
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.mainRecycler);

        //Create array lists with itemsForSale info from database, get data from database
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        prices = new ArrayList<String>();
        users = new ArrayList<String>();
        dates = new ArrayList<Date>();
        Log.d(TAG, "Array Lists created");

        //Pass the newly created arrays to the adapter made for the card views
        final CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices, users, dates);
        mainRecycler.setAdapter(adapter); //Link the adapter to the recycler
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRecycler.setLayoutManager(layoutManager);

        //Set the listener that says what to do if a card is clicked
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(int position) {
                Log.d(TAG, "Card selected. Item = " + titles.get(position));
                Intent intent = new Intent(ProfileActivity.this, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.TITLE, titles.get(position));
                intent.putExtra(ItemDetailsActivity.DESCRIPTION, descriptions.get(position));
                intent.putExtra(ItemDetailsActivity.PRICE, prices.get(position));
                intent.putExtra(ItemDetailsActivity.USER, users.get(position));
                startActivity(intent);
            }
        });

        //Get data from database specific to current user
        dB.collection("Items for sale").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "Successfully accessed collection!");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            //Create an item object with the document
                            ItemForSale item = document.toObject(ItemForSale.class);
                            Log.d(TAG, "Document item = " + item);
                            Log.d(TAG, "Item email = " + item.getUser() + ", current user email = " + auth.getCurrentUser().getEmail());

                            //Compare the email in the doc item to the current user email
                            if (item.getUser().replaceAll("\n", "").equals(auth.getCurrentUser().getEmail().replaceAll("\n", ""))) {
                                Log.d(TAG, "Item email and current user email matched!");
                                //Add the item info to the appropriate array list
                                titles.add(item.getTitle());
                                descriptions.add(item.getDescription());
                                prices.add(item.getPrice());
                                users.add(item.getUser());
                                dates.add(item.getDatePosted());
                                Log.d(TAG, "Item title: " + item.getTitle() + " added");
                            }
                        }
                        Log.d(TAG, "Size of titles array list = " + titles.size());
                        Log.d(TAG, "Size of descriptions array list = " + descriptions.size());
                        Log.d(TAG, "Size of prices array list = " + prices.size());
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failure iterating through database to get item info!");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Code to run when the about item is clicked
            case R.id.action_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}