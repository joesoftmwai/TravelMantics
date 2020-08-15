 package com.joesoft.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

 public class TravelDealActivity extends AppCompatActivity {
     public static final String DEAL_POSITION = "com.joesoft.travelmantics.DEAL_POSITION";
     public static final int PICTURE_RESULT = 1;
     private FirebaseDatabase mFirebaseDatabase;
     private DatabaseReference mDatabaseReference;
     private EditText mTextTitle;
     private EditText mTextPrice;
     private EditText mTextDescription;
     private Button mBtnImage;
     private ImageView mImageView;
     private CardView mCVImageHolder;
     private TravelDeal mDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_deal);

//        FirebaseUtil.openFirebaseReference("traveldeals", this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mTextTitle = findViewById(R.id.text_title);
        mTextPrice = findViewById(R.id.text_price);
        mTextDescription = findViewById(R.id.text_description);
        mBtnImage = findViewById(R.id.btnImage);
        mImageView = findViewById(R.id.image);
        mCVImageHolder = findViewById(R.id.cvImageHolder);

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
         showImage(deal.getImageUrl());

         mBtnImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                 intent.setType("image/*");
                 intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                 startActivityForResult(Intent.createChooser(intent, "Insert picture"), PICTURE_RESULT);
             }
         });

     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
             Uri imageUri = data.getData();
             StorageReference ref = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());
//             ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                 @Override
//                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                     String url = taskSnapshot.getStorage().getDownloadUrl().toString();
//                     mDeal.setImageUrl(url);
//                     showImage(url);
//                 }
//             });

             ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {
                             String url = uri.toString();
                             mDeal.setImageUrl(url);
                             showImage(url);
                         }
                     });
                     String pictureName = taskSnapshot.getStorage().getPath();
                     mDeal.setImageName(pictureName);

                 }
             });
         }
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.travel_deal_menu, menu);
         if (FirebaseUtil.mIsAdmin) {
             menu.findItem(R.id.action_save).setVisible(true);
             menu.findItem(R.id.action_delete).setVisible(true);
             findViewById(R.id.btnImage).setVisibility(View.VISIBLE);
//             enableEditText(true);
         } else {
             menu.findItem(R.id.action_save).setVisible(false);
             menu.findItem(R.id.action_delete).setVisible(false);
             findViewById(R.id.btnImage).setVisibility(View.INVISIBLE);
//             enableEditText(false);
         }
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveDeal();
            Toast.makeText(getApplicationContext(), "Deal saved", Toast.LENGTH_LONG).show();
            backToList();
            return true;
        } else if(id == R.id.action_delete) {
            deleteDeal();
            Toast.makeText(getApplicationContext(), "Deal deleted", Toast.LENGTH_LONG).show();
            backToList();
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
        if (mDeal.getImageName()!=null && !mDeal.getImageName().isEmpty()) {
            StorageReference picRef = FirebaseUtil.mFirebaseStorage.getReference().child(mDeal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete image", "onSuccess: Image successfully deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Delete image", "onFailure: " + e.getMessage());
                }
            });
        }

     }

     private void backToList() {
        startActivity(new Intent(getApplicationContext(), ListActivity.class));
     }

     private void enableEditText (boolean isEnabled) {
        mTextTitle.setEnabled(isEnabled);
        mTextDescription.setEnabled(isEnabled);
        mTextPrice.setEnabled(isEnabled);
     }

     private void showImage (String url) {
        if (url != null && !url.isEmpty()) {
            mCVImageHolder.setVisibility(View.VISIBLE);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(mImageView);
        }
     }
 }
