 package com.joesoft.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

 public class InsertActivity extends AppCompatActivity {
     private FirebaseDatabase mFirebaseDatabase;
     private DatabaseReference mDatabaseReference;
     private EditText mTextTitle;
     private EditText mTextPrice;
     private EditText mTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        FirebaseUtil.openFirebaseReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mTextTitle = findViewById(R.id.text_title);
        mTextPrice = findViewById(R.id.text_price);
        mTextDescription = findViewById(R.id.text_description);
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.insert_menu, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveDeal();
            Toast.makeText(getApplicationContext(), "Deal saved", Toast.LENGTH_LONG).show();
            clean();
            return true;
        }
         return super.onOptionsItemSelected(item);
     }

     private void saveDeal() {
        String title = mTextTitle.getText().toString();
        String price = mTextPrice.getText().toString();
        String description = mTextDescription.getText().toString();

        TravelDeal deal = new TravelDeal(title, price, description, "" );
        //saving to the firebase db
         mDatabaseReference.push().setValue(deal);
     }

     private void clean() {
        mTextTitle.setText("");
        mTextPrice.setText("");
        mTextDescription.setText("");
        mTextTitle.requestFocus();
     }
 }
