package com.ville.devproc.projecttracker.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.ville.devproc.projecttracker.data.db.model.Project;
import com.ville.devproc.projecttracker.data.db.model.ProjectWorker;
import com.ville.devproc.projecttracker.data.db.model.Timesheet;
import com.ville.devproc.projecttracker.data.db.model.Worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static class DatabaseManager {

        private int mOpenCounter;

        private static DatabaseManager instance;
        private static SQLiteOpenHelper mDatabaseHelper;
        private SQLiteDatabase mDatabase;

        public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
            if (instance == null) {
                instance = new DatabaseManager();
                mDatabaseHelper = helper;
            }
        }

        public static synchronized DatabaseManager getInstance() {
            if (instance == null) {
                throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                        " is not initialized, call initializeInstance(..) method first.");
            }

            return instance;
        }

        public synchronized SQLiteDatabase openDatabase() {
            mOpenCounter++;
            if(mOpenCounter == 1) {
                // Opening new database
                mDatabase = mDatabaseHelper.getWritableDatabase();
            }
            return mDatabase;
        }

        public synchronized void closeDatabase() {
            mOpenCounter--;
            if(mOpenCounter == 0) {
                // Closing database
                mDatabase.close();

            }
        }

    }

    // Logcat tag
    private static final String LOG = DBHelper.class.getName();

    // Database version
    public static final int DATABASE_VERSION = 2;
    //Database name
    public static final String DATABASE_NAME = "projects_db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating new tables to Database.
        db.execSQL(Project.CREATE_TABLE);
        db.execSQL(Worker.CREATE_TABLE);
        db.execSQL(ProjectWorker.CREATE_TABLE);
        db.execSQL(Timesheet.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deleting existing tables
        db.execSQL("DROP TABLE IF EXISTS " + Project.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Worker.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectWorker.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Timesheet.TABLE_NAME);

        // Creating tables again
        onCreate(db);
    }

    // ------------------- Project CRUD methods --------------------- //
    /** Creating a Project */
    public long createProject(Project project) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Project.COLUMN_NAME, project.getName());
        values.put(Project.COLUMN_DESCRIPTION, project.getDescription());
        values.put(Project.COLUMN_LOCATION, project.getLocation());
        values.put(Project.COLUMN_START_DATE, project.getStartDate());
        values.put(Project.COLUMN_END_DATE, project.getEndDate());
        values.put(Project.COLUMN_INPUT_DATE, project.getInputDate());

        // insert row
        long projectId = db.insert(Project.TABLE_NAME, null, values);

        DatabaseManager.getInstance().closeDatabase();

        return projectId;
    }
    /** Get single Project */
    public Project getProject(long projectId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT  * FROM " + Project.TABLE_NAME + " WHERE "
                + Project.COLUMN_PROJECT_ID + " = " + projectId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Project project = new Project();
        project.setId(c.getColumnIndex(Project.COLUMN_PROJECT_ID));
        project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME)) );
        project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
        project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
        project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
        project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
        project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

        return project;
    }
    /** Query Projects*/
    public Project query(int position) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT * FROM " + Project.TABLE_NAME +" ORDER BY " + Project.COLUMN_PROJECT_ID + " ASC " +"LIMIT " + position + ",1";

        //Log.d(LOG, selectQuery);

        Cursor c = null;
        Project project = new Project();

        try {
            c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            project.setId( c.getInt( c.getColumnIndex(Project.COLUMN_PROJECT_ID) ));
            project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME) ) );
            project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
            project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
            project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
            project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
            project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

        } catch(Exception e) {
            Log.e(LOG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            c.close();
            DatabaseManager.getInstance().closeDatabase();
            return project;
        }
    }
    /** Get all Projects */
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<Project>();
        String selectQuery = "SELECT  * FROM " + Project.TABLE_NAME;

        //Log.e(LOG, selectQuery);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Project project = new Project();
                project.setId( c.getInt( c.getColumnIndex(Project.COLUMN_PROJECT_ID) ) );
                project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME)) );
                project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
                project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
                project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
                project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
                project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

                // adding to projects list
                projects.add(project);
            } while (c.moveToNext());
        }

        return projects;
    }
    /** Get Project count */
    public int getProjectCount() {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String countQuery = "SELECT  * FROM " + Project.TABLE_NAME;
        //Log.d(LOG, countQuery);
        int count = 0;
        try {
            Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.e(LOG, "Project COUNT Exception! " + e.getMessage() );
        } finally {
            // return count
            DatabaseManager.getInstance().closeDatabase();
            return count;
        }
    }
    /** Update project */
    public int updateProject(Project project) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Project.COLUMN_NAME, project.getName());
        values.put(Project.COLUMN_DESCRIPTION, project.getDescription());
        values.put(Project.COLUMN_LOCATION, project.getLocation());
        values.put(Project.COLUMN_START_DATE, project.getStartDate());
        values.put(Project.COLUMN_END_DATE, project.getEndDate());
        values.put(Project.COLUMN_INPUT_DATE, project.getInputDate());

        // updating row
        return db.update(Project.TABLE_NAME, values, Project.COLUMN_PROJECT_ID + " = ?",
                new String[] { String.valueOf(project.getId()) });
    }
    /** Delete one project */
    public void deleteProject(long projectId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(Project.TABLE_NAME, Project.COLUMN_PROJECT_ID + " = ?",
                new String[] { String.valueOf(projectId) });

        DatabaseManager.getInstance().closeDatabase();
    }
    /**Delete multiple projects */
    public Boolean deleteProjects(List<Integer> projectIds) {

        Collections.sort(projectIds);
        String idList = TextUtils.join(",", projectIds);

        if(projectIds.size() <= 0 )
            throw new IndexOutOfBoundsException("Cannot delete empty project list");

        Log.v("deleteCheckedProjects", "DELETE FROM " + Project.TABLE_NAME + " WHERE " + Project.COLUMN_PROJECT_ID + " IN (" + idList + ");");

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int results = db.delete(Project.TABLE_NAME, Project.COLUMN_PROJECT_ID + " IN ("+idList+")",
                null );

        Log.v("deleteCheckedProjects", "Delete count: " + results);

        DatabaseManager.getInstance().closeDatabase();

        return results == projectIds.size();
    }

    // ------------------- Worker CRUD methods ---------------------- //
    /** Creating a Worker */
    public long createWorker(Worker worker) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Worker.COLUMN_NAME, worker.getName());

        // insert row
        long projectId = db.insert(Worker.TABLE_NAME, null, values);

        return projectId;
    }
    /** Get single Worker */
    public Worker getWorker(long workerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME + " WHERE "
                + Worker.COLUMN_WORKER_ID + " = " + workerId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Worker worker = new Worker();
        worker.setId(c.getColumnIndex(Worker.COLUMN_WORKER_ID));
        worker.setName( c.getString( c.getColumnIndex(Worker.COLUMN_NAME)) );

        return worker;
    }
    /** Get all Worker */
    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<Worker>();
        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Worker worker = new Worker();
                worker.setId(c.getColumnIndex(Project.COLUMN_PROJECT_ID));
                worker.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME)) );

                // adding to worker list
                workers.add(worker);
            } while (c.moveToNext());
        }

        return workers;
    }
    /** Get Worker count */
    public int getWorkerCount() {
        String countQuery = "SELECT  * FROM " + Worker.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    /** Update Worker */
    public int updateWorker(Worker worker) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Worker.COLUMN_NAME, worker.getName());

        // updating row
        return db.update(Worker.TABLE_NAME, values, Worker.COLUMN_WORKER_ID + " = ?",
                new String[] { String.valueOf(worker.getId()) });
    }
    /** Delete project */
    public void deleteWorker(long workerId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Worker.TABLE_NAME, Worker.COLUMN_WORKER_ID + " = ?",
                new String[] { String.valueOf(workerId) });
    }

    // ----------------- ProjectWorker CRUD methods ----------------- //
    /** Create single ProjectWorker */
    public long createProjectWorker(int projectId, int workerId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put( ProjectWorker.COLUMN_PROJECT_ID, projectId);
        values.put( ProjectWorker.COLUMN_WORKER_ID, workerId );

        // insert row
        long projectWorkerId = db.insert(ProjectWorker.TABLE_NAME, null, values);

        return projectWorkerId;
    }
    /** Creating a ProjectWorkers */
    public boolean createProjectWorkers(ProjectWorker projectWorker) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        boolean result = true;

        for ( int workerId : projectWorker.getWorkerIds() ) {
            ContentValues values = new ContentValues();
            values.put( ProjectWorker.COLUMN_PROJECT_ID, projectWorker.getProjectId());
            values.put( ProjectWorker.COLUMN_WORKER_ID, workerId );

            // insert row
            long projectWorkerId = db.insert(ProjectWorker.TABLE_NAME, null, values);
            if( projectWorkerId != -1 ) {
                result = false;
            }
        }

        return result;
    }
    /** Get all Workers of a Project */
    public List<Worker> getProjectWorkers(long projectId) {
        List<Worker> workers = new ArrayList<Worker>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + Worker.TABLE_NAME + " WHERE "
                + Worker.COLUMN_WORKER_ID + " IN (SELECT " + ProjectWorker.COLUMN_WORKER_ID
                + " FROM " + ProjectWorker.TABLE_NAME + " WHERE "
                + ProjectWorker.COLUMN_PROJECT_ID + " = " + projectId + ")";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Worker worker = new Worker();
                worker.setId(c.getInt((c.getColumnIndex(Worker.COLUMN_WORKER_ID))));
                worker.setName((c.getString(c.getColumnIndex(Worker.TABLE_NAME))));

                // adding to Workder list
                workers.add(worker);
            } while (c.moveToNext());
        }

        return workers;
    }
    /** Get all Projects of a Worker */
    public List<Project> getWorkerProjects(long workerId) {
        List<Project> projects = new ArrayList<Project>();

        String selectQuery = "SELECT * FROM " + Project.TABLE_NAME +" WHERE "
                + Project.COLUMN_PROJECT_ID + " IN (SELECT " + ProjectWorker.COLUMN_PROJECT_ID
                + " FROM " + ProjectWorker.TABLE_NAME + " WHERE " + ProjectWorker.COLUMN_WORKER_ID
                + " = " + workerId + ")";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Project project = new Project();
                project.setId(c.getColumnIndex(Project.COLUMN_PROJECT_ID));
                project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME)) );
                project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
                project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
                project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
                project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
                project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

                // adding to projects list
                projects.add(project);
            } while (c.moveToNext());
        }

        return projects;
    }
    /** Update project */
    public boolean updateProjectWorker(ProjectWorker projectWorker) {

        // TODO: NEEDS TESTING
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int updateResult = 1;

        for ( int workerId : projectWorker.getWorkerIds()) {
            ContentValues values = new ContentValues();
            values.put(ProjectWorker.COLUMN_PROJECT_ID, projectWorker.getProjectId());
            values.put(ProjectWorker.COLUMN_WORKER_ID, workerId);

            // updating row(s)
            updateResult = updateResult & db.update(ProjectWorker.TABLE_NAME, values, ProjectWorker.COLUMN_PROJECTWORKER_ID + " = ?",
                    new String[] { String.valueOf(projectWorker.getProjectWorkerId()) });
        }

        return updateResult == 1;
    }
    /** Delete project */
    public void deleteProjectWorker(long projectWorkerId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Project.TABLE_NAME, Project.COLUMN_PROJECT_ID + " = ?",
                new String[] { String.valueOf(projectWorkerId) });
    }

    // ------------------ Timesheet CRUD methods -------------------- //

    /** Create single Timesheet */
    public long createTimesheet(Timesheet timesheet) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put( ProjectWorker.COLUMN_PROJECT_ID, timesheet.getProjectId() );
        values.put( ProjectWorker.COLUMN_WORKER_ID, timesheet.getWorkerId() );
        values.put( Timesheet.COLUMN_DURATION, timesheet.getDuration() );
        values.put( Timesheet.COLUMN_INPUT_DATE, timesheet.getInputDate() );

        // insert row
        long timesheetId = db.insert(ProjectWorker.TABLE_NAME, null, values);
        timesheet.setTimesheetId((int) timesheetId);

        return timesheetId;
    }
    /** Get Timesheets of a Project between startDate and endDate*/
    public List<Timesheet> getProjectTimesheetsFrom(long projectId, long startDate, long endDate) {
        List<Timesheet> timesheets = new ArrayList<Timesheet>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + Timesheet.TABLE_NAME + " WHERE " +
                Timesheet.COLUMN_PROJECT_ID + " = " + projectId + " AND " +
                Timesheet.COLUMN_INPUT_DATE + " >= " + startDate + " AND " +
                Timesheet.COLUMN_INPUT_DATE + " <= " + endDate;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Timesheet timesheet = new Timesheet();
                timesheet.setTimesheetId( c.getInt( ( c.getColumnIndex( Timesheet.COLUMN_TIMESHEET_ID ) ) ) );
                timesheet.setProjectId( c.getInt( c.getColumnIndex( Timesheet.COLUMN_PROJECT_ID ) ) );
                timesheet.setWorkerId( c.getInt( c.getColumnIndex( Timesheet.COLUMN_WORKER_ID ) ) );
                timesheet.setDuration( c.getInt( c.getColumnIndex( Timesheet.COLUMN_DURATION ) ) );
                timesheet.setInputDate( c.getInt( c.getColumnIndex( Timesheet.COLUMN_INPUT_DATE ) ) );

                // adding to Workder list
                timesheets.add(timesheet);
            } while (c.moveToNext());
        }

        return timesheets;
    }
    /** Get all Timesheets of a Worker */
    public List<Timesheet> getWorkerTimesheets(long workerId) {
        List<Timesheet> timesheets = new ArrayList<Timesheet>();

        String selectQuery = "SELECT * FROM " + Timesheet.TABLE_NAME + " WHERE " + Timesheet.COLUMN_WORKER_ID + " = " + workerId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Timesheet timesheet = new Timesheet();
                timesheet.setTimesheetId( c.getColumnIndex( Timesheet.COLUMN_TIMESHEET_ID ) );
                timesheet.setProjectId( c.getInt( c.getColumnIndex( Timesheet.COLUMN_PROJECT_ID ) ) );
                timesheet.setWorkerId( c.getInt( c.getColumnIndex( Timesheet.COLUMN_WORKER_ID ) ) );
                timesheet.setDuration( c.getInt( c.getColumnIndex( Timesheet.COLUMN_DURATION ) ) );
                timesheet.setInputDate( c.getInt( c.getColumnIndex( Timesheet.COLUMN_INPUT_DATE ) ) );

                // adding to projects list
                timesheets.add(timesheet);
            } while (c.moveToNext());
        }

        return timesheets;
    }
    /** Update Timesheet */
    public boolean updateTimesheet(Timesheet timesheet) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put( Timesheet.COLUMN_TIMESHEET_ID, timesheet.getTimesheetId() );
        values.put( Timesheet.COLUMN_PROJECT_ID, timesheet.getProjectId() );
        values.put( Timesheet.COLUMN_WORKER_ID, timesheet.getWorkerId() );
        values.put( Timesheet.COLUMN_DURATION, timesheet.getDuration() );
        values.put( Timesheet.COLUMN_INPUT_DATE, timesheet.getInputDate() );

        // updating row(s)
        int updateResult = db.update(Timesheet.TABLE_NAME, values, Timesheet.COLUMN_TIMESHEET_ID + " = ?",
                new String[] { String.valueOf(timesheet.getTimesheetId()) });

        return updateResult == 1;
    }
    /** Delete project */
    public void deleteTimesheet(long timesheetId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Timesheet.TABLE_NAME, Timesheet.COLUMN_TIMESHEET_ID + " = ?",
                new String[] { String.valueOf(timesheetId) });
    }
}
