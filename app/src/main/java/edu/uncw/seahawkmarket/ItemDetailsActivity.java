package edu.uncw.seahawkmarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailsActivity";
    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String COLLECTION = "Items for sale";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "$0.0f";
    public static final String EMAIL = "email";
    public static String userEmail;
    public static String itemTitle;
    private String imageFile;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        auth = FirebaseAuth.getInstance();

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get intent and item info from intent extras
        Intent intent = getIntent();
        String title = (String) intent.getExtras().get(TITLE);
        itemTitle = title;
        String description = (String) intent.getExtras().get(DESCRIPTION);
        String price = (String) intent.getExtras().get(String.valueOf(PRICE));
        String user = (String) intent.getExtras().get(EMAIL);
        userEmail = user;


        //Change the text in text views to reflect this item data
        TextView titleTextView = findViewById(R.id.itemDetailTitle);
        TextView descriptionTextView = findViewById(R.id.itemDetailDescription);
        TextView priceTextView = findViewById(R.id.itemDetailPrice);
        TextView userTextView = findViewById(R.id.itemDetailEmail);
        ImageButton contactSellerButton = findViewById(R.id.contactSellerButton);

        //Make contactSellerButton visible if current user is not poster
        if(auth.getCurrentUser().getEmail().equals(userEmail)){
            contactSellerButton.setVisibility(View.GONE);
        }

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        priceTextView.setText(price);
        userTextView.setText(user);

        final ImageView itemDetailImage = findViewById(R.id.itemDetailImage);
        storage = FirebaseStorage.getInstance();
        DocumentReference docRef = dB.collection(COLLECTION).document(title);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(imageFile!=null) {
                            imageFile = document.get("imageFile").toString();
                            StorageReference gsReference = storage.getReferenceFromUrl("gs://seahawk-market.appspot.com/images/" + imageFile);
                            gsReference.getBytes(1024 * 1024 * 10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    itemDetailImage.setImageBitmap(bitmap);
                                }

                            });
                        }
                    } else {
                        itemDetailImage.setImageDrawable(getResources().getDrawable(R.drawable.default_cardview_image));
                    }
                }

            }
        });



        System.out.println("Image file... ===  " + imageFile);


    }

    public void createListing(View view) {
        Intent intent = new Intent(ItemDetailsActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

    //Set up menu and actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        //If the currentUser matches the user who posted the item, make the delete button visible
        if (auth.getCurrentUser().getEmail().equals(userEmail)) {
            getMenuInflater().inflate(R.menu.menu_item_details, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Code that runs when delete button is clicked
            case R.id.action_delete:
                deleteItem();
                return true;

            case R.id.action_edit:
                editItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteItem() { //Delete the item being viewed, return to Main Activity
        //Remove info from Fire store
        DocumentReference docRef = dB.collection(COLLECTION).document(itemTitle);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ItemDetailsActivity.this, "Item deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ItemDetailsActivity.this, "Issue deleting item!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editItem() { //Edit the item being viewed
        DocumentReference docRef = dB.collection(COLLECTION).document(itemTitle);
        Intent intent1 = getIntent();
        Intent intent = new Intent(ItemDetailsActivity.this, EditActivity.class);
        intent.putExtra(TITLE, itemTitle);
        String description = (String) intent1.getExtras().get(DESCRIPTION);
        intent.putExtra(DESCRIPTION, description);
        String price = (String) intent1.getExtras().get(PRICE);
        System.out.println("itemTitle = " + itemTitle + "  description = " + description);
        intent.putExtra(PRICE, price);

        startActivity(intent);



    }

    public void messageSeller(View view) {      // method to email seller
        Intent intent = new Intent(Intent.ACTION_SEND);
        String to[] = {userEmail};
        intent.putExtra(Intent.EXTRA_EMAIL, to);   //  puts the email of the seller in the To:
        String example_email = "Hi! I would like to purchase '";
        intent.putExtra(Intent.EXTRA_TEXT,  example_email + itemTitle + "'");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client"));
    }
}