package com.example.stacyzolnikov.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stacyzolnikov on 7/15/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private DatabaseHelper mHelper;
    private static final String TAG = DatabaseHelper.class.getCanonicalName();

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EMPLOYEEJOB_DB";

    //Below are the two tables
    public static final String EMPLOYEE_LIST_TABLE_NAME = "EMPLOYEE_LIST";
    public static final String JOB_LIST_TABLE_NAME = "JOB_LIST";

    //Below are the columns for Employee List Table
    public static final String COL_ID = "_id";
    public static final String COL_SSN = "SSN";
    public static final String COL_FIRST_NAME = "FIRST_NAME";
    public static final String COL_LAST_NAME = "LAST_NAME";
    public static final String COL_YEAR_OF_BIRTH = "YEAR_OF_BIRTH";
    public static final String COL_CITY = "CITY";

    public static final String[] EMPLOYEE_COLUMNS = {COL_ID, COL_FIRST_NAME, COL_LAST_NAME, COL_CITY, COL_YEAR_OF_BIRTH, COL_SSN,};

    //Below are the columns for Job List Table
    public static final String COL_COMPANY = "COMPANY";
    public static final String COL_SALARY = "SALARY";
    public static final String COL_EXPERIENCE = "EXPERIENCE";

    public static final String[] JOB_COLUMNS = {COL_SSN, COL_COMPANY, COL_SALARY, COL_EXPERIENCE};

    private DatabaseHelper(Context context){
        super (context, "db", null, 1);
    }

    private static DatabaseHelper instance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    // private DatabaseHelper(Context context) {
   //     super (context, DATABASE_NAME, null, DATABASE_VERSION);
   // }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_EMPLOYEE);
        db.execSQL(SQL_CREATE_ENTRIES_JOB);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES_EMPLOYEE);
        db.execSQL(SQL_DELETE_ENTRIES_JOB);
        onCreate(db);
    }
    //Below are creating the tables
    private static final String SQL_CREATE_ENTRIES_EMPLOYEE =
            "CREATE TABLE " + EMPLOYEE_LIST_TABLE_NAME +
                    " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_FIRST_NAME + " TEXT," +
                    COL_LAST_NAME + " TEXT," +
                    COL_CITY + " TEXT, " +
                    COL_YEAR_OF_BIRTH + " TEXT," +
                    COL_SSN + " TEXT" + ")";

    private static final String SQL_CREATE_ENTRIES_JOB =
            "CREATE TABLE " + JOB_LIST_TABLE_NAME +
                    " (" +
                    COL_COMPANY + " TEXT," +
                    COL_SALARY + " TEXT," +
                    COL_EXPERIENCE + " TEXT," +
                    COL_SSN + " TEXT"  + ")";

                    //"FOREIGN KEY(" + COL_SSN + ") REFERENCES" + EMPLOYEE_LIST_TABLE_NAME + "(" + COL_SSN + ") )";

    private static final String SQL_DELETE_ENTRIES_EMPLOYEE = "DROP TABLE IF EXISTS " + EMPLOYEE_LIST_TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_JOB = "DROP TABLE IF EXISTS " + JOB_LIST_TABLE_NAME;
 //   public void emptyTables () {
 //       SQLiteDatabase db = getWritableDatabase();
//
 //       db.execSQL("DELETE FROM" + EMPLOYEE_LIST_TABLE_NAME);
 //       db.execSQL("DELETE FROM" + JOB_LIST_TABLE_NAME);
//
 //       db.close();
 //   }
    public void insertRowEmployee(Employee employee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SSN, employee.getSsn());
        values.put(COL_FIRST_NAME, employee.getFirstName());
        values.put(COL_LAST_NAME, employee.getLastName());
        values.put(COL_YEAR_OF_BIRTH, employee.getYear());
        values.put(COL_CITY, employee.getCity());
        db.insertOrThrow(EMPLOYEE_LIST_TABLE_NAME, null, values);
        db.close();
    }

    public void insertRowJob(Job job) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SSN, job.getSsn());
        values.put(COL_COMPANY, job.getCompany());
        values.put(COL_SALARY, job.getSalary());
        values.put(COL_EXPERIENCE, job.getExperience());
        db.insertOrThrow(JOB_LIST_TABLE_NAME, null, values);
        db.close();
    }
    public void close() {
        mHelper.close();
    }
    public Cursor getEmployeesFromSameCompany() {
        SQLiteDatabase db = getReadableDatabase();
        //String result = "";

        String query = "SELECT " + COL_ID + "," + COL_FIRST_NAME + ", " + COL_LAST_NAME + " FROM " + EMPLOYEE_LIST_TABLE_NAME + " JOIN " + JOB_LIST_TABLE_NAME + " ON " + EMPLOYEE_LIST_TABLE_NAME + "." + COL_SSN + " = " + JOB_LIST_TABLE_NAME + "." + COL_SSN + " WHERE " + COL_COMPANY + " LIKE 'Macy%'";
       Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
    public Cursor getCompaniesInBoston() {
        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT " + COL_ID + "," + COL_COMPANY + ", " + COL_CITY + " FROM " + EMPLOYEE_LIST_TABLE_NAME + " JOIN " + JOB_LIST_TABLE_NAME + " ON " + EMPLOYEE_LIST_TABLE_NAME + "." + COL_SSN + " = " + JOB_LIST_TABLE_NAME + "." + COL_SSN + " WHERE " + COL_CITY + " = 'Boston'";
        Cursor cursor = db.rawQuery(query,null);
      //  String name = null;
      //  if (cursor.moveToFirst()){
      //      name = cursor.getString(cursor.getColumnIndex("COMPANY"));
      //  }
      //  cursor.close();
        return cursor;
        //List<String> boston = new ArrayList<String>();
        //if (cursor.moveToFirst()){
        //    while(!cursor.isAfterLast()){
        //        boston.add(cursor.getString(cursor.getColumnIndex((COL_COMPANY))));
        //        cursor.moveToNext();
        //    }
        //}
        //cursor.close();
       // return cursor;


    }

    public Cursor highestSalary () {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(JOB_LIST_TABLE_NAME, new String[]{COL_COMPANY},
                null,null,null,null,COL_SALARY + " DESC");

        return cursor;
    }


}
