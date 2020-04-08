package com.mubaracktahir.notepad;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mubaracktahir.notepad.Model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {
    private String mFileName;
    public static final String TAG = "JSONSERIALIZER";

    // this is used to deserialize the file from the disk


    // this is the context of the activity
    private Context mContext;

    //whenever the JSONSerialization class is called, The name of the JSON file is passed into the constructor and also the context
    //and also the name of the
    public JSONSerializer(String mFileName, Context mContext){
        this.mFileName = mFileName;
        this.mContext = mContext;
    }

    //the function below is used to save Object of type note to the disk

    public void save(List<Note> note) throws IOException {

        // an instance of the json array is created to write the json object to the disk

        JSONArray jArray = new JSONArray();

        // the writer is set to null because it is a local variable , and all local variable are immutable
        Writer writer= null;

        try {
            for (Note n : note) {


                jArray.put(n.convertToJSON());
                Log.e(TAG,jArray.toString());

            }

            OutputStream out = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
            Log.e(TAG,jArray.toString());
            Log.e(TAG,"File saved successfully!");
        }catch (Exception e){
            Log.e(TAG,"Unable to save file!");
        }finally {
            //  if ( writer != null;){
            writer.close();

            // }
        }
    }

    public ArrayList<Note> load() throws IOException, JSONException {

        ArrayList<Note> noteList = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {

            InputStream in = mContext.openFileInput(mFileName);
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();

            String line = null;
            if(bufferedReader.readLine() == null)
            Log.e(TAG,"nothing to read");

            while((line = bufferedReader.readLine()) != null){

                jsonString.append(line);


            }
            Log.e(TAG,line);




            JSONArray jsonArray = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            Log.e(TAG," " +jsonArray.length());
            Log.e(TAG,"loaded successfully");

            for(int i = 0; i<jsonArray.length(); i++){

                noteList.add(new Note(jsonArray.getJSONObject(i)));
            }


        }catch (Exception e){

            Log.e(TAG,"Unable to load notes!");

        }finally {
            //  if ( writer != null;){
            bufferedReader.close();

            // }
        }
        return noteList;

    }


}
