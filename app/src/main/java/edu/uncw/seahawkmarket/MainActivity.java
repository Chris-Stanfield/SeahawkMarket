package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    //TODO: Order cards by timestamp

    public static final String userEmail = "";
    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> prices;
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
        prices = new ArrayList<String>();
        Log.d(TAG, "Array Lists created");

        //Pass the newly created arrays to the adapter made for the card views
        final CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices);
        mainRecycler.setAdapter(adapter); //Link the adapter to the recycler
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRecycler.setLayoutManager(layoutManager);

        //Set the listener that says what to do if a card is clicked
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(int position) {
                Log.d(TAG, "Card selected. Item = " + titles.get(position));
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.TITLE, titles.get(position));
                intent.putExtra(ItemDetailsActivity.DESCRIPTION, descriptions.get(position));
                intent.putExtra(ItemDetailsActivity.PRICE, prices.get(position));
                intent.putExtra(ItemDetailsActivity.USER, auth.getCurrentUser().getEmail());
                startActivity(intent);
            }
        });
        dB.collection("Items for sale").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "Successfully accessed collection!");
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots){
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
    public void signOut(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void createListing(View view){
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //Code to run when the about item is clicked
            case R.id.action_profile:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
