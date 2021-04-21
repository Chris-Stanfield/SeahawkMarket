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


class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> prices;
    private static final String TAG = "CaptionedImagesAdapter";
    private Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) { //Each view holder will display a CardView
            super(v);
            cardView = v;
        }
    }

    public CaptionedImagesAdapter(ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> prices) { //This info is passed in mainActivity
        this.titles = titles;
        Log.d(TAG, "Size of titles in CaptionedImagesAdapter = " + titles.size());
        this.descriptions = descriptions;
        this.prices = prices;
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
                inflate((R.layout.card_captioned_image), //Use the layout we created earlier for the card views
                        parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.itemDetailImage);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), R.drawable.default_cardview_image); //Display image in image view
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(titles.get(position));

        TextView textView = (TextView) cardView.findViewById(R.id.itemDetailTitle);
        textView.setText(titles.get(position)); //Populate the CardView's title view

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
