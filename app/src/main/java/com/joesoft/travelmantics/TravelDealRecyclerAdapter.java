package com.joesoft.travelmantics;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TravelDealRecyclerAdapter extends RecyclerView.Adapter<TravelDealRecyclerAdapter.ViewHolder> {

    public static final String TAG = TravelDealRecyclerAdapter.class.getSimpleName();
    private final FirebaseDatabase mFirebaseDatabase;
    private final DatabaseReference mDatabaseReference;
    private  ArrayList<TravelDeal> mDeals;
    private final ChildEventListener mChildEventListener;

    public TravelDealRecyclerAdapter() {
        FirebaseUtil.openFirebaseReference("traveldeals");
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
//        View viewItem = LayoutInflater.from(context)
//                .inflate(R.layout.item_deal)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TravelDealRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_item_title);
        }
    }
}
