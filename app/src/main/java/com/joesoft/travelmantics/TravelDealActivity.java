 package com.joesoft.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

 public class TravelDealActivity extends AppCompatActivity {
     public static final String DEAL_POSITION = "com.joesoft.travelmantics.DEAL_POSITION";
     private FirebaseDatabase mFirebaseDatabase;
     private DatabaseReference mDatabaseReference;
     private EditText mTextTitle;
     private EditText mTextPrice;
     private EditText mTextDescription;
     private TravelDeal mDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        FirebaseUtil.openFirebaseReference("traveldeals", this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mTextTitle = findViewById(R.id.text_title);
        mTextPrice = findViewById(R.id.text_price);
        mTextDescription = findViewById(R.id.text_description);

        readDisplayStateValues();
    }

     private void readDisplayStateValues() {
         Intent intent = getIntent();
         TravelDeal deal = intent.getParcelableExtra(DEAL_POSITION);

         if (deal == null) {
             deal = new TravelDeal();
         }

         this.mDeal = deal;

         mTextTitle.setText(deal.getTitle());
         mTextDescription.setText(deal.getDescription());
         mTextPrice.setText(deal.getPrice());

     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.travel_deal_menu, menu);
         if (FirebaseUtil.mIsAdmin) {
             menu.findItem(R.id.action_save).setVisible(true);
             menu.findItem(R.id.action_delete).setVisible(true);
             enableEditText(true);
         } else {
             menu.findItem(R.id.action_save).setVisible(false);
             menu.findItem(R.id.action_delete).setVisible(false);
             enableEditText(false);
         }
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveDeal();
            Toast.makeText(getApplicationContext(), "Deal saved", Toast.LENGTH_LONG).show();
            return true;
        } else if(id == R.id.action_delete) {
            deleteDeal();
            Toast.makeText(getApplicationContext(), "Deal deleted", Toast.LENGTH_LONG).show();
            return true;
        }
         return super.onOptionsItemSelected(item);
     }

     private void saveDeal() {
//        String title = mTextTitle.getText().toString();
//        String price = mTextPrice.getText().toString();
//        String description = mTextDescription.getText().toString();

//         TravelDeal deal = new TravelDeal(title, price, description, "" );
//         //saving to the firebase db
//         mDatabaseReference.push().setValue(deal);
//         clean();
//         finish();

         mDeal.setTitle(mTextTitle.getText().toString());
         mDeal.setPrice(mTextPrice.getText().toString());
         mDeal.setDescription(mTextDescription.getText().toString());

        if (mDeal.getId() == null) {
            //saving into the firebase db
            mDatabaseReference.push().setValue(mDeal);
        } else {
            //updating into the firebase db
            mDatabaseReference.child(mDeal.getId()).setValue(mDeal);
        }

         clean();
         backToList();
     }

     private void clean() {
        mTextTitle.setText("");
        mTextPrice.setText("");
        mTextDescription.setText("");
        mTextTitle.requestFocus();
     }

     private void deleteDeal() {
        if (mDeal == null) {
            Toast.makeText(getApplicationContext(), "Please SAVE the deal before DELETING", Toast.LENGTH_LONG).show();
            return;
        }

        mDatabaseReference.child(mDeal.getId()).removeValue();
         backToList();
     }

     private void backToList() {
        startActivity(new Intent(getApplicationContext(), ListActivity.class));
     }

     private void enableEditText (boolean isEnabled) {
        mTextTitle.setEnabled(isEnabled);
        mTextDescription.setEnabled(isEnabled);
        mTextPrice.setEnabled(isEnabled);
     }
 }
