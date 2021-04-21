package edu.uncw.seahawkmarket;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.View;

import java.util.ArrayList;


class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>{
    //Hold the captions and image resource id's
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<Float> prices;
    private Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v) { //Each view holder will display a CardView
            super(v);
            cardView = v;
        }
    }

    public CaptionedImagesAdapter (ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<Float> prices){ //This info is passed in mainActivity in constructor
        this.titles = titles;
        this.descriptions = descriptions;
        this.prices = prices;
    }

    @Override
    public int getItemCount(){ //The number of data items
        return titles.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).
                inflate((R.layout.card_captioned_image), //Use the layout we created earlier for the card views
                        parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cardView = holder.cardView;
        //Use to display image of item. Not implemented yet.
//      ImageView imageView = (ImageView)cardView.findViewById(R.id.info_image);
//      Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imageIds[position]); //Display image in image view
//      imageView.setImageDrawable(drawable);
//      imageView.setContentDescription(captions[position]);

        TextView textView = (TextView)cardView.findViewById(R.id.itemTitle);
        textView.setText(titles.get(position)); //Populate the CardView's title view

        //Add the listener to the card view
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });


    }
}
