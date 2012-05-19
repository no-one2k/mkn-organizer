/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

import java.util.Date;

/**
 *
 * @author noone
 */
public class Task {
    String name;
    Date startDate;
    Date finishDate;
    int durationInMinutes;
    String comment;
    boolean ended = false;
    TaskPriority priorit;

    public TaskPriority getPriorit() {
        return priorit;
    }

    public void setPriorit(TaskPriority priorit) {
        this.priorit = priorit;
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
    
    
    
    
}
