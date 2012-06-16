package test.andro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author noone
 */
public class CreateActivity extends Activity {

    private static final int START_TIME_DIALOG_ID = 100;
    private static final int START_DATE_DIALOG_ID = 200;
    private static final int FIN_TIME_DIALOG_ID = 300;
    private static final int FIN_DATE_DIALOG_ID = 400;
    private EditText startTimeEdit;
    private EditText finishTimeEdit;
    private SqlTasksAdapter adapter;
    private EditText taskNameEdit;
    private EditText timeDateEditingView;
    private Task task;
    private EditText startDateEdit;
    private EditText finishDateEdit;
    private EditText durationEdit;
    private EditText commentEdit;
    private TextView createEditView;
    private boolean editing = false;
    private RadioButton radioVeryImp;
    private RadioButton radioImp;
    private RadioButton radioNotImp;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.create_edit);
        adapter = SqlTasksAdapter.getInstance(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        createEditView = (TextView) findViewById(R.id.create_label);
        taskNameEdit = (EditText) findViewById(R.id.taskname_edit);
        startTimeEdit = (EditText) findViewById(R.id.start_time_edit);
        addTimeDateEditDialog(startTimeEdit);
        finishTimeEdit = (EditText) findViewById(R.id.finish_time_edit);
        addTimeDateEditDialog(finishTimeEdit);
        startDateEdit = (EditText) findViewById(R.id.start_date_edit);
        addTimeDateEditDialog(startDateEdit);
        finishDateEdit = (EditText) findViewById(R.id.finish_date_edit);
        addTimeDateEditDialog(finishDateEdit);
        durationEdit = (EditText) findViewById(R.id.duration_edit);
        commentEdit = (EditText) findViewById(R.id.comment_edit);
        radioVeryImp = (RadioButton) findViewById(R.id.veryimp_radio);
        radioImp = (RadioButton) findViewById(R.id.imp_radio);
        radioNotImp = (RadioButton) findViewById(R.id.notimp_radio);

        int id = extras.getInt("EDITING", -1);
        editing = id != -1;
        if (editing) {
            task = adapter.getTaskByID(id);
            createEditView.setText(R.string.edit_task_title);
        } else {
            task = new Task(0, "Задача");
            createEditView.setText(R.string.create_task_title);
        }


        taskNameEdit.setText(task.getName());
        startTimeEdit.setText(time2Text(task.getStartDate()));
        finishTimeEdit.setText(time2Text(task.getFinishDate()));
        startDateEdit.setText(date2Text(task.getStartDate()));
        finishDateEdit.setText(date2Text(task.getFinishDate()));
        durationEdit.setText(String.valueOf(task.getDurationInMinutes()));

        int priority = R.id.imp_radio;
        switch (task.getPriority()) {
            case Important:
                priority = R.id.imp_radio;
                break;
            case VeryImportant:
                priority = R.id.veryimp_radio;
                break;
            case NotImportant:
                priority = R.id.notimp_radio;
                break;
        }
        RadioButton radioButton = (RadioButton) findViewById(priority);
        radioButton.setChecked(true);

        commentEdit.setText(task.getComment());


        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                save2DB();
            }
        });
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                exit();
            }
        });
    }

    private void exit() {
        this.finish();
    }

    private void save2DB() {
        
        task.setName(taskNameEdit.getText().toString());
        task.setComment(commentEdit.getText().toString());
        task.setDurationInMinutes(Integer.valueOf(durationEdit.getText().toString()));
        task.setEnded(false);
        task.setFinishDate(finishDateEdit.getText().toString() + " " + finishTimeEdit.getText().toString());
        task.setStartDate(startDateEdit.getText().toString() + " " + startTimeEdit.getText().toString());
        if (radioVeryImp.isChecked()) {
            task.setPriority(TaskPriority.VeryImportant);
        }
        if (radioImp.isChecked()) {
            task.setPriority(TaskPriority.Important);
        }
        if (radioNotImp.isChecked()) {
            task.setPriority(TaskPriority.NotImportant);
        }
        if (editing) {
            adapter.updateItem(task.getId(), task);
        } else {
            adapter.addItem(task);
        }
        setResult(RESULT_OK, new Intent());
        this.finish();

    }

    private String time2Text(Date date) {
        return Task.getTimeFormat().format(date);
    }

    private CharSequence date2Text(Date date) {
        return Task.getDateFormat().format(date);
    }

    private void addTimeDateEditDialog(EditText edit) {
        edit.setClickable(true);
        edit.setInputType(InputType.TYPE_NULL);
        edit.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                timeDateEditingView = (EditText) arg0;
                if (timeDateEditingView == startTimeEdit) {
                    showDialog(START_TIME_DIALOG_ID);
                }
                if (timeDateEditingView == startDateEdit) {
                    showDialog(START_DATE_DIALOG_ID);
                }
                if (timeDateEditingView == finishTimeEdit) {
                    showDialog(FIN_TIME_DIALOG_ID);
                }
                if (timeDateEditingView == finishDateEdit) {
                    showDialog(FIN_DATE_DIALOG_ID);
                }
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Date now = Calendar.getInstance().getTime();
        Date finishDate = task.getFinishDate() == null ? now : task.getFinishDate();
        Date startDate = task.getStartDate() == null ? now : task.getStartDate();
        switch (id) {
            case START_TIME_DIALOG_ID: {
                timeDateEditingView = startTimeEdit;
                return new TimePickerDialog(this,
                        timeSetListener,
                        startDate.getHours(),
                        startDate.getMinutes(), true);
            }
            case FIN_TIME_DIALOG_ID: {
                timeDateEditingView = finishTimeEdit;
                return new TimePickerDialog(this,
                        timeSetListener,
                        finishDate.getHours(),
                        finishDate.getMinutes(), true);
            }
            case START_DATE_DIALOG_ID: {
                timeDateEditingView = startDateEdit;
                return new DatePickerDialog(this,
                        dateSetListener,
                        startDate.getYear() + 1900,
                        startDate.getMonth(),
                        startDate.getDate());
            }
            case FIN_DATE_DIALOG_ID: {
                timeDateEditingView = finishDateEdit;
                return new DatePickerDialog(this,
                        dateSetListener,
                        finishDate.getYear() + 1900,
                        finishDate.getMonth(),
                        finishDate.getDate());
            }
        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timeDateEditingView.setText(Task.getTimeFormat().format(new Date(2012, 6, 3, hourOfDay, minute)));
                }
            };
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker dp, int year, int month, int day) {
                    timeDateEditingView.setText(Task.getDateFormat().format(new Date(year-1900, month, day)));
                }
            };
}
