package com.ville.devproc.projecttracker.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
        long projectId = 0;
        try {
            projectId = db.insert(Project.TABLE_NAME, null, values);
        } catch(Exception e) {
            Log.e(LOG, "PROJECT CREATE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return projectId;
        }
    }
    /** Get single Project */
    public Project getProject(long projectId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT  * FROM " + Project.TABLE_NAME + " WHERE "
                + Project.COLUMN_PROJECT_ID + " = " + projectId;

        Log.e(LOG, selectQuery);

        Cursor c = null;
        Project project = new Project();
        try {
            c = db.rawQuery(selectQuery, null);

            if (c != null)
                c.moveToFirst();

            project.setId(c.getColumnIndex(Project.COLUMN_PROJECT_ID));
            project.setName(c.getString(c.getColumnIndex(Project.COLUMN_NAME)));
            project.setDescription(c.getString(c.getColumnIndex(Project.COLUMN_DESCRIPTION)));
            project.setLocation(c.getString(c.getColumnIndex(Project.COLUMN_LOCATION)));
            project.setInputDate(c.getLong(c.getColumnIndex(Project.COLUMN_INPUT_DATE)));
            project.setStartDate(c.getLong(c.getColumnIndex(Project.COLUMN_START_DATE)));
            project.setEndDate(c.getLong(c.getColumnIndex(Project.COLUMN_END_DATE)));
        } catch (Exception e) {
            Log.e(LOG, "PROJECT GET EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return project;
        }
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

            if(c != null)
                c.moveToFirst();

            project.setId( c.getInt( c.getColumnIndex(Project.COLUMN_PROJECT_ID) ));
            project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME) ) );
            project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
            project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
            project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
            project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
            project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

        } catch(Exception e) {
            Log.e(LOG, "PROJECT QUERY EXCEPTION! " + e.getMessage());
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

        DatabaseManager.initializeInstance(this);
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

        DatabaseManager.getInstance().closeDatabase();

        return projects;
    }
    /**Get all projects where worker is not yet assigned */
    public List<Project> getAllUnassignedProjects(long workerId) {
        List<Project> projects = new ArrayList<Project>();
        String selectWorkerExistingProjectsQuery = "SELECT " + ProjectWorker.COLUMN_PROJECT_ID + " FROM " + ProjectWorker.TABLE_NAME + " WHERE " + ProjectWorker.COLUMN_WORKER_ID + " = " + workerId;
        String selectQuery = "SELECT * FROM " + Project.TABLE_NAME + " WHERE " + Project.COLUMN_PROJECT_ID + " NOT IN (" + selectWorkerExistingProjectsQuery + ")";

        //Log.e(LOG, selectQuery);

        DatabaseManager.initializeInstance(this);
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

        DatabaseManager.getInstance().closeDatabase();

        return projects;
    }
    /** Get Project count */
    public long getProjectCount() {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        long count = 0;

        /*
        String countQuery = "SELECT  * FROM " + Project.TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        */

        try {
            count = DatabaseUtils.queryNumEntries(db, Project.TABLE_NAME);
        } catch (Exception e) {
            Log.e(LOG, "Project COUNT Exception! " + e.getMessage() );
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            // return count
            return count;
        }
    }
    /** Update project */
    public int updateProject(Project project) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Project.COLUMN_NAME, project.getName());
        values.put(Project.COLUMN_DESCRIPTION, project.getDescription());
        values.put(Project.COLUMN_LOCATION, project.getLocation());
        values.put(Project.COLUMN_START_DATE, project.getStartDate());
        values.put(Project.COLUMN_END_DATE, project.getEndDate());
        values.put(Project.COLUMN_INPUT_DATE, project.getInputDate());

        // updating row
        int updatedRows = 0;
        try {
            updatedRows = db.update(Project.TABLE_NAME, values, Project.COLUMN_PROJECT_ID + " = ?",
                    new String[]{String.valueOf(project.getId())});
        } catch(Exception e) {
            Log.e(LOG, "PROJECT UPDATE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return updatedRows;
        }
    }
    /** Delete one project */
    public int deleteProject(long projectId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int deletedRows = 0;
        try {
            deletedRows = db.delete(Project.TABLE_NAME, Project.COLUMN_PROJECT_ID + " = ?",
                    new String[]{String.valueOf(projectId)});
        } catch(Exception e) {
            Log.e(LOG, "PROJECT DELETE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return deletedRows;
        }
    }
    /**Delete multiple projects */
    public Boolean deleteProjects(List<Integer> projectIds) {

        Collections.sort(projectIds);
        String idList = TextUtils.join(",", projectIds);

        if(projectIds.size() <= 0 )
            throw new IndexOutOfBoundsException("Cannot delete empty project list");

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int results = 0;
        try {
            results = db.delete(Project.TABLE_NAME, Project.COLUMN_PROJECT_ID + " IN (" + idList + ")",
                    null);
        } catch (Exception e) {
            Log.e(LOG, "PROJECT DELETE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return results == projectIds.size();
        }
    }


    // ------------------- Worker CRUD methods ---------------------- //
    /** Creating a Worker */
    public long createWorker(Worker worker) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Worker.COLUMN_NAME, worker.getName());

        // insert row
        long projectId = 0;
        try {
            db.insert(Worker.TABLE_NAME, null, values);
        } catch(Exception e) {
            Log.e(LOG, "WORKER CREATE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return projectId;
        }
    }
    /** Get single Worker */
    public Worker getWorker(long workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME + " WHERE "
                + Worker.COLUMN_WORKER_ID + " = " + workerId;

        Cursor c = null;
        Worker worker = new Worker();
        try {
            db.rawQuery(selectQuery, null);

            if (c != null)
                c.moveToFirst();

            worker.setId(c.getColumnIndex(Worker.COLUMN_WORKER_ID));
            worker.setName( c.getString( c.getColumnIndex(Worker.COLUMN_NAME)) );
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return worker;
        }
    }
    /** Get all Worker */
    public List<Worker> getAllWorkers() {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT  * FROM " + Worker.TABLE_NAME;

        Cursor c = null;
        List<Worker> workers = new ArrayList<Worker>();
        try {
            c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    Worker worker = new Worker();
                    worker.setId(c.getInt( c.getColumnIndex(Worker.COLUMN_WORKER_ID) ) );
                    worker.setName( c.getString( c.getColumnIndex(Worker.COLUMN_NAME)) );

                    // adding to worker list
                    workers.add(worker);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e(LOG, "WORKER GET ALL EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return workers;
        }
    }
    /** Query Workers*/
    public Worker queryWorker(int position) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT * FROM " + Worker.TABLE_NAME +" ORDER BY " + Worker.COLUMN_WORKER_ID + " ASC " +"LIMIT " + position + ",1";

        //Log.d(LOG, selectQuery);

        Cursor c = null;
        Worker worker = new Worker();
        try {
            c = db.rawQuery(selectQuery, null);
            c.moveToFirst();
            worker.setId( c.getInt( c.getColumnIndex(Worker.COLUMN_WORKER_ID) ));
            worker.setName( c.getString( c.getColumnIndex(Worker.COLUMN_NAME) ) );
            c.close();
        } catch(Exception e) {
            Log.e(LOG, "QUERY WORKER EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return worker;
        }
    }
    /** Get Worker count */
    public long getWorkerCount() {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        /*
        String countQuery = "SELECT  * FROM " + Worker.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        */

        long count = 0;
        try {
            count = DatabaseUtils.queryNumEntries(db, Worker.TABLE_NAME);
        } catch( Exception e) {
            Log.e(LOG, "WORKER COUNT EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            // return count
            return count;
        }
    }
    /** Update Worker */
    public int updateWorker(Worker worker) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Worker.COLUMN_NAME, worker.getName());

        int updatedRows = 0;

        try {
            // updating row
            updatedRows = db.update(Worker.TABLE_NAME, values, Worker.COLUMN_WORKER_ID + " = ?",
                    new String[]{String.valueOf(worker.getId())});
        } catch (Exception e){
            Log.e(LOG, "WORKER UPDATE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return updatedRows;
        }
    }
    /** Delete project */
    public int deleteWorker(long workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int deleteCount = 0;
        try {
            deleteCount = db.delete(Worker.TABLE_NAME, Worker.COLUMN_WORKER_ID + " = ?",
                    new String[]{String.valueOf(workerId)});
        } catch( Exception e) {
            Log.e(LOG, "WORKER DELETE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return deleteCount;
        }
    }
    /**Delete multiple projects */
    public Boolean deleteWorkers(List<Integer> workerIds) {

        Collections.sort(workerIds);
        String idList = TextUtils.join(",", workerIds);

        if(workerIds.size() <= 0 )
            throw new IndexOutOfBoundsException("Cannot delete empty project list");

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int results = 0;
        try {
            results = db.delete(Worker.TABLE_NAME, Worker.COLUMN_WORKER_ID + " IN (" + idList + ")",
                    null);
        } catch (Exception e) {
            Log.e(LOG, "WORKER DELETE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return results == workerIds.size();
        }
    }




    // ----------------- ProjectWorker CRUD methods ----------------- //
    /** Create single ProjectWorker */
    public long createProjectWorker(int projectId, int workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put( ProjectWorker.COLUMN_PROJECT_ID, projectId);
        values.put( ProjectWorker.COLUMN_WORKER_ID, workerId );

        // insert row
        long projectWorkerId = 0;
        try {
            projectWorkerId = db.insert(ProjectWorker.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(LOG, "PROJECTWORKER CREATE EXCEPTION! " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return projectWorkerId;
        }
    }
    /** Creating a ProjectWorkers */
    public boolean createProjectWorkers(ProjectWorker projectWorker) {
        DatabaseManager.initializeInstance(this);
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
    /** Query ProjectWorker*/
    public Project queryProjectWorker(int position, int workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectProjectWorkerQuery = "SELECT "+ ProjectWorker.COLUMN_PROJECT_ID +" FROM " + ProjectWorker.TABLE_NAME + " WHERE " + ProjectWorker.COLUMN_WORKER_ID + " = " + workerId + " ORDER BY " + ProjectWorker.COLUMN_PROJECT_ID + " ASC " +"LIMIT " + position + ",1";
        String selectQuery = "SELECT * FROM " + Project.TABLE_NAME +" WHERE " + Project.COLUMN_PROJECT_ID + " IN (" + selectProjectWorkerQuery + ")";

        //Log.d(LOG, selectQuery);

        Cursor c = null;
        Project project = new Project();

        try {
            c = db.rawQuery(selectQuery, null);

            if(c != null)
                c.moveToFirst();

            project.setId( c.getInt( c.getColumnIndex(Project.COLUMN_PROJECT_ID) ));
            project.setName( c.getString( c.getColumnIndex(Project.COLUMN_NAME) ) );
            project.setDescription( c.getString( c.getColumnIndex(Project.COLUMN_DESCRIPTION)) );
            project.setLocation( c.getString( c.getColumnIndex(Project.COLUMN_LOCATION)) );
            project.setInputDate( c.getLong( c.getColumnIndex(Project.COLUMN_INPUT_DATE)) );
            project.setStartDate( c.getLong( c.getColumnIndex(Project.COLUMN_START_DATE)) );
            project.setEndDate( c.getLong( c.getColumnIndex(Project.COLUMN_END_DATE)) );

        } catch(Exception e) {
            Log.e(LOG, "PROJECT QUERY EXCEPTION! " + e.getMessage());
        } finally {
            c.close();
            DatabaseManager.getInstance().closeDatabase();
            return project;
        }
    }
    /** Get all Workers of a Project */
    public List<Worker> getProjectWorkers(long projectId) {
        List<Worker> workers = new ArrayList<Worker>();
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

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

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
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
    /** Get ProjectWorker count for specific Worker */
    public long getProjectWorkerCount(int workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        long count = 0;

        try {
            count = DatabaseUtils.queryNumEntries(db, ProjectWorker.TABLE_NAME, ProjectWorker.COLUMN_WORKER_ID + "=?", new String[] { String.valueOf(workerId) } );
        } catch (Exception e) {
            Log.e(LOG, "Project COUNT Exception! " + e.getMessage() );
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            // return count
            return count;
        }
    }
    /** Check if worker and project already exist */
    public Boolean checkProjectWorker(long projectId, long workerId) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = "SELECT COUNT(*) FROM " + ProjectWorker.TABLE_NAME + " WHERE "
                + Project.COLUMN_PROJECT_ID + " = " + projectId + " AND "
                + Worker.COLUMN_WORKER_ID + " = " + workerId;

        long queryResultCount = 0;
        Cursor c = null;
        try {
            queryResultCount =
                    DatabaseUtils.queryNumEntries( db,
                            ProjectWorker.TABLE_NAME,
                            ProjectWorker.COLUMN_PROJECT_ID + "=? AND " + ProjectWorker.COLUMN_WORKER_ID + "=?",
                            new String[] { Long.toString(projectId), Long.toString(workerId) }
                    );

        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return queryResultCount > 0;
        }
    }
    /** Update project */
    public boolean updateProjectWorker(ProjectWorker projectWorker) {
        DatabaseManager.initializeInstance(this);
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
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        try {
            db.delete(ProjectWorker.TABLE_NAME, ProjectWorker.COLUMN_WORKER_ID + " = ?",
                    new String[] { String.valueOf(projectWorkerId) });
        } catch(Exception e) {
            Log.e(LOG, ProjectWorker.class.getName() + " delete " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }
    /** Delete list of projects from a specific worker */
    public boolean deleteProjectWorkers(int workerId, List<Integer> projectIds) {
        Collections.sort(projectIds);
        String idList = TextUtils.join(",", projectIds);

        if(projectIds.size() <= 0 )
            throw new IndexOutOfBoundsException("Cannot delete empty project list");

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int results = 0;
        try {
            results = db.delete(ProjectWorker.TABLE_NAME, ProjectWorker.COLUMN_WORKER_ID + "=? AND " + ProjectWorker.COLUMN_PROJECT_ID + " IN (" + idList + ")",
                    new String[] { String.valueOf(workerId) } );
        } catch (Exception e) {
            Log.e(LOG, "PROJECT DELETE EXCEPTION " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return results == projectIds.size();
        }
    }
    /** Delete all rows from specific workerIds */
    public boolean deleteProjectWorkers(List<Integer> workerIds) {
        Collections.sort(workerIds);
        String idList = TextUtils.join(",", workerIds);

        if(workerIds.size() <= 0 )
            throw new IndexOutOfBoundsException("Cannot delete empty project list");

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        int results = 0;
        try {
            results = db.delete(ProjectWorker.TABLE_NAME, ProjectWorker.COLUMN_WORKER_ID + " IN (" + idList + ")", null);
        } catch (Exception e) {
            Log.e(LOG, "PROJECT DELETE EXCEPTION " + e.getMessage());
        } finally {
            DatabaseManager.getInstance().closeDatabase();
            return results == workerIds.size();
        }
    }



    // ------------------ Timesheet CRUD methods -------------------- //
    /** Create single Timesheet */
    public long createTimesheet(Timesheet timesheet) {
        DatabaseManager.initializeInstance(this);
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

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

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

        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
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
        DatabaseManager.initializeInstance(this);
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
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(Timesheet.TABLE_NAME, Timesheet.COLUMN_TIMESHEET_ID + " = ?",
                new String[] { String.valueOf(timesheetId) });
    }
}
