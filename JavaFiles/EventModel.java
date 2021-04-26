package com.example.pesuapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventModel {
    private String description;
    private String event;
    private Date date;
    private String time ;
    private String usr ;

    public EventModel() {}

    public EventModel(String description, Date date, String event, String time, String usr) {
        this.description = description;
        this.date = date;
        this.event = event;
        this.time=time;
        this.usr=usr;
    }

    public String getDescription() {
        return description;
    }

    public String getDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String info3= formatter.format(date);
        return info3;
    }

    public boolean getU(String s,String d){
        if(s.contains("PES")){
            return true;
        }
        else{
            return false;
        }
    };

    public String getUsr() { return usr;}

    public String getEvent() {
        return event;
    }

    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        String info4= formatter.format(date);
        return info4;
    }
}
