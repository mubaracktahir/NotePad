package com.mubaracktahir.notepad.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.ui.MainActivity;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    public static final String TAG = "JSONSERIALIZER";
    public List<Note> notes;
    private Context context;


    public ListAdapter(List<Note> notes, Context context) {

        this.context = context;

        // updating the adapter with the available notes
        this.notes = notes;


    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //setting the value of the listview
        Note note = notes.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.view_list, viewGroup, false);
        }
        View v = view.findViewById(R.id.view);
        TextView description = view.findViewById(R.id.description);
        TextView category = view.findViewById(R.id.category);
        TextView date = view.findViewById(R.id.date);
        if (note.getCategory().equals(MainActivity.UNCAT)) {
            v.setBackgroundColor(Color.parseColor("#5BFD74"));
            category.setTextColor(Color.parseColor("#5BFD74"));
        } else if (note.getCategory().equals(MainActivity.STUDY)) {
            v.setBackgroundColor(Color.parseColor("#69A3F3"));
            category.setTextColor(Color.parseColor("#69A3F3"));
        } else if (note.getCategory().equals(MainActivity.WORK)) {
            v.setBackgroundColor(Color.parseColor("#AA00FF"));
            category.setTextColor(Color.parseColor("#AA00FF"));
        } else if (note.getCategory().equals(MainActivity.FAMILY)) {
            v.setBackgroundColor(Color.parseColor("#F00057"));
            category.setTextColor(Color.parseColor("#F00057"));
        } else if (note.getCategory().equals(MainActivity.PERSONAL)) {
            v.setBackgroundColor(Color.parseColor("#FFAB00"));
            category.setTextColor(Color.parseColor("#FFAB00"));
        }

        description.setText(note.getDescription());
        category.setText(note.getCategory());
        date.setText(note.getDate());
        notifyDataSetChanged();
        return view;

    }
}
