package com.ville.devproc.projecttracker.data.db.model;

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
        return  "Worker id: " + getId() + "\n" +
                "name: " + getName();
    }
}
