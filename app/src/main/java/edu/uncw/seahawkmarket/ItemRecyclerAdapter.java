package edu.uncw.seahawkmarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ItemRecyclerAdapter extends FirestoreRecyclerAdapter<ItemForSale, ItemRecyclerAdapter.ItemViewHolder> {
    private String TAG = "ItemRecyclerAdapter";
    private FirebaseStorage storage;
    private FirebaseFirestore dB = FirebaseFirestore.getInstance();
    private String imageFile;
    private static final String COLLECTION = "Items for sale";

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private final OnItemClickListener listener;

    ItemRecyclerAdapter(FirestoreRecyclerOptions<ItemForSale> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    ItemRecyclerAdapter(FirestoreRecyclerOptions<ItemForSale> options) {
        super(options);
        this.listener = null;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final CardView view;
        final ImageView itemImage;
        final TextView itemEmail;
        final TextView itemTitle;
        final TextView itemPrice;
        final TextView itemDatePosted;

        ItemViewHolder(CardView v) {
            super(v);
            view = v;
            itemImage = v.findViewById(R.id.itemDetailImage);
            itemEmail = v.findViewById(R.id.itemDetailEmail);
            itemTitle = v.findViewById(R.id.itemDetailTitle);
            itemPrice = v.findViewById(R.id.itemDetailPrice);
            itemDatePosted = v.findViewById(R.id.itemDetailDatePosted);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, @NonNull int position, @NonNull final ItemForSale item) {
        Log.d(TAG, "Item = " + item.getTitle());
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://seahawk-market.appspot.com/images/" + item.getImageFile());
        gsReference.getBytes(1024*1024*5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.itemImage.setImageBitmap(bitmap);
            }
        });

        holder.itemEmail.setText(item.getEmail());
        holder.itemTitle.setText(item.getTitle());
        holder.itemPrice.setText("$" + item.getPrice());
        holder.itemDatePosted.setText(holder.view.getContext()
                .getString(R.string.posted_on, format.format(item.getDatePosted())));
        if (listener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posted_item_card, parent, false);
        return new ItemViewHolder(v);
    }
}