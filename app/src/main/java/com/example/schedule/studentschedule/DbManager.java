package com.example.schedule.studentschedule;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.schedule.studentschedule.Model.Assessment;
import com.example.schedule.studentschedule.Model.Assign;
import com.example.schedule.studentschedule.Model.Course;
import com.example.schedule.studentschedule.Model.DataItem;
import com.example.schedule.studentschedule.Model.Mentor;

import java.util.ArrayList;

import static java.sql.Types.NULL;

public class DbManager {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DbManager(Context context) {
        this.mContext = context;
        mDbHelper = new DbHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();

    }

    public ContentValues setData(Object dataObj, String tableName) {
        Log.d("Table Name:", tableName);
       // long id;
        ContentValues values = new ContentValues();
        switch (tableName) {
            case "term":

                DataItem termObj = (DataItem) dataObj;

                Log.d("Term in set data ID :", Integer.toString(termObj.getItemId()));
                values.put(DbHelper.TERM_TITLE, termObj.getItem());
                values.put(DbHelper.TERM_START_DATE, termObj.getStartDate());
                values.put(DbHelper.TERM_END_DATE, termObj.getEndDate());
                //   id = mDatabase.insert(tableName, null, values);
                break;

            case "course":

                Course courseObj = (Course) dataObj;
                values.put(DbHelper.COURSE_TITLE, courseObj.getItem());
                values.put(DbHelper.COURSE_START_DATE, courseObj.getStartDate());
                values.put(DbHelper.COURSE_END_DATE, courseObj.getEndDate());
                values.put(DbHelper.COURSE_STATUS, courseObj.getStatus());
                values.put(DbHelper.COURSE_NOTES, courseObj.getNotes());

                //   id = mDatabase.insert(tableName, null, values);
                break;

            case "assessment":

                Assessment assessmentObj = (Assessment) dataObj;
                values.put(DbHelper.ASSESSMENT_TITLE, assessmentObj.getTitle());
                values.put(DbHelper.ASSESSMENT_TYPE, assessmentObj.getType());
                values.put(DbHelper.ASSESSMENT_DUE_DATE, assessmentObj.getDueDate());
                // id = mDatabase.insert(tableName, null, values);
                break;

            case "mentor":

                Mentor mentorObj = (Mentor) dataObj;
                values.put(DbHelper.MENTOR_NAME, mentorObj.getName());
                values.put(DbHelper.MENTOR_PHONE, mentorObj.getPhone());
                values.put(DbHelper.MENTOR_EMAIL, mentorObj.getEmail());
                //  id = mDatabase.insert(tableName, null, values);
                break;
            case "assign":
                Assign assignObj = (Assign) dataObj;

                values.put(DbHelper.ASSIGN_TERM_ID, assignObj.getTermId());
                values.put(DbHelper.ASSIGN_COURSE_ID,assignObj.getCourseId());
                values.put(DbHelper.ASSIGN_ASSESSMENT_ID, assignObj.getAssessmentId());
                values.put(DbHelper.ASSIGN_MENTOR_ID, assignObj.getMentorId());

//
                //  id = mDatabase.insert(tableName, null, values);
                break;
        }


        // values.clear();
        return  values;
    }





    public long getRowCount(String tableName){
        return DatabaseUtils.queryNumEntries(mDatabase,tableName);
    }




    public void insertData(String tableName,ContentValues  values){
        mDatabase.insert(tableName, null, values);
    }

    public void update (String table, ContentValues values, String whereClause, long id ){
        Log.d("testing Item ID in DbManager :" , Long.toString(id) ) ;
        mDatabase.update(table,values,whereClause,new String[] { String.valueOf(id)});

    }

    public void delete (String table, String whereClause, long id){
        mDatabase.delete(table,whereClause, new String[] { String.valueOf(id)});

    }


