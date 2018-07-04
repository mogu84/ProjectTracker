package com.ville.devproc.projecttracker.data.db.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectWorker {

    public static final String TABLE_NAME = "projectworker";

    public static final String COLUMN_PROJECTWORKER_ID = "projectworker_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_WORKER_ID = "worker_id";

    private int projectworker_id;
    private int project_id;
    private List<Integer> worker_id;

    public static final String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_PROJECTWORKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORKER_ID + " INTEGER, " +
            COLUMN_PROJECT_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_WORKER_ID + ") REFERENCES " + Worker.TABLE_NAME + "(" + Worker.COLUMN_WORKER_ID + "), " +
            "FOREIGN KEY(" + COLUMN_PROJECT_ID + ") REFERENCES " + Project.TABLE_NAME + "(" + Project.COLUMN_PROJECT_ID + ")" +
            ")";

    public ProjectWorker() {
        worker_id = new ArrayList<Integer>();
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
        return  "ProjectWorker id: " + getProjectWorkerId() + "\n" +
                "Project id: " + getProjectId() + "\n" +
                "Workers: " + worker_id.size();
    }
}
