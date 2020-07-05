package com.joesoft.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    public static final String TAG = ListActivity.class.getSimpleName();
    private ArrayList<TravelDeal> mDeals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private RecyclerView mRecyclerDeals;
    private LinearLayoutManager mDealsLayoutManager;
    private TravelDealRecyclerAdapter mDealRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");
//        using FirebaseUtil.class
//        FirebaseUtil.openFirebaseReference("traveldeals");
//        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
//        mDatabaseReference = FirebaseUtil.mDatabaseReference;
//
//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                TextView textDeals = findViewById(R.id.text_deals);
//                TravelDeal td = snapshot.getValue(TravelDeal.class);
////                textDeals.setText(textDeals.getText() + "\n" + td.getTitle());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        mDatabaseReference.addChildEventListener(mChildEventListener);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TravelDealActivity.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtil.openFirebaseReference("traveldeals", this);
        mRecyclerDeals = findViewById(R.id.recycler_deals);
        mDealsLayoutManager = new LinearLayoutManager(this);
        mRecyclerDeals.setLayoutManager(mDealsLayoutManager);

        mDealRecyclerAdapter = new TravelDealRecyclerAdapter();
        mRecyclerDeals.setAdapter(mDealRecyclerAdapter);

        FirebaseUtil.attachListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Log.d(TAG, "onComplete: Logout successful");
                            FirebaseUtil.attachListener();
                        }
                    });
            FirebaseUtil.detachListener();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
