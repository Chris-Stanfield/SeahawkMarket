package edu.uncw.seahawkmarket;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "$0.0f";
    public static final String USER = "user";

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
}