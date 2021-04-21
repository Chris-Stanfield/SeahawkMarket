package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String userEmail = "";
    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();

        //Get reference to recycler view in main layout
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        //Create array lists with itemsForSale info from database


        final ArrayList<String> titles = new ArrayList<String>();
        final ArrayList<String> descriptions = new ArrayList<String>();
        final ArrayList<Float> prices = new ArrayList<Float>();
        Log.d(TAG, "Array Lists created");

        dB.collection("Items for sale").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                            //Create an item object with the document
                            ItemsForSale item = document.toObject(ItemsForSale.class);
                            //Append the item info to the appropriate array list
                            titles.add(item.getTitle());
                            descriptions.add(item.getDescription());
                            prices.add(item.getPrice());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failure iterating through database to get item info!");
                    }
                });

        //Pass newly made array lists to card adapter
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices);
        mainRecycler.setAdapter(adapter); //Link the adapter to the recycler
        Log.d(TAG, "Array Lists passed to adapter");

        //Set layout manager as grid and set mainRecycler
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRecycler.setLayoutManager(layoutManager);
        Log.d(TAG, "Layout manager set as grid, mainRecycler set");

        //Set the listener that class the appropriate activity when a card view is clicked in recycler
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

    public void createListing(View view){
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);

    }
}
