package com.example.schedule.studentschedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;


    public static final String TABLE_TERM = "term";
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE="termTitle";
    public static final String TERM_START_DATE = "termStartDate";
    public static final String TERM_END_DATE = "termEndDate";

    public static final String TABLE_COURSE = "course";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TITLE="courseTitle";
    public static final String COURSE_START_DATE = "courseStartDate";
    public static final String COURSE_END_DATE = "courseEndDate";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_NOTES = "courseNotes";

    public static final String TABLE_ASSESSMENT = "assessment";
    public static final String ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_TITLE ="assessmentTitle";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_DUE_DATE = "assessmentDueDate";

    public static final String TABLE_MENTOR = "mentor";
    public static final String MENTOR_ID = "_id";
    public static final String MENTOR_NAME = "mentorName";
    public static final String MENTOR_PHONE ="mentorPhone";
    public static final String MENTOR_EMAIL="mentorEmail";

    public static final String TABLE_ASSIGN = "assign";
    public static final String ASSIGN_ID = "_id";
    public static final String ASSIGN_TERM_ID = "termID";
    public static final String ASSIGN_COURSE_ID = "courseID";
    public static final String ASSIGN_ASSESSMENT_ID = "assesementID";
    public static final String ASSIGN_MENTOR_ID = "mentorID";



   /* public static final String[] ALL_COLUMNS_TERM    = { TERM_ID, TERM_TITLE , TERM_START_DATE , TERM_END_DATE };*/
    public static final String[] ALL_COLUMNS_COURSE   = { DbHelper.TABLE_COURSE + "."+ COURSE_ID, COURSE_TITLE , COURSE_START_DATE ,
           COURSE_END_DATE ,COURSE_STATUS ,COURSE_NOTES };
    public static final String[] All_COLUMNS_MENTOR = {  DbHelper.TABLE_MENTOR + "."+ MENTOR_ID, MENTOR_NAME , MENTOR_PHONE , MENTOR_EMAIL };
    //SQL to create term table
    public static final String[] All_COLUMNS_ASSESSMENT = { ASSESSMENT_ID, ASSESSMENT_TITLE , ASSESSMENT_TYPE , ASSESSMENT_DUE_DATE };

    private static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERM  + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START_DATE + " TEXT, " +
                    TERM_END_DATE + " TEXT" + ")";

    //create Course table
    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSE  + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START_DATE + " TEXT, " +
                    COURSE_END_DATE + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_NOTES + " TEXT" + ")";

    //creating Assessment table

    private static final String ASSESSMENT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENT + " (" +
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_TITLE + " TEXT, " +
                    ASSESSMENT_TYPE + " TEXT, " +
                    ASSESSMENT_DUE_DATE + " TEXT" + ")";

    //creating Mentor table
    private static final String MENTOR_TABLE_CREATE =
            "CREATE TABLE " + TABLE_MENTOR + " (" +
                    MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENTOR_NAME + " TEXT, " +
                    MENTOR_PHONE + " TEXT, " +
                    MENTOR_EMAIL + " TEXT" + ")";


    private static final String ASSIGN_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSIGN + " (" +
                    ASSIGN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSIGN_TERM_ID + " INTEGER, " +
                    ASSIGN_COURSE_ID + " INTEGER, " +
                    ASSIGN_ASSESSMENT_ID + " INTEGER DEFAULT NULL, " +
                    ASSIGN_MENTOR_ID + " INTEGER, " +
                    "FOREIGN KEY (" + ASSIGN_TERM_ID + ") REFERENCES " + TABLE_TERM + "(" + TERM_ID + "), " +
                    "FOREIGN KEY (" + ASSIGN_COURSE_ID + ") REFERENCES " + TABLE_COURSE + "(" + COURSE_ID + "), " +
                    "FOREIGN KEY (" + ASSIGN_ASSESSMENT_ID + ") REFERENCES " + TABLE_ASSESSMENT + "(" + ASSESSMENT_ID + "), " +
                    "FOREIGN KEY (" + ASSIGN_MENTOR_ID + ") REFERENCES " + TABLE_MENTOR+ "(" + MENTOR_ID + ") " +
                        " ON DELETE CASCADE " +
                        " ON UPDATE CASCADE " + ")";

    public DbHelper( Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
        db.execSQL(MENTOR_TABLE_CREATE);
        db.execSQL(ASSIGN_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_TERM );
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_COURSE );
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ASSESSMENT);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_MENTOR  );
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ASSIGN  );
        onCreate(db);
    }
}