    public Cursor query(boolean distinct,
                         String table,
                         String[] columns,
                         String selection,
                         String[] selectionArgs,
                         String groupBy,
                         String having,
                         String orderBy,
                         String limit){
        mDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor = mDatabase.query(distinct,table,columns,selection,
                selectionArgs,groupBy,having,orderBy,limit);


        return cursor;
    }

//    public int getTermId(String termTitle, String startDate, String endDate ){
//        int termId = 0;
//        final String[] columns = {DbHelper.TERM_ID };
//        //        cursor = dbManager.query("Select "+ DbHelper.TERM_TITLE + " FROM "+ DbHelper.TABLE_TERM);
//        String selection = DbHelper.TERM_TITLE + "=?" + " AND " + DbHelper.TERM_START_DATE + "=?" + " AND " + DbHelper.TERM_END_DATE + "=?";
//        String [] selectionArg = { termTitle, startDate , endDate };
//        Cursor cursor = query(true, DbHelper.TABLE_TERM, columns, selection, selectionArg, null,
//                null, null, null);
//
//
//        boolean isTerm =  cursor.moveToFirst();
//        if( isTerm ) {
//            if (!cursor.isAfterLast()) {
//                do {
//
//                    termId = cursor.getInt(cursor.getColumnIndex(DbHelper.TERM_ID);
//
//                } while (cursor.moveToNext());
//            }
//        }else   return -1;
//        cursor.close();
//        return termId;
//    }

    public ArrayList<DataItem>getAllTerms() {
        ArrayList<DataItem> list = new ArrayList();
        final String[] columns = {DbHelper.TERM_ID, DbHelper.TERM_TITLE, DbHelper.TERM_START_DATE, DbHelper.TERM_END_DATE};
        //        cursor = dbManager.query("Select "+ DbHelper.TERM_TITLE + " FROM "+ DbHelper.TABLE_TERM);
        Cursor cursor = query(true, DbHelper.TABLE_TERM, columns, null, null, null,
                null, null, null);


        boolean isTerm =  cursor.moveToFirst();
        if( isTerm ) {
            if (!cursor.isAfterLast()) {
                do {

                    DataItem term = new DataItem();
                    term.setItemId(cursor.getInt(cursor.getColumnIndex(DbHelper.TERM_ID)));
                    term.setItem(cursor.getString(cursor.getColumnIndex(DbHelper.TERM_TITLE)));
                    term.setStartDate(cursor.getString(cursor.getColumnIndex(DbHelper.TERM_START_DATE)));
                    term.setEndDate(cursor.getString(cursor.getColumnIndex(DbHelper.TERM_END_DATE)));
                    list.add(term);
                } while (cursor.moveToNext());
            }
        }else   return null;
        cursor.close();
       return list;
    }

