package test.andro;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends ListActivity {
    public static final int CALENDAR_ACTIVITY = 200;
    public static final int CREATE_EDIT_ACTIVITY = 100;

    public static final String FERTERTERTE = "ferterterte";
    private SqlTasksAdapter adapter;
    Button btnCalendar;
    private Button btnAdd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        adapter = SqlTasksAdapter.getInstance(this);
        adapter.setFilterDate(Calendar.getInstance().getTime());
        setListAdapter(adapter);
        btnCalendar = (Button) findViewById(R.id.calendar_button);
        btnCalendar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                runCalendarActivity();
            }
        });
        btnAdd = (Button) findViewById(R.id.add_button);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                runEditActivity(-1);
            }
        });
        // get
        //  btnCalendar.setBackgroundDrawable(android.R.drawable.ic_menu_compass);
        btnCalendar.performClick();
    }

    public void runCalendarActivity() {
        Toast.makeText(getApplicationContext(), "cal", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SimpleCalendarViewActivity.class);
        startActivityForResult(intent, CALENDAR_ACTIVITY);
    }

    public void runEditActivity(int position) {
        setSelection(position);
        Long id = null;
        if (position != -1) {
            Task item = adapter.getItem(position);
            Toast.makeText(getApplicationContext(), item + " выбран_", Toast.LENGTH_SHORT).show();
            id = item.getId();
        }

        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("EDITING", (id == null) ? -1 : id.intValue());
        startActivityForResult(intent, CREATE_EDIT_ACTIVITY);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        adapter.refresh();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        runEditActivity(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
    }
}
