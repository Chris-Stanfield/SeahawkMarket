package edu.uncw.seahawkmarket;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;


class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {
    private final ArrayList<String> titles;
    private final ArrayList<String> descriptions;
    private final ArrayList<String> prices;
    private final ArrayList<String> users;
    private final ArrayList<Date> dates;
    private static final String TAG = "CaptionedImagesAdapter";
    private Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(CardView v) { //Each view holder will display a CardView
            super(v);
            cardView = v;
        }
    }

    public CaptionedImagesAdapter(ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> prices, ArrayList<String> users, ArrayList<Date> dates) { //This info is passed in mainActivity
        this.titles = titles;
        Log.d(TAG, "Size of titles in CaptionedImagesAdapter = " + titles.size());
        this.descriptions = descriptions;
        this.prices = prices;
        this.users = users;
        this.dates = dates;

    }

    @Override
    public int getItemCount() { //The number of data items
        Log.d(TAG, "Size of titles = " + titles.size());
        return titles.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).
                inflate((R.layout.posted_item), //Use the layout we created earlier for the card views
                        parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.itemDetailImage);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), R.drawable.default_cardview_image); //Display image in image view
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(titles.get(position));

        //Get access to all the views
        TextView titleTextView = cardView.findViewById(R.id.itemDetailTitle);
        TextView emailTextView = cardView.findViewById(R.id.itemDetailEmail);
        TextView priceTextView = cardView.findViewById(R.id.itemDetailPrice);
        TextView createdOnTextView = cardView.findViewById(R.id.itemDetailDatePosted);

        //Set the correct values for each view
        titleTextView.setText(titles.get(position)); //Populate the CardView's title view
        emailTextView.setText(users.get(position)); //Populate the CardView's email view
        priceTextView.setText(prices.get(position)); //Populate the CardView's price view
        createdOnTextView.setText(dates.get(position).toString()); //Populate the CardView's date view

        //Add the listener to the card view
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }
}
