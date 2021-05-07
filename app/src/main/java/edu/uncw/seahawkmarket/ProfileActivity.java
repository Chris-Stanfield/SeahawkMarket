package edu.uncw.seahawkmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> prices;
    private ArrayList<String> users;
    private ArrayList<String> imageFiles;
    private ArrayList<Date> dates;
    private String profileImageFile;
    public Uri imageUri;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ImageView profileImage;
    private FirebaseStorage storage;
    private StorageReference riversRef;
    private StorageReference storageReference;

    //TODO: Add look out menu option in this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Set up toolbar and enable up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        // Handles Image Profile pictures
        profileImage = findViewById(R.id.profileImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DocumentReference docRef = dB.collection("Users").document(auth.getCurrentUser().getEmail());  // Gets the pofile image name out of collection and document for current user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {     // A custom profile image only loads if the user has created one. Other wise a default image
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        profileImageFile = document.get("profileImageFile").toString();      // profileImageFile = the name of the profile image file
                        StorageReference gsReference = storage.getReferenceFromUrl("gs://seahawk-market.appspot.com/images/" + profileImageFile);  // Gets the fu
                        gsReference.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                profileImage.setImageBitmap(bitmap);
                            }
                        });
                    } else {
                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.default_cardview_image));
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        //Set email in text view
        TextView emailTextView = findViewById(R.id.profileEmail);
        emailTextView.setText(auth.getCurrentUser().getEmail());

        //Get reference to recycler view in main layout
        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.mainRecycler);

        //Create array lists with itemsForSale info from database, get data from database
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        prices = new ArrayList<String>();
        users = new ArrayList<String>();
        dates = new ArrayList<Date>();
        imageFiles = new ArrayList<String>();
        Log.d(TAG, "Array Lists created");

        //Pass the newly created arrays to the adapter made for the card views
        final CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(titles, descriptions, prices, users, dates, imageFiles);
        mainRecycler.setAdapter(adapter); //Link the adapter to the recycler
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainRecycler.setLayoutManager(layoutManager);

        //Set the listener that says what to do if a card is clicked
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(int position) {
                Log.d(TAG, "Card selected. Item = " + titles.get(position));
                Intent intent = new Intent(ProfileActivity.this, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.TITLE, titles.get(position));
                intent.putExtra(ItemDetailsActivity.DESCRIPTION, descriptions.get(position));
                intent.putExtra(ItemDetailsActivity.PRICE, prices.get(position));
                intent.putExtra(ItemDetailsActivity.EMAIL, users.get(position));
                startActivity(intent);
            }
        });

        //Get data from database specific to current user
        dB.collection("Items for sale").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "Successfully accessed collection!");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            //Create an item object with the document
                            ItemForSale item = document.toObject(ItemForSale.class);
                            Log.d(TAG, "Document item = " + item);
                            Log.d(TAG, "Item email = " + item.getEmail() + ", current user email = " + auth.getCurrentUser().getEmail());

                            //Compare the email in the doc item to the current user email
                            if (item.getEmail().equals(auth.getCurrentUser().getEmail())) {
                                Log.d(TAG, "Item email and current user email matched!");
                                //Add the item info to the appropriate array list
                                titles.add(item.getTitle());
                                descriptions.add(item.getDescription());
                                prices.add(item.getPrice());
                                users.add(item.getEmail());
                                dates.add(item.getDatePosted());
                                imageFiles.add(item.getImageFile());

                                Log.d(TAG, "Item title: " + item.getTitle() + " added");
                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Code to run when the about item is clicked
            case R.id.action_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addProfileImage(View view){
        Intent img = new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(img, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadPicture();
        }
    }

    // uploads profile picture to the database
    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        riversRef = storageReference.child("images/" + randomKey);
        Users userName = new Users(riversRef.getName());  // create a new document for a specific user
        String userEmail = auth.getCurrentUser().getEmail();
        mDb.collection("Users").document(userEmail).set(userName);  // collection Users. Document of users email that has a profileImageFile field that we can obtain later. We know what the profile image filename is for the user.
        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Unable to upload image.", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
    }




}