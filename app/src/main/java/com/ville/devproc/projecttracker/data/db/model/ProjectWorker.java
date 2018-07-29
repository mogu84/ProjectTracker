package com.ville.devproc.projecttracker.data.db.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectWorker {

    public static final String TABLE_NAME = "projectworker";

    public static final String COLUMN_PROJECTWORKER_ID = "projectworker_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_WORKER_ID = "worker_id";

    private int projectworker_id;
    private int project_id;
    private List<Integer> worker_id = new ArrayList<Integer>();;

    public static final String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_PROJECTWORKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORKER_ID + " INTEGER, " +
            COLUMN_PROJECT_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_WORKER_ID + ") REFERENCES " + Worker.TABLE_NAME + "(" + Worker.COLUMN_WORKER_ID + "), " +
            "FOREIGN KEY(" + COLUMN_PROJECT_ID + ") REFERENCES " + Project.TABLE_NAME + "(" + Project.COLUMN_PROJECT_ID + ")" +
            ")";

    public ProjectWorker() {
    }

    public ProjectWorker(String jsonProjectWorker) {
        JSONArray c = new JSONArray();

        try {
            JSONObject projectWorkerObj = new JSONObject(jsonProjectWorker);
            this.setProjectWorkerId( projectWorkerObj.getInt( COLUMN_PROJECT_ID) );
            this.setProjectId( projectWorkerObj.getInt(COLUMN_PROJECTWORKER_ID) );

            if( projectWorkerObj.has(COLUMN_WORKER_ID) )
                c = projectWorkerObj.getJSONArray(COLUMN_WORKER_ID);



            for (int i = 0; i < projectWorkerObj.getJSONArray(COLUMN_WORKER_ID).length(); i++) {
                worker_id.add( c.getJSONObject(i).getInt(Worker.COLUMN_WORKER_ID) );
            }
        } catch(Exception e) {
            Log.e( this.getClass().getName(), "JSON INIT ERROR: " + e.getMessage() );
        }
    }

    public int getProjectWorkerId() {
        return projectworker_id;
    }

    public void setProjectWorkerId(int projectWorkerId) {
        this.projectworker_id = projectWorkerId;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int projectId) {
        this.project_id = project_id;
    }

    public List<Integer> getWorkerIds() {
        return worker_id;
    }

    public void addWorkerId(int workerId) {
        worker_id.add(workerId);
    }

    public void setWorkerIds(List<Integer> workerIds) {
        this.worker_id = workerIds;
    }

    public String toString() {
        JSONObject returnObject = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            returnObject.put( COLUMN_PROJECTWORKER_ID, this.getProjectWorkerId() );
            returnObject.put( COLUMN_PROJECT_ID, this.getProjectId() );
            for (int value: worker_id ) {
                array.put(value);
            }
            returnObject.put(COLUMN_WORKER_ID, array);

        } catch(Exception e) {
            Log.e( this.getClass().getName(), "JSON TOSTRING ERROR: " + e.getMessage() );
        }
        return  returnObject.toString();
    }
}