    public ArrayList<Course>getAllCourse() {
        ArrayList<Course> list = new ArrayList();


        Cursor cursor = query(true, DbHelper.TABLE_COURSE, DbHelper.ALL_COLUMNS_COURSE, null, null, null,
                null, null, null);

        boolean isCourse =  cursor.moveToFirst();
        if( isCourse ) {
            if (!cursor.isAfterLast()) {
                do {
                    Course course = new Course();
                    course.setItemId(cursor.getInt(cursor.getColumnIndex(DbHelper.COURSE_ID)));
                    course.setItem(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_TITLE)));
                    course.setStartDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_START_DATE)));
                    course.setEndDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_END_DATE)));
                    list.add(course);
                } while (cursor.moveToNext());
            }
        }else return null;
        cursor.close();
        return list;
    }

    public ArrayList<Mentor> getAllMentor() {
        ArrayList<Mentor> list = new ArrayList();

        //        cursor = dbManager.query("Select "+ DbHelper.TERM_TITLE + " FROM "+ DbHelper.TABLE_TERM);
        Cursor cursor = query(true, DbHelper.TABLE_MENTOR, DbHelper.All_COLUMNS_MENTOR, null, null, null,
                null, null, null);

        boolean isMentor =  cursor.moveToFirst();
        if( isMentor ) {
            if (!cursor.isAfterLast()) {
                do {
                    Mentor mentor = new Mentor();
                    mentor.setMentorId(cursor.getInt(cursor.getColumnIndex(DbHelper.MENTOR_ID)));
                    mentor.setName(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_NAME)));
                    mentor.setPhone(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_PHONE)));
                    mentor.setEmail(cursor.getString(cursor.getColumnIndex(DbHelper.MENTOR_EMAIL)));
                    list.add(mentor);
                } while (cursor.moveToNext());
            }
        }else return null;
        cursor.close();
        return list;
    }

    public ArrayList<Assessment> getAllAssesment() {
        ArrayList<Assessment> list = new ArrayList();

        Cursor cursor = query(false, DbHelper.TABLE_ASSESSMENT, DbHelper.All_COLUMNS_ASSESSMENT, null, null, null,
                null, null, null);

        boolean isAssessment =  cursor.moveToFirst();
        if( isAssessment ) {
            if (!cursor.isAfterLast()) {
                do {
                    Assessment assessment = new Assessment();
                    assessment.setAssessmentId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSESSMENT_ID)));
                    assessment.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_TITLE)));
                    assessment.setType(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_TYPE)));
                    assessment.setDueDate(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_DUE_DATE)));
                    list.add(assessment);
                } while (cursor.moveToNext());
            }
        }else return null;
        cursor.close();
        return list;
    }

    public ArrayList<Course> getCoursesOfTerm(int termId){
        Log.d("termId in getCourese",String.valueOf(termId));
        ArrayList<Course> list = new ArrayList<>();

        String table = DbHelper.TABLE_ASSIGN + "," + DbHelper.TABLE_COURSE + "," + DbHelper.TABLE_TERM;
        String [] id = { String.valueOf(termId)};
        String [] projection =  DbHelper.ALL_COLUMNS_COURSE;
        String selection =
                DbHelper.TABLE_COURSE + "." + DbHelper.COURSE_ID + "=" + DbHelper.TABLE_ASSIGN + "." +  DbHelper.ASSIGN_COURSE_ID +
                        " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=" + DbHelper.TABLE_TERM + "." + DbHelper.TERM_ID +
                        " AND " + DbHelper.TABLE_ASSIGN + "." + DbHelper.ASSIGN_TERM_ID + "=?" ;

        Cursor cursor = query(true,
                table,
                projection,
                selection,
                id,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        if ( !cursor.isAfterLast()) {
            do {
                Course course = new Course();
                course.setItemId(cursor.getInt(cursor.getColumnIndex(DbHelper.COURSE_ID)));
                course.setItem(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_TITLE)));
                course.setStartDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_START_DATE)));
                course.setEndDate(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_END_DATE)));
                course.setStatus(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_STATUS)));
                course.setNotes(cursor.getString(cursor.getColumnIndex(DbHelper.COURSE_NOTES)));

                list.add(course);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    public int getAssessmentId( int termId, int courseId ){
        int assessmentId = 0;
        String selection = DbHelper.ASSIGN_TERM_ID + "=?" + " AND " + DbHelper.ASSIGN_COURSE_ID + "=?";
        String [] selectionArgs = { String.valueOf(termId) , String.valueOf(courseId) };

        Cursor cursor = query(
                true,
                DbHelper.TABLE_ASSIGN,
                new String[]{ DbHelper.ASSIGN_ASSESSMENT_ID },
                selection,
                selectionArgs,null,null,null,null);

        boolean isAssessment = cursor.moveToFirst();

        if( isAssessment ) {
            if (!cursor.isAfterLast()) {
                do {
                    assessmentId = cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_ASSESSMENT_ID));
//                    assign.setTermId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_TERM_ID)));
//                    assign.setCourseId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_COURSE_ID)));
//                    assign.setMentorId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_MENTOR_ID)));

                } while (cursor.moveToNext());
            }

        }else return -1;
        cursor.close();
        return assessmentId;
    }

    public Assessment getAssessment(int id){
        Assessment assessment = new Assessment();
        Cursor cursor = query(false,DbHelper.TABLE_ASSESSMENT,DbHelper.All_COLUMNS_ASSESSMENT,
                DbHelper.ASSESSMENT_ID + "=?", new String[] {String.valueOf(id)},null,null, null,null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                assessment.setAssessmentId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSESSMENT_ID)));
                assessment.setTitle(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_TITLE)));
                assessment.setType(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_TYPE)));
                assessment.setDueDate(cursor.getString(cursor.getColumnIndex(DbHelper.ASSESSMENT_DUE_DATE)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return assessment;
    }


    public int getAssignId( int termId, int courseId, int mentorId  ){
        int assignId = 0;
        String selection = DbHelper.ASSIGN_TERM_ID + "=?" + " AND " + DbHelper.ASSIGN_COURSE_ID + "=?" + " AND " + DbHelper.ASSIGN_MENTOR_ID + "=?";
        String [] selectionArgs = { String.valueOf(termId) , String.valueOf(courseId) , String.valueOf(mentorId)};

        Cursor cursor = query(
                true,
                DbHelper.TABLE_ASSIGN,
                new String[]{ DbHelper.ASSIGN_ID },
                selection,
                selectionArgs,null,null,null,null);

        boolean isAssign = cursor.moveToFirst();

        if( isAssign ) {
            if (!cursor.isAfterLast()) {
                do {
                    assignId = cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_ID));
//                    assign.setTermId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_TERM_ID)));
//                    assign.setCourseId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_COURSE_ID)));
//                    assign.setMentorId(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSIGN_MENTOR_ID)));

                } while (cursor.moveToNext());
            }

        }else return -1;
        cursor.close();
        return assignId;
    }

