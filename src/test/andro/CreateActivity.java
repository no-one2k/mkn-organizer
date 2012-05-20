/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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
        Task item = adapter.getTaskByID(id);
        taskNameEdit=(EditText) findViewById(R.id.taskname_edit);
        taskNameEdit.setText(item.getName());
        startTimeEdit = (EditText) findViewById(R.id.start_time_edit);
        startTimeEdit.setClickable(true);
        startTimeEdit.setInputType(InputType.TYPE_NULL);
        startTimeEdit.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                showDialog(TIME_DIALOG_ID);
                return true;
            }
        });
        
        finishTimeEdit = (EditText) findViewById(R.id.finish_time_edit);
        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

          
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        startTimeSetListener, 12, 13, true);
        }
        return null;
    }
    
    private TimePickerDialog.OnTimeSetListener startTimeSetListener =
    new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTimeEdit.setText(hourOfDay+":"+minute);
        }
    };
}
