package edu.uncw.seahawkmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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
        mDb.collection("Items for sale").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"Item added for sale successfully");
                Toast.makeText(CreateListingActivity.this, "Item added for sell!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Item not added");
                Toast.makeText(CreateListingActivity.this, "Could not add item for sell.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}