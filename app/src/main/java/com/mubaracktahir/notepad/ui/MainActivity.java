package com.mubaracktahir.notepad.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.mubaracktahir.notepad.Adapter.RecyclerViewAdapter;
import com.mubaracktahir.notepad.JSONSerializer;
import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.Utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    boolean is_in_action_mode = false;
    public final static String DESC_KEY = "description";
    public final static String ITEM_KEY = "index";
    public final static String UNCAT = "Uncategorized";
    public final static String STUDY = "Study";
    public final static String FAMILY = "Family affair";
    public final static String PERSONAL = "Personal";
    public final static String WORK = "Work";
    public static final String TAG = "MAINACTIVITY";
    public static RecyclerViewAdapter recyclerviewAdapter;
    public static boolean isActionmodeEnabled = false;
    static List<Note> notes;
    public final String REQUEST = "request";
    public final int DISPLAY = 1;
    public DatabaseReference rootDB;
    LinearLayout deleteIcon;
    ActionMode actionMode = null;
    ActionModeCallback actionModeCallback;
    private RecyclerView recyclerview;
    private FloatingActionButton addNewNote;
    private LinearLayout del;
    private float translatiionY = 100f;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private JSONSerializer jsonSerializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionModeCallback = new ActionModeCallback();

//        rootDB = FirebaseDatabase.getInstance().getReference();
        recyclerview = findViewById(R.id.recyclerview);
        addNewNote = findViewById(R.id.fab);
        deleteIcon = findViewById(R.id.del_lyt);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotes();
            }
        });
        jsonSerializer = new JSONSerializer("NotePad.json" +
                "", this);


        if (jsonSerializer.isFilePresent())

            // initializing the list of notes.
            try {
                notes = jsonSerializer.load();

                Log.e(TAG, "file exist");
            } catch (IOException e) {

                notes = new ArrayList<>();
                e.printStackTrace();
            }
        else {
            Log.e(TAG, "file does not exist");
            Toast.makeText(this, "file does not exist", Toast.LENGTH_LONG).show();
            notes = new ArrayList<>();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setHasFixedSize(true);

        //sending the notes to the adapter.
        recyclerviewAdapter = new RecyclerViewAdapter(notes, getApplicationContext());

        //setting the adapter of the listview to baseadapter
        recyclerview.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnClickListener(new RecyclerViewAdapter.OnClickListener() {

            @Override
            public void onItemClick(View view, Note note, int pos) {
                if (recyclerviewAdapter.getSelectedItemCount() <= 0) {

                    //storing the description of the note that was selected
                    String description = note.getDescription();
                    int index = pos;

                    //transiting from the mainactivity to the activity that shows the note
                    Intent intent = new Intent(MainActivity.this, ShowNote.class);

                    //sending the description of the note that was selected to the activity that shows note
                    intent.putExtra(DESC_KEY, description);

                    //sending the position of the note that was selected to the activity that shows note
                    intent.putExtra(ITEM_KEY, index);

                    //sending the category of the note that was selected to the activity that shows note
                    intent.putExtra("cat", note.getCategory());

                    //sending the date the note which  was selected was created to the activity that shows note
                    intent.putExtra("date", note.getDate());


                    //requesting to display the note on the ShowNote activity
                    intent.putExtra(REQUEST, DISPLAY);

                    //sending the color of the category
                    if (note.getCategory().equals(MainActivity.UNCAT)) {
                        Toast.makeText(getApplicationContext(), note.getCategory(), Toast.LENGTH_LONG).show();

                        intent.putExtra("color", "#5BFD74");
                    } else if (note.getCategory().equals(MainActivity.PERSONAL)) {
                        intent.putExtra("color", "#FFAB00");
                    } else if (note.getCategory().equals(MainActivity.STUDY)) {
                        intent.putExtra("color", "#69A3F3");
                    } else if (note.getCategory().equals(MainActivity.WORK)) {
                        intent.putExtra("color", "#AA00FF");
                    } else if (note.getCategory().equals(MainActivity.FAMILY)) {
                        intent.putExtra("color", "#F00057");
                    }

                    startActivity(intent);
                } else {

                    enableActionMode(pos);

                }
            }

            @Override
            public void onItemLongClick(View view, Note note, int pos) {
                enableActionMode(pos);
            }
        });

        //responding to long clickevent
        //listView.setOnItemLongClickListener(this);

        //listening to the Fab to transit to the add new note activity
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Transiting to the Activity that adds new Note
                Intent intent = new Intent(MainActivity.this, AddNewNote.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all:
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort_time:
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.sort_tabs:
                Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.cloud:
                startActivity(new Intent(MainActivity.this, LogInActivity.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewNote(Note note, final int REQUEST, int index) {
        if (REQUEST == 1) {
            notes.add(0, note);
            recyclerviewAdapter.notifyDataSetChanged();
        } else {
            notes.remove(index);
            notes.add(index, note);
            recyclerviewAdapter.notifyDataSetChanged();
        }
        recyclerviewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return true;
    }

    public void enableActionMode(int pos) {
        isActionmodeEnabled = true;
        if (actionMode == null)
            actionMode = startSupportActionMode(actionModeCallback);
        toggleSelection(pos);

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            jsonSerializer.save(notes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            jsonSerializer.save(notes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //contextual actionmode callback for long clicking

    private void toggleSelection(int position) {
        recyclerviewAdapter.toggleSelection(position);
        int count = recyclerviewAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressLint("ResourceAsColor")
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimary);
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            deleteIcon.setVisibility(View.VISIBLE);
            addNewNote.animate().translationY(translatiionY).alpha(0f).setInterpolator(interpolator).setDuration(600).start();
            deleteIcon.animate().translationY(8f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;

        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();


            if (id == R.id.delete) {

               // deleteInboxes();

                mode.finish();

                return true;
            }
            return false;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            deleteIcon.animate().translationY(translatiionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
            addNewNote.animate().translationY(8f).alpha(1f).setInterpolator(interpolator).setDuration(400).start();
            isActionmodeEnabled = false;
            recyclerviewAdapter.clearSelections();
            actionMode = null;

        }
    }
    public void prepareSelection(View view, int position){
        if(((CheckBox)view).isChecked()){
            recyclerviewAdapter.toggleSelection(position);
        }


    }
    private void deleteNotes() {
        List<Integer> selectedItemPositions = recyclerviewAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            recyclerviewAdapter.removeData(selectedItemPositions.get(i));
        }
        recyclerviewAdapter.notifyDataSetChanged();
        actionMode.finish();
    }
}
