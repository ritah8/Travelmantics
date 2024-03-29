package com.example.travelmantics;

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

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebasedb;
    private DatabaseReference mDbRef;
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    private TravelDeal deal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebasedb=FirebaseDatabase.getInstance();
        mDbRef=mFirebasedb.getReference().child("traveldeals");
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtDescription=(EditText)findViewById(R.id.txtDescription);
        txtPrice=(EditText)findViewById(R.id.txtPrice);
        Intent intent =getIntent();
        TravelDeal deal=(TravelDeal) intent.getSerializableExtra("Deal");
        if (deal==null){
            deal= new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                    saveDeal();
                     Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                     //clean form after data saved
                    clean();
                    backToList();
                    return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this,"Deal Deleted",Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);

        return true;
    }
    private void saveDeal(){
        //read content of the text fields
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        //check if deal is new or existing
        if (deal.getId()==null){
            mDbRef.push().setValue(deal);
        }else {
            mDbRef.child(deal.getId()).setValue(deal);
        }

    }
    private void deleteDeal(){
        if (deal==null){
            Toast.makeText(this,"Please save the deal before deleting",Toast.LENGTH_SHORT).show();
            return;
        }
        mDbRef.child(deal.getId()).removeValue();
    }
    //return to the list after save
    private void backToList(){
        Intent intent= new Intent(this, ListActivity.class);
        startActivity(intent);
    }
    private void clean(){
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    };

}
