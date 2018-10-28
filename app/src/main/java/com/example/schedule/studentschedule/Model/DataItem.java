package com.example.schedule.studentschedule.Model;

import java.io.Serializable;

public class DataItem implements Serializable {
    private int itemId;
    private String item;
    private String startDate;
    private String endDate;


    public DataItem( String item, String startDate, String endDate ) {

        this.item = item;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DataItem() {

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    @Override
    public String toString() {
        return "DataItem{" +
                "itemId=" + itemId +
                ", item='" + item + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }


}
