package com.mubaracktahir.notepad.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mubaracktahir.notepad.JSONSerializer;
import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.ui.MainActivity;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    public static final String TAG = "JSONSERIALIZER";
    public List<Note> notes;
    int a = 100;
    private Context context;
    public ListAdapter(List<Note> notes, Context context) {

        this.context = context;
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
        Note note = notes.get(i);
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.view_list, viewGroup, false);

        View v = view.findViewById(R.id.view);
        TextView description = view.findViewById(R.id.description);
        TextView category = view.findViewById(R.id.category);
        TextView date = view.findViewById(R.id.date);
        if (note.getCategory() == MainActivity.UNCAT) {
            v.setBackgroundColor(Color.parseColor("#5BFD74"));
            category.setTextColor(Color.parseColor("#5BFD74"));
        } else if (note.getCategory() == MainActivity.STUDY) {

            v.setBackgroundColor(Color.parseColor("#69A3F3"));
            category.setTextColor(Color.parseColor("#69A3F3"));
        } else if (note.getCategory() == MainActivity.WORK) {
            v.setBackgroundColor(Color.parseColor("#AA00FF"));
            category.setTextColor(Color.parseColor("#AA00FF"));
        } else if (note.getCategory() == MainActivity.FAMILY) {
            v.setBackgroundColor(Color.parseColor("#F00057"));
            category.setTextColor(Color.parseColor("#F00057"));
        } else if (note.getCategory() == MainActivity.PERSONAL) {
            v.setBackgroundColor(Color.parseColor("#FFAB00"));
            category.setTextColor(Color.parseColor("#FFAB00"));
        }

        description.setText(note.getDescription());
        category.setText(note.getCategory());
        date.setText(note.getDate());

        return view;

    }

    public void addNewNote(Note note, final int REQUEST, int index) {
        if (REQUEST == 1) {
            //notes.add(new Note("Mubby","34:54",MainActivity.PERSONAL));

            notes.add(0, note);
            Log.e(TAG, "new not added with request code 1");
            notifyDataSetChanged();

        }
        if (REQUEST == 2) {
            notes.remove(index);
            notes.add(index, note);
            Log.e(TAG, "new not added with request code 2)");

        }
        notifyDataSetChanged();

    }

    public void delete(Note note) {
        notes.remove(note);
    }

}
