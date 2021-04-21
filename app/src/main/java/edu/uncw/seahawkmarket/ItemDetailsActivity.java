package edu.uncw.seahawkmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final Float PRICE = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Log.d(TAG, "ItemDetailsActivity started.");

        //Get intent and item info from intent extras
        Intent intent = getIntent();
        String title = (String) intent.getExtras().get(TITLE);
        String description = (String) intent.getExtras().get(DESCRIPTION);
        String price = (String) intent.getExtras().get(String.valueOf(PRICE));

        //Change the text in text views to reflect this item data
        TextView titleTextView = findViewById(R.id.itemTitle);
        TextView descriptionTextView = findViewById(R.id.itemDesciption);
        TextView priceTextView = findViewById(R.id.itemPrice);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priceTextView.setText(price);
    }
}