package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateListingActivity extends AppCompatActivity {
    private static final String TAG = "CreateListingActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private String currentUserEmail;

    //TODO: Recycler needs to update after an item is added.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        auth = FirebaseAuth.getInstance();
        currentUserEmail = auth.getCurrentUser().getEmail();

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void listItem(View view) {
        final EditText itemName = findViewById(R.id.ItemName);
        final EditText itemDescription = findViewById(R.id.itemDesciption);
        final EditText itemPrice = findViewById(R.id.itemDetailPrice);

        String title = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        String price = itemPrice.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();

        if(!title.isEmpty()&&!description.isEmpty()&&!price.isEmpty()&&user!=null) {       // You must put a title, description, and price. You must also be signed in.
            final ItemsForSale item = new ItemsForSale(title, description, price, user);
            Log.d(TAG, "\nListed item: " + " \n Name of item: " + item.getTitle() + "\n Description: " + item.getDescription() + "\n price: " + item.getPrice() +"\n User: " + item.getUser());
            mDb.collection("Items for sale").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "Item added for sale successfully");
                    Toast.makeText(CreateListingActivity.this, "Item added for sell!", Toast.LENGTH_SHORT).show();

                    //Reset the editTexts to blank
                    itemName.setText("");
                    itemDescription.setText("");
                    itemPrice.setText("");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Item not added");
                            Toast.makeText(CreateListingActivity.this, "Could not add item for sell.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            if(title.isEmpty()&&description.isEmpty()&&price.isEmpty()) {      // conditional statements for detailed user feedback on why item was not added for sell.
                Toast.makeText(CreateListingActivity.this, "Missing information for Name, Description, and Price", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty() && description.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Name and Description", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty() && !price.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Name", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Name and Price", Toast.LENGTH_SHORT).show();
            }  else if (description.isEmpty() && price.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Description and Price", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Description", Toast.LENGTH_SHORT).show();
            } else if (price.isEmpty()){
                Toast.makeText(CreateListingActivity.this, "Missing information for Price", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateListingActivity.this, "You must login to list an item", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Set up menu actions
    public void createListing(View view){
        Intent intent = new Intent(CreateListingActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

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