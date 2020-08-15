package com.joesoft.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TravelDealRecyclerAdapter extends RecyclerView.Adapter<TravelDealRecyclerAdapter.ViewHolder> {

    public static final String TAG = TravelDealRecyclerAdapter.class.getSimpleName();
    private final FirebaseDatabase mFirebaseDatabase;
    private final DatabaseReference mDatabaseReference;
    private  ArrayList<TravelDeal> mDeals;
    private final ChildEventListener mChildEventListener;

    public TravelDealRecyclerAdapter() {
//        FirebaseUtil.openFirebaseReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        mDeals = FirebaseUtil.mDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TravelDeal td = snapshot.getValue(TravelDeal.class);
                td.setId(snapshot.getKey());
                Log.d(TAG, "onChildAdded: " +  td.getTitle());
                mDeals.add(td);
                notifyItemInserted(mDeals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public TravelDealRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_deal, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelDealRecyclerAdapter.ViewHolder holder, int position) {
        TravelDeal deal = mDeals.get(position);
        holder.mTextTitle.setText(deal.getTitle());
        holder.mTextDescription.setText(deal.getDescription());
        holder.mTextPrice.setText(deal.getPrice());
        holder.showImage(deal.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextTitle;
        TextView mTextDescription;
        TextView mTextPrice;
        ImageView mImageDeal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_item_title);
            mTextDescription = itemView.findViewById(R.id.text_item_description);
            mTextPrice = itemView.findViewById(R.id.text_item_price);
            mImageDeal = itemView.findViewById(R.id.image_item_deal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: position = " + position);
            TravelDeal dealPosition = mDeals.get(position);
            Intent intent = new Intent(v.getContext(), TravelDealActivity.class);
            intent.putExtra(TravelDealActivity.DEAL_POSITION, dealPosition);
            v.getContext().startActivity(intent);
        }

        private void showImage(String url) {
            if (url != null && url.isEmpty() == false) {
                Picasso.get()
                        .load(url)
                        .resize(140, 140)
                        .centerCrop()
                        .into(mImageDeal);
            }

        }
    }
}
