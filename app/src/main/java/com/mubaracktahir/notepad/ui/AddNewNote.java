package com.mubaracktahir.notepad.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.Utils.BNVUtils;

import java.util.Date;

public class AddNewNote extends AppCompatActivity {
    private EditText editText;
    private int index;
    MainActivity mainActivity;
    private Note note;
    int request = 0;
    TextView textView;
    TextView textView2;
    private BottomNavigationViewEx bottomNavigationViewEx;
    String option = MainActivity.UNCAT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        editText = findViewById(R.id.describtion);
        textView = findViewById(R.id.cancle);
        textView2 = findViewById(R.id.save);
        textView.setOnClickListener(v -> finish());
        textView2.setOnClickListener(view -> {
            if(getIntent().getIntExtra("request",1) == 2 ) {
                saveNote(2);
            }
            else
            saveNote(1);
        });

        if(getIntent().getIntExtra("request",1) == 2 ){

            Intent intent = getIntent();

            index = intent.getIntExtra(MainActivity.ITEM_KEY,1);

            note = mainActivity.notes.get(index);

            editText.setText(note.getDescription());


        }
        setUpBottomNav();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getIntExtra("request",1) == 2 ) {
            saveNote(2);
        }
        else
            saveNote(1);
    }

    public void saveNote(final int REQUEST){
        if (REQUEST == 1) {

            if (!TextUtils.isEmpty(editText.getText()))
            {
                Note note = new Note();


                note.setDescription(editText.getText().toString());
                note.setCategory(option);
                note.setDate("3:40 pm");

                mainActivity = new MainActivity();
                mainActivity.addNewNote(note,1,0);

                option = MainActivity.UNCAT;
            }

           showToast();
            finish();
        }
        else if (REQUEST == 2){
            mainActivity = new MainActivity();
            note.setDescription(editText.getText().toString());
            note.setCategory(option);
            mainActivity.addNewNote(note,2,index);
            showToast();
            ShowNote.description.setText(editText.getText());
            ShowNote.cat.setText(option);
            if (note.getCategory() == MainActivity.UNCAT) {

                ShowNote.cat.setTextColor( Color.parseColor("#5BFD74"));
            } else if (note.getCategory() == MainActivity.PERSONAL) {
                ShowNote.cat.setTextColor( Color.parseColor("#FFAB00"));
            } else if (note.getCategory() == MainActivity.STUDY) {
                ShowNote.cat.setTextColor( Color.parseColor("#69A3F3"));

            } else if (note.getCategory() == MainActivity.WORK) {
                ShowNote.cat.setTextColor( Color.parseColor("#AA00FF"));

            } else if (note.getCategory() == MainActivity.FAMILY) {
                ShowNote.cat.setTextColor( Color.parseColor("#F00057"));
            }

            finish();
        }
    }
    public void showToast(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View vw = layoutInflater.inflate(R.layout.customtoastlayout, findViewById(R.id.custom_layout));
        TextView txt = vw.findViewById(R.id.txt);
        String tx = editText.getText().toString();

        if (TextUtils.isEmpty(tx))
            txt.setText(getResources().getString(R.string.empty_note_not_saved));
        else txt.setText("Note saved");
        Toast toast = new Toast(getApplicationContext());
        toast.setView(vw);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }
    public void setUpBottomNav(){
            bottomNavigationViewEx = findViewById(R.id.bnb);
        BNVUtils.setUpProperties(bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Uncategorize:
                        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                        MenuItem menuItem0 = menu.getItem(0);
                        menuItem0.setCheckable(true);
                        option = MainActivity.UNCAT;
                        break;
                    case R.id.work:
                        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();

                        MenuItem menuItem1 = menu.getItem(1);
                        menuItem1.setCheckable(true);
                        option = MainActivity.WORK;

                        break;
                    case R.id.family:
                        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                        MenuItem menuItem2 = menu.getItem(2);
                        menuItem2.setCheckable(true);
                        option = MainActivity.FAMILY;
                        break;
                    case R.id.study:
                        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();

                        MenuItem menuItem3 = menu.getItem(3);
                        menuItem3.setCheckable(true);
                        option = MainActivity.STUDY;
                        break;
                    case R.id.personal:
                        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();

                        MenuItem menuItem4 = menu.getItem(4);
                        menuItem4.setCheckable(true);
                        option = MainActivity.PERSONAL;
                        break;

                }
                return false;
            }
        });
    }


}