//    public Cursor query(String selectQuery) {
//        Cursor cursor = null;
//        mDatabase = mDbHelper.getReadableDatabase();
//
//        cursor = mDatabase.rawQuery(selectQuery, null);
//        return cursor;
//    }




        public int getQueryCount(int termId, int courseId, String tablename){
        String selection =
                DbHelper.TABLE_ASSESSMENT + "." + "_id" + "=" + DbHelper.ASSIGN_ASSESSMENT_ID + " AND " +
                DbHelper.TABLE_TERM + "." + "_id" + "=" + DbHelper.ASSIGN_TERM_ID + " AND " +
                DbHelper.TABLE_COURSE + "." + "_id" + "=" + DbHelper.ASSIGN_COURSE_ID + " AND " +
                DbHelper.ASSIGN_COURSE_ID + "=?" + " AND "+
                DbHelper.ASSIGN_TERM_ID + "=?" + " AND "+ DbHelper.ASSESSMENT_TITLE + " IS NOT NULL";

        Cursor cursor = query(false,
                tablename,new String []{ DbHelper.TABLE_ASSESSMENT + "."+ DbHelper.ASSESSMENT_ID},
                selection,  new String[]{String.valueOf(courseId), String.valueOf(termId)},
                null,
                null,
                null,
                null);

       return cursor.getCount();

    }

    public ArrayList<Assessment> getAssessmentOfCourse(int termId, int courseId, String tablename){
        ArrayList<Assessment> list = new ArrayList<>();
        String selection =
                DbHelper.TABLE_ASSESSMENT + "." + "_id" + "=" + DbHelper.ASSIGN_ASSESSMENT_ID + " AND " +
                        DbHelper.TABLE_TERM + "." + "_id" + "=" + DbHelper.ASSIGN_TERM_ID + " AND " +
                        DbHelper.TABLE_COURSE + "." + "_id" + "=" + DbHelper.ASSIGN_COURSE_ID + " AND " +
                        DbHelper.ASSIGN_COURSE_ID + "=?" + " AND "+
                        DbHelper.ASSIGN_TERM_ID + "=?" + " AND "+ DbHelper.ASSESSMENT_TITLE + " IS NOT NULL";

        Cursor cursor = query(false,
                tablename,new String []{ DbHelper.TABLE_ASSESSMENT + "."+ DbHelper.ASSESSMENT_ID },
                selection,  new String[]{String.valueOf(courseId), String.valueOf(termId)},
                null,
                null,
                null,
                null);


        cursor.moveToFirst();
       if( ! cursor.isAfterLast()) {
           do {
               list.add(getAssessment(cursor.getInt(cursor.getColumnIndex(DbHelper.ASSESSMENT_ID))));
           }while (cursor.moveToNext());

       }

       return list;
    }

    public void open(){
        mDatabase = mDbHelper.getWritableDatabase();

    }

    public void close(){

        mDbHelper.close();
    }
   }
