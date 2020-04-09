package com.mubaracktahir.notepad.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mubaracktahir.notepad.R;

public class ShowNote extends AppCompatActivity {
    final static String REQUEST = "request";
    final static String TAG = "ShowNote";

    static final int ADD_ACTIVITY_REQUAST_CODE = 2;
    static TextView description;
    static TextView date;
    static TextView cat;
    int index;
    androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        floatingActionButton = findViewById(R.id.fabshow);
        description = findViewById(R.id.textdesc);
        date = findViewById(R.id.date);
        cat = findViewById(R.id.cat);


        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        final String desc = intent.getStringExtra(MainActivity.DESC_KEY);
        index = intent.getIntExtra(MainActivity.ITEM_KEY, 1);
        final String cate = intent.getStringExtra("cat");
        final String dat = intent.getStringExtra("date");
        final String color = intent.getStringExtra("color");


        description.setText(desc);
        date.setText(dat);
        cat.setText(cate);

//        Log.e(TAG,color);
  cat.setTextColor(Color.parseColor(color));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitToEditActivity();
            }
        });

    }

    public void dostuff(View view) {
        transitToEditActivity();
    }

    public void transitToEditActivity() {

        Intent intent1 = new Intent(ShowNote.this, AddNewNote.class);
        intent1.putExtra(MainActivity.ITEM_KEY, index);
        intent1.putExtra(REQUEST, ADD_ACTIVITY_REQUAST_CODE);

        startActivity(intent1);
    }
}
