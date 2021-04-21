package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String userEmail = "";
    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<Float> prices;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //Get reference to recycler view in main layout
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        //Create array lists with itemsForSale info from database, get data from database
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        prices = new ArrayList<Float>();
        Log.d(TAG, "Array Lists created");

        dB.collection("Items for sale").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "Successfully accessed collection!");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            //Create an item object with the document
                            ItemsForSale item = document.toObject(ItemsForSale.class);
                            Log.d(TAG, "Document item = " + item);
                            //Add the item info to the appropriate array list
                            titles.add(item.getTitle());
                            Log.d(TAG, "Item title: " + item.getTitle() + " added");
                            descriptions.add(item.getDescription());
                            prices.add(item.getPrice());
                        }
                        Log.d(TAG, "Size of titles array list = " + titles.size());
                        Log.d(TAG, "Size of descriptions array list = " + descriptions.size());
                        Log.d(TAG, "Size of prices array list = " + prices.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failure iterating through database to get item info!");
                    }
                });

        //Pass the newly created arrays to the adapter made for the card views
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices);
        mainRecycler.setAdapter(adapter); //Link the adapter to the recycler

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, testActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signOut(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void createListing(View view) {
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }
}
