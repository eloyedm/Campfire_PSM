package com.example.android.campfire;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PlaceViewHolder> {
    List<SitesList.Place> places;

    RVAdapter(List<SitesList.Place> places){
        this.places = places;
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.site, viewGroup, false);
        PlaceViewHolder pvh = new PlaceViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {
        placeViewHolder.siteName.setText(places.get(i).name);
        placeViewHolder.siteState.setText(places.get(i).creator);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView siteName;
        TextView siteState;
        ImageView siteImage;

        PlaceViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.siteCard);
            siteName = itemView.findViewById(R.id.siteName);
            siteState = itemView.findViewById(R.id.siteState);
            siteImage = itemView.findViewById(R.id.siteImage);
        }
    }
}
