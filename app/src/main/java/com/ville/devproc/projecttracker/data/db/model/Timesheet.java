package com.ville.devproc.projecttracker.data.db.model;

public class Timesheet {

    public static final String TABLE_NAME = "timesheet";

    public static final String COLUMN_TIMESHEET_ID = "timesheet_id";
    public static final String COLUMN_WORKER_ID = "worker_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_INPUT_DATE = "input_date";

    private int timesheet_id;
    private int worker_id;
    private int project_id;
    private double duration;
    private long input_date;

    public static final String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_TIMESHEET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORKER_ID + " INTEGER, " +
                COLUMN_PROJECT_ID + " INTEGER, " +
                COLUMN_DURATION + " INTEGER, " +
                COLUMN_INPUT_DATE + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_WORKER_ID + ") REFERENCES " + Worker.TABLE_NAME + "(" + Worker.COLUMN_WORKER_ID + "), " +
                "FOREIGN KEY(" + COLUMN_PROJECT_ID + ") REFERENCES " + Project.TABLE_NAME + "(" + Project.COLUMN_PROJECT_ID + ")" +
            ")";

    public int getTimesheetId() {
        return timesheet_id;
    }

    public void setTimesheetId(int timesheet_id) {
        this.timesheet_id = timesheet_id;
    }

    public int getWorkerId() {
        return worker_id;
    }

    public void setWorkerId(int worker_id) {
        this.worker_id = worker_id;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }

    public double getDuration() { return duration; }

    public void setDuration(double duration) { this.duration = duration; }

    public long getInputDate() {
        return input_date;
    }

    public void setInputDate(long input_date) {
        this.input_date = input_date;
    }

    public String toString() {
        return  "Timesheet id: " + getTimesheetId() + "\n" +
                "Worker id: " + getWorkerId() + "\n" +
                "Project id: " + getProjectId() + "\n" +
                "Duration: " + getDuration() + "\n" +
                "Input date: " + getInputDate();
    }
}
