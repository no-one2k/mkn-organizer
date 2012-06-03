package test.andro;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author noone
 */
public class Task {
    
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static SimpleDateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT,Locale.US);

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
    
    private static final String TIME_FORMAT = "HH:mm";
    private static SimpleDateFormat timeFormat= new SimpleDateFormat(TIME_FORMAT,Locale.US);

    public static SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }
    
    private static final String DATE_TIME_FORMAT = DATE_FORMAT+" "+TIME_FORMAT;
    private static SimpleDateFormat dateTimeFormat= new SimpleDateFormat(DATE_TIME_FORMAT,Locale.US);

    public static SimpleDateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }
   
    
    
    long _id;
    String name;
    Date startDate;
    Date finishDate;
    int durationInMinutes;
    String comment;
    boolean ended = false;
    TaskPriority priority;

    public Task(long _id, String name) {
        this._id = _id;
        this.name = name;
        startDate=Calendar.getInstance().getTime();
        finishDate=Calendar.getInstance().getTime();
        durationInMinutes=0;
        comment="";
        priority=TaskPriority.Important;
    }
    
    

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }
    

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priorit) {
        this.priority = priorit;
    }
    

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    void setStartDate(String start) {
        startDate=string2Date (start);
    }

    void setFinishDate(String finish) {
        finishDate=string2Date (finish);
    }

    private Date string2Date(String stringDate) {
        if ((stringDate==null)|| ("".equalsIgnoreCase(stringDate))){
            return null;
                  }
        else{
            try {
                return getDateTimeFormat().parse(stringDate);
            } catch (ParseException ex) {
                Log.d("task", "date parsing failed",ex);
                return null;
            }
        }
    }
    
    
    
    
}
