package com.example.travelmantics;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class FirebaseUtils {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    public static  FirebaseUtils firebaseUtils;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static ArrayList<TravelDeal> mDeals;
    private static Activity caller;
    private static final int RC_SIGN_IN=123;
    private FirebaseUtils(){};

    public static void openFBReference(String ref, final Activity callerActivity) {
        if (firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            mFirebaseDatabase=FirebaseDatabase.getInstance();
            mFirebaseAuth=FirebaseAuth.getInstance();
            caller =callerActivity;
            mAuthListener=new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUtils.signIn();
            Toast.makeText(callerActivity.getBaseContext(),"Welcome Back !",Toast.LENGTH_LONG).show();

        }
    };

        }
        mDeals = new ArrayList<>();
        mDatabaseReference= mFirebaseDatabase.getReference().child(ref);

    }
    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    public static void attachListener(){
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
    public static void detachListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
