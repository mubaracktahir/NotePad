package com.mubaracktahir.notepad.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Note {
    private String description;
    private String date ;
    private String category;
    private final String DESCRIOTION = "description";
    private final String DATE = "date";
    private final String CATEGORY ="category";

    public Note(){

    }
    public Note(String description, String date, String category) {
        this.description = description;
        this.date = date;
        this.category = category;
    }
    public JSONObject convertToJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(DESCRIOTION,description);
        object.put(DATE,date);
        object.put(CATEGORY,category);
        return object;
    }

    public Note(JSONObject jsonObject) throws  JSONException{
        description = jsonObject.getString(DESCRIOTION);
        date = jsonObject.getString(DATE);
        category = jsonObject.getString(CATEGORY);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
