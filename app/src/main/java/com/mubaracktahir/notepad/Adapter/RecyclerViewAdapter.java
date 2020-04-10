package com.mubaracktahir.notepad.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mubaracktahir.notepad.Model.Note;
import com.mubaracktahir.notepad.R;
import com.mubaracktahir.notepad.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mubarak Tahir on 4/10/2020.
 * Mubby inc
 * mubarack.tahirr@gmail.com
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private SparseBooleanArray selected_items;
    private Context context;
    private List<Note> notes;
    private OnClickListener onClickListener = null;
    private static MainActivity mainActivity;
    public RecyclerViewAdapter(List<Note> notes,Context context){
        this.context = context;
        this.notes = notes;
        //mainActivity = (MainActivity)context.getApplicationContext();

        selected_items = new SparseBooleanArray();

    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.view_list,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder viewHolder, int position) {
        final Note note = notes.get(position);
        Log.e("adapterout",String.valueOf(MainActivity.isActionmodeEnabled));

        if(!(MainActivity.isActionmodeEnabled) )
            viewHolder.selectedCheck.setVisibility(View.GONE);
        else {
            viewHolder.selectedCheck.setVisibility(View.VISIBLE);
            viewHolder.selectedCheck.setChecked(false);
        }
        viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, note, position);
            }
        });

        viewHolder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, note, position);
                return true;
            }
        });
        if (note.getCategory() .equals( MainActivity.UNCAT)){
            viewHolder.v.setBackgroundColor(Color.parseColor("#5BFD74"));
            viewHolder.category.setTextColor(Color.parseColor("#5BFD74"));
        }
        else if(note.getCategory() .equals( MainActivity.STUDY)) {
            viewHolder.v.setBackgroundColor(Color.parseColor("#69A3F3"));
            viewHolder.category.setTextColor(Color.parseColor("#69A3F3"));
        }  else if (note.getCategory() .equals( MainActivity.WORK) ) {
            viewHolder.v.setBackgroundColor(Color.parseColor("#AA00FF"));
            viewHolder.category.setTextColor(Color.parseColor("#AA00FF"));
        } else if (note.getCategory() .equals( MainActivity.FAMILY)) {
            viewHolder.v.setBackgroundColor(Color.parseColor("#F00057"));
            viewHolder.category.setTextColor(Color.parseColor("#F00057"));
        }  else if (note.getCategory() .equals( MainActivity.PERSONAL) ) {
            viewHolder.v.setBackgroundColor(Color.parseColor("#FFAB00"));
            viewHolder.category.setTextColor(Color.parseColor("#FFAB00"));
        }

        viewHolder.description.setText(note.getDescription());
        viewHolder.category.setText(note.getCategory());
        viewHolder.date.setText(note.getDate());
        toggleCheckedIcon(viewHolder,position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void toggleSelection(int pos) {
        //current_selected_idx = pos;

        //checking if the selected item has been selected before

        if (selected_items.get(pos, false)) {

            //if it has been selected then it should be deleted
            selected_items.delete(pos);
        } else {
            //else it should be added to the least spearedBooleanArreay object
            selected_items.put(pos, true);
        }

        notifyItemChanged(pos);
    }
    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }
    public void removeData(int position) {
        notes.remove(position);
        resetCurrentIndex();
    }
    private void resetCurrentIndex()
    {
        //current_selected_idx = -1;
    }
    public int getSelectedItemCount() {
        return selected_items.size();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener{
        void onItemClick(View view, Note note, int pos);

        void onItemLongClick(View view, Note note, int pos);
    }
    private void toggleCheckedIcon(MyViewHolder holder, int position) {
        if (selected_items.get(position, false)) {

            holder.selectedCheck.setVisibility(View.VISIBLE);
            holder.selectedCheck.setChecked(true);
            // if (current_selected_idx == position) resetCurrentIndex();

        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{


        public View v ;
        public TextView description ;
        public TextView category ;
        public TextView date ;
        public View lyt_parent;
        public CheckBox selectedCheck;

        public MyViewHolder(@NonNull View view) {
            super(view);
            v = view.findViewById(R.id.view);
            description = view.findViewById(R.id.description);
            category = view.findViewById(R.id.category);
            date = view.findViewById(R.id.date);
            lyt_parent =  view.findViewById(R.id.lyt_parent);
            selectedCheck = view.findViewById(R.id.check);
            selectedCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = new MainActivity();
                   // mainActivity.prepareSelection(view,getAdapterPosition());
                }
            });

        }

    }

}
