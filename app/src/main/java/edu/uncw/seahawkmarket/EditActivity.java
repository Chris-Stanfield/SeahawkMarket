package edu.uncw.seahawkmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "$0.0f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText titleEdit = findViewById(R.id.ItemName);
        EditText descriptionEdit = findViewById(R.id.itemDesciption);
        EditText priceEdit = findViewById(R.id.itemDetailPrice);
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        titleEdit.setText(title);
        String description = intent.getStringExtra(DESCRIPTION);
        descriptionEdit.setText(description);
        String price = intent.getStringExtra(PRICE);
        priceEdit.setText(price);

    }

    public void updateItem(View view){
        final EditText itemName = findViewById(R.id.ItemName);
        final EditText itemDescription = findViewById(R.id.itemDesciption);
        final EditText itemPrice = findViewById(R.id.itemDetailPrice);

        String title = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        String price = itemPrice.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();

        if (!title.isEmpty() && !description.isEmpty() && !price.isEmpty() && user != null && !price.equals(".")) {       // You must put a title, description, and price. You must also be signed in.
            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Items for sale").document(title);
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("description", description);
            map.put("price", price);

            docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "updated document");
                    Toast.makeText(EditActivity.this, "Updated Document!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Unable to update document");
                    Toast.makeText(EditActivity.this, "Unable to update document", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (title.isEmpty() && description.isEmpty() && price.isEmpty()) {      // conditional statements for detailed user feedback on why item was not added for sell.
                Toast.makeText(EditActivity.this, "Missing information for Name, Description, and Price", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty() && description.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Name and Description", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty() && !price.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Name", Toast.LENGTH_SHORT).show();
            } else if (title.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Name and Price", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty() && price.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Description and Price", Toast.LENGTH_SHORT).show();
            } else if (description.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Description", Toast.LENGTH_SHORT).show();
            } else if (price.isEmpty()) {
                Toast.makeText(EditActivity.this, "Missing information for Price", Toast.LENGTH_SHORT).show();
            } else if (price.equals(".")) {
                Toast.makeText(EditActivity.this, "enter valid price \nprice must contain a number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditActivity.this, "You must login to list an item", Toast.LENGTH_SHORT).show();
            }
        }
    }

}