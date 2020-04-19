package com.mubaracktahir.notepad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;

public class CloudActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private static final String TAG = "CloudActivity";
    Button signOut;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        progressDialog = new ProgressDialog(this);
        cardView = findViewById(R.id.upload);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        signOut = findViewById(R.id.signout);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.signout:
                    signOut();
                    break;

                }
            }
        });
        showSignedInUserProfile();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllNotes();
            }
        });

    }
    public void showSignedInUserProfile(){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignInAccount != null){
            String acctName = googleSignInAccount.getDisplayName();
            Uri acctImage = googleSignInAccount.getPhotoUrl();
        }
    }
    public void logout(View view) {
       signOut();
    }
    private void signOut(){
       mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               Intent intent = new Intent(CloudActivity.this, LogInActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               FirebaseAuth.getInstance().signOut();
               startActivity(intent);
               finish();
           }
       });

    }

    public void loadAllNotes(){

    }

    public void saveAllNotes(){
        if(MainActivity.notes.size()>0) {
            progressDialog.setMessage("saving notes on cloud");
            progressDialog.show();
            for (Note note : MainActivity.notes) {
                DatabaseReference innerPush = databaseReference.push();
                innerPush.child("position").setValue(MainActivity.notes.indexOf(note));
                innerPush.child("description").setValue(note.getDescription());
                innerPush.child("date").setValue(note.getDate());
                innerPush.child("category").setValue(note.getCategory());
            }
            progressDialog.dismiss();
        }
        else {
            Toast.makeText(this, "You dont have any Note to save!!!",Toast.LENGTH_LONG).show();
        }
    }
}
