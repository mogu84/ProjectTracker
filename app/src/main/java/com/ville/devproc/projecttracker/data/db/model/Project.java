package com.ville.devproc.projecttracker.data.db.model;

public class Project {

    public static final String TABLE_NAME = "project";

    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_INPUT_DATE = "input_date";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";

    private int project_id;
    private String name;
    private String description;
    private String location;
    private long input_date;
    private long start_date;
    private long end_date;

    public static final String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_INPUT_DATE + " INTEGER, " +
            COLUMN_START_DATE + " INTEGER, " +
            COLUMN_END_DATE + " INTEGER" +
            ")";


    public Project() {

    }

    public int getId() {
        return project_id;
    }

    public void setId(int project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String project_name) {
        this.name = project_name;
    }

    public String getDescription() { return description; }

    public void setDescription(String project_description) {
        this.description = project_description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String project_location) {
        this.location = project_location;
    }

    public long getInputDate() { return input_date; }

    public void setInputDate(long inputDate) { this.input_date = inputDate; }

    public long getStartDate() {
        return start_date;
    }

    public void setStartDate(long start_date) {
        this.start_date = start_date;
    }

    public long getEndDate() {
        return end_date;
    }

    public void setEndDate(long end_date) {
        this.end_date = end_date;
    }

    public String toString() {
        return  "Project id: " + getId() + "\n" +
                "Name: " + getName() + "\n" +
                "Description: " + getDescription() + "\n" +
                "Location: " + getLocation() + "\n" +
                "Input date: " + getInputDate() + "\n" +
                "Start date: " + getStartDate() + "\n" +
                "End date: " + getEndDate();
    }
}
