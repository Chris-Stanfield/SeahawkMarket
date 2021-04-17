package edu.uncw.seahawkmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class CreateListingActivity extends AppCompatActivity {
    private static final String TAG = "CreateListingActivity";
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);
    }

    public void listItem(View view){
        EditText itemName = findViewById(R.id.ItemName);
        EditText itemDescription = findViewById(R.id.itemDesciption);
        EditText itemPrice = findViewById(R.id.itemPrice);

        String title = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        String priceString = itemPrice.getText().toString();
        Float price = Float.parseFloat(priceString);

        ItemsForSale item = new ItemsForSale(title, description, price);
        Log.d(TAG, "\nListed item: " + " \n Name of item: " + item.getTitle() + "\n Description: " + item.getDescription() + "\n price: " + item.getPrice());
        mDb.collection("Items for sale").add(item);

    }
}