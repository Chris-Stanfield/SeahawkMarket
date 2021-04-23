package edu.uncw.seahawkmarket;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "$0.0f";
    public static final String USER = "user";

    //TODO: Add if statement so that is the current user is the poster, they can click a button to delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Log.d(TAG, "ItemDetailsActivity started.");

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get intent and item info from intent extras
        Intent intent = getIntent();
        String title = (String) intent.getExtras().get(TITLE);
        String description = (String) intent.getExtras().get(DESCRIPTION);
        String price = (String) intent.getExtras().get(String.valueOf(PRICE));
        String user = (String) intent.getExtras().get(USER);

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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