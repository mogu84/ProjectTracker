package com.ville.devproc.projecttracker.data.db.model;

import android.util.Log;

import org.json.JSONObject;

public class Worker {
    public static final String TABLE_NAME = "worker";

    public static final String COLUMN_WORKER_ID = "worker_id";
    public static final String COLUMN_NAME = "name";

    private int worker_id;
    private String name;

    public static final String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_WORKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" +
            ")";

    public Worker() {

    }

    public Worker(String jsonWorker) {
        try {
            JSONObject workerObject = new JSONObject(jsonWorker);
            this.setId( workerObject.getInt(COLUMN_WORKER_ID) );
            this.setName( workerObject.getString( COLUMN_NAME ) );
        } catch(Exception e) {
            Log.e("Worker : " + TABLE_NAME, "JSON INIT ERROR: " + e.getMessage() );
        }
    }

    public int getId() {
        return worker_id;
    }

    public void setId(int worker_id) {
        this.worker_id = worker_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        JSONObject returnObject = new JSONObject();

        try {
            returnObject.put(COLUMN_WORKER_ID, this.getId());
            returnObject.put(COLUMN_NAME, this.getName() );
        } catch (Exception e) {
            Log.e("Worker : " + TABLE_NAME, "JSON TOSTRING ERROR: " + e.getMessage() );
        }

        return returnObject.toString();
    }
}
