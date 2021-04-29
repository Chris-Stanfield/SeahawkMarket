package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    //TODO: Order cards by timestamp

    public static final String userEmail = "";
    private FirebaseAuth auth;
    private final FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private static final String COLLECTION = "Items for sale";
    private ItemRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        //This keeps the keyboard from pushing the layout up when it appears. Not sure why its needed in this activity..?
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        RecyclerView recyclerView = findViewById(R.id.mainRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Query query = dB.collection(COLLECTION)
                .orderBy("datePosted", Query.Direction.ASCENDING);
        final FirestoreRecyclerOptions<ItemForSale> options = new FirestoreRecyclerOptions.Builder<ItemForSale>()
                .setQuery(query, ItemForSale.class)
                .build();

        adapter = new ItemRecyclerAdapter(options, new ItemRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);

                //Get reference to the item clicked. put extra with intent, based on data from reference
                ItemForSale clickedItem = adapter.getSnapshots().getSnapshot(position).toObject(ItemForSale.class);
                intent.putExtra(ItemDetailsActivity.TITLE, clickedItem.getTitle());
                intent.putExtra(ItemDetailsActivity.DESCRIPTION, clickedItem.getDescription());
                intent.putExtra(ItemDetailsActivity.PRICE, clickedItem.getPrice());
                intent.putExtra(ItemDetailsActivity.USER, clickedItem.getUser());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        //Set up search box textChangedListener
        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            //First 2 can be left blank
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //This is the one important for my search bar.
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "Search box has changed to: " + s.toString());
                //Set up the query to search for items
                Query query = dB.collection(COLLECTION)
                        .whereEqualTo("title", s.toString())
                        .orderBy("datePosted", Query.Direction.ASCENDING);

                FirestoreRecyclerOptions<ItemForSale> options = new FirestoreRecyclerOptions.Builder<ItemForSale>()
                        .setQuery(query, ItemForSale.class)
                        .build();
                adapter.updateOptions(options);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public void createListing(View view) {
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Code to run when the about item is clicked
            case R.id.action_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
