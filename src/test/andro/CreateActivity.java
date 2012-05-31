/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import java.util.Date;

/**
 *
 * @author noone
 */
public class CreateActivity extends Activity {

    private static final int TIME_DIALOG_ID = 0;
    private EditText startTimeEdit;
    private EditText finishTimeEdit;
    private SqlTasksAdapter adapter;
    private EditText taskNameEdit;
    private View timeDateEditingView;
    private Task task;
    private EditText startDateEdit;
    private EditText finishDateEdit;
    private EditText durationEdit;
    private EditText commentEdit;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.create_edit);
        adapter=new SqlTasksAdapter(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        int id = extras.getInt("EDITING",2);
        task = adapter.getTaskByID(id);
        taskNameEdit=(EditText) findViewById(R.id.taskname_edit);
        taskNameEdit.setText(task.getName());
        
        startTimeEdit = (EditText) findViewById(R.id.start_time_edit);
        startTimeEdit.setText(time2Text(task.getStartDate()));
        addTimeDateEditDialog(startTimeEdit);
        
        finishTimeEdit = (EditText) findViewById(R.id.finish_time_edit);
        finishTimeEdit.setText(time2Text(task.getFinishDate()));
        addTimeDateEditDialog(finishTimeEdit);
        
        startDateEdit = (EditText) findViewById(R.id.start_date_edit);
        startDateEdit.setText(date2Text(task.getStartDate()));
        addTimeDateEditDialog(startDateEdit);
        
        finishDateEdit = (EditText) findViewById(R.id.finish_date_edit);
        finishDateEdit.setText(date2Text(task.getFinishDate()));
        addTimeDateEditDialog(finishDateEdit);
        
        durationEdit = (EditText) findViewById(R.id.duration_edit);
        durationEdit.setText(String.valueOf(task.getDurationInMinutes()));
        
        int priority=R.id.imp_radio;
        switch(task.getPriority()){
            case Important:priority=R.id.imp_radio;break;
            case VeryImportant:priority=R.id.veryimp_radio;break;
            case NotImportant:priority=R.id.notimp_radio;break;
        }
        RadioButton radioButton=(RadioButton) findViewById(priority);
        radioButton.setChecked(true);
        
        commentEdit = (EditText) findViewById(R.id.comment_edit);
        commentEdit.setText(task.getComment());
        
        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

          
            }
        });
    }

    private String time2Text(Date date) {
        return date.getHours()+":"+date.getMinutes();
    }
    
    private CharSequence date2Text(Date date) {
        return DateFormat.format("dd.MM.yyyy", date);//date.getDay()getHours()+":"+date.getMinutes();
    }

    private void addTimeDateEditDialog(EditText edit) {
        edit.setClickable(true);
        edit.setInputType(InputType.TYPE_NULL);
        edit.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                timeDateEditingView=arg0;
                showDialog(TIME_DIALOG_ID);
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:{
                if(timeDateEditingView==startTimeEdit){
                    return new TimePickerDialog(this,
                        startTimeSetListener, task.getStartDate().getHours(), task.getStartDate().getMinutes(), true);
                }
                if(timeDateEditingView==finishTimeEdit){
                    return new TimePickerDialog(this,
                        startTimeSetListener, task.getFinishDate().getHours(), task.getFinishDate().getMinutes(), true);
                }
                if(timeDateEditingView==startDateEdit){
                    return new DatePickerDialog(this,
                        startDateSetListener, 
                            task.getStartDate().getYear(), 
                            task.getStartDate().getMonth(),
                            task.getStartDate().getDay());
                }
                if(timeDateEditingView==finishDateEdit){
                    return new DatePickerDialog(this,
                        startDateSetListener, 
                            task.getStartDate().getYear(), 
                            task.getStartDate().getMonth(),
                            task.getStartDate().getDay());
                }
            }
        }
        return null;
    }
    
    private TimePickerDialog.OnTimeSetListener startTimeSetListener =
    new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTimeEdit.setText(hourOfDay+":"+minute);
        }
    };
    
    private DatePickerDialog.OnDateSetListener startDateSetListener =
    new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker dp, int i, int i1, int i2) {
            
        }
    };
}
