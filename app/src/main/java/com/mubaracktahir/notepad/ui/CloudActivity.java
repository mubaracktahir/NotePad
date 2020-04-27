package com.mubaracktahir.notepad.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.data.JSONSerializer;

import java.io.IOException;
import java.util.List;

public class CloudActivity extends AppCompatActivity {
    private static final String TAG = "CloudActivity";
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    Button signOut;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    CardView cardView;
    CardView retrieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        progressDialog = new ProgressDialog(this);
        cardView = findViewById(R.id.upload);
        retrieve = findViewById(R.id.retrieve);
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
                switch (view.getId()) {
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
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveNotes();
            }
        });
    }

    public void showSignedInUserProfile() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String acctName = googleSignInAccount.getDisplayName();
            Uri acctImage = googleSignInAccount.getPhotoUrl();
        }
    }

    public void logout(View view) {
        signOut();
    }

    private void signOut() {
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

    public void loadAllNotes() {

    }

    public void saveAllNotes() {
        List<Note> notes = null;
        JSONSerializer jsonSerializer = new JSONSerializer("NotePad.json", this);
        try {
            notes = jsonSerializer.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!(MainActivity.notes.isEmpty())) {
            progressDialog.setMessage("saving notes on cloud");
            progressDialog.show();
            databaseReference.removeValue();
            for (Note note : notes) {
                DatabaseReference innerPush = databaseReference.push();
                innerPush.child("description").setValue(note.getDescription());
                innerPush.child("date").setValue(note.getDate());
                innerPush.child("category").setValue(note.getCategory());
            }
            progressDialog.dismiss();
        } else {
            Toast.makeText(this, "You dont have any Note to save!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void retrieveNotes() {
        JSONSerializer jsonSerializer = new JSONSerializer("NotePad.json", this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "Value:" + dataSnapshot.getValue());
                int i = 0;
                MainActivity mainActivity;
                MainActivity.notes.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    i++;
                    DataSnapshot data = dataSnapshot.child(dataSnapshot1.getKey());
                    Log.e(TAG, "Key: " + i + " " + data.child("date"));

                    String date = data.child("date").getValue().toString();
                    String description = data.child("description").getValue().toString();
                    String category = data.child("category").getValue().toString();

                    Note note = new Note();
                    note.setDescription(description);
                    note.setCategory(category);
                    note.setDate(date);
                    mainActivity = new MainActivity();
                    mainActivity.addNewNote(note, 3, 0);
                }
/*
                try {
                    jsonSerializer.save(MainActivity.notes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/

                Toast.makeText(getApplicationContext(), "" + MainActivity.notes.size() + "Note(s) retrieved successfully", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

