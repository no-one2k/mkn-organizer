package test.andro;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
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
    private Button btnDiagram;
    // объект для посылки команд сервису
    private Messenger mService = null;
    // объект для обработки ответов сервиса
    private Messenger mMessenger = new Messenger(new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NotifyService.TEST_CODE:
                    // получить данные, возвращенные сервисом и что-то с ними сделать
                    // Object obj = msg.getData().getSerializable("date");
                    // ...
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    });
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    // посылаем команду сервису
    // подробности смотри в аналогичном коде сервиса
    private void testService() {
        Bundle bundle = new Bundle();
        Message msg = Message.obtain(null, NotifyService.TEST_CODE);
        msg.setData(bundle);
        // заполняем поле объектом, который сервис будет
        // использовать для посылки ответа
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
        }
    }

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
        btnDiagram = (Button) findViewById(R.id.diagram_button);
        btnDiagram.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                runDiagramActivity();
            }
        });
        btnAdd = (Button) findViewById(R.id.add_button);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                runEditActivity(-1);
            }
        });

        boolean res = bindService(new Intent(this, NotifyService.class),
                mConnection, Context.BIND_AUTO_CREATE);
        // get
        //  btnCalendar.setBackgroundDrawable(android.R.drawable.ic_menu_compass);
        //btnCalendar.performClick();
    }

    public void runCalendarActivity() {
        Intent intent = new Intent(getApplicationContext(), SimpleCalendarViewActivity.class);
        startActivityForResult(intent, CALENDAR_ACTIVITY);
    }

    public void runDiagramActivity() {
        testService();
        //Intent intent = new Intent(getApplicationContext(), SimpleCalendarViewActivity.class);
        //startActivityForResult(intent, CALENDAR_ACTIVITY);
    }

    public void runEditActivity(int position) {
        setSelection(position);
        Long id = null;
        if (position != -1) {
            Task item = adapter.getItem(position);
            id = item.getId();
        }

        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("EDITING", (id == null) ? -1 : id.intValue());
        startActivityForResult(intent, CREATE_EDIT_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        unbindService(mConnection);
        adapter.onDestroy();
    }
}
