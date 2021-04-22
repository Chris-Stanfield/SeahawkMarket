package edu.uncw.seahawkmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> prices;
    private ArrayList<String> users;

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

        //Get reference to recycler view in main layout
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        //Create array lists with itemsForSale info from database, get data from database
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        prices = new ArrayList<String>();
        users = new ArrayList<String>();
        Log.d(TAG, "Array Lists created");

        //Pass the newly created arrays to the adapter made for the card views
        final CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices, users);
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
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                            //Create an item object with the document
                            ItemsForSale item = document.toObject(ItemsForSale.class);
                            Log.d(TAG, "Document item = " + item);

                            //Compare the email in the doc item to the current user email
                            if(item.getUser() == auth.getCurrentUser().getEmail()){
                                //Add the item info to the appropriate array list
                                titles.add(item.getTitle());
                                Log.d(TAG, "Item title: " + item.getTitle() + " added");
                                descriptions.add(item.getDescription());
                                prices.add(item.getPrice());
                                users.add(item.getUser());
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


}