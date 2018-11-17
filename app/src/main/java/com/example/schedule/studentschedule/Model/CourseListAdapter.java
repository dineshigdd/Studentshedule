package com.example.schedule.studentschedule.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.schedule.studentshedule.R;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends ArrayAdapter <Course>{
    private List<Course> courseList;




    public CourseListAdapter(Context context, int resource, ArrayList<Course> objects) {
        super(context, resource, objects);
        courseList = objects;
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        if( convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_course,parent,false);
        }

        Course course = courseList.get(position);
        TextView courseTitle = convertView.findViewById(R.id.courseTitle);
        courseTitle.setText(course.getItem() + " course "+ "\n" +
                            " from " + course.getStartDate() + " to " + course.getEndDate());

        return convertView;
    }




    }
