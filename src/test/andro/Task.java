package test.andro;

import java.util.Date;

/**
 *
 * @author noone
 */
public class Task {
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
        return (stringDate!=null)? new Date(Date.parse(stringDate)):null;
    }
    
    
    
    
}
