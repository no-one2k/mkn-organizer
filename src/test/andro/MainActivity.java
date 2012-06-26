package test.andro;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;
import java.util.Calendar;
import test.andro.vk.VkApp;

public class MainActivity extends ListActivity implements RunInterface {

    public static final int PREF_ACTIVITY = 200;
    public static final int CREATE_EDIT_ACTIVITY = 100;
    public static final String FERTERTERTE = "ferterterte";
    private SqlTasksAdapter adapter;
    private Button btnCalendar;
    private Button btnAdd;
    private Button btnDiagram;
    private ToggleButton btnShowAll;
    private ToggleButton btnSort;
    // объект для посылки команд сервису
    private Messenger mService = null;
    // объект для обработки ответов сервиса
    private Messenger mMessenger = new Messenger(new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NotifyService.REFRESH_CODE:
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
    private RunInterface runInterfac;

    // посылаем команду сервису
    private void refreshService() {
        Bundle bundle = new Bundle();
        Message msg = Message.obtain(null, NotifyService.REFRESH_CODE);
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
        runInterfac = new RunInterfaceImpl(this);
        setContentView(R.layout.main);
        adapter = SqlTasksAdapter.getInstance(this);
        //adapter.setFilterDate(Calendar.getInstance().getTime());
        setListAdapter(adapter);
        btnCalendar = (Button) findViewById(R.id.calendar_button);

        btnDiagram = (Button) findViewById(R.id.diagram_button);

        btnAdd = (Button) findViewById(R.id.add_button);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                runEditActivity(-1);
            }
        });

        btnShowAll = (ToggleButton) findViewById(R.id.show_all_button);
        btnShowAll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showAll();
            }
        });

        btnSort = (ToggleButton) findViewById(R.id.sort_button);
        btnSort.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                changeSort();
            }
        });

        boolean res = bindService(new Intent(this, NotifyService.class),
                mConnection, Context.BIND_AUTO_CREATE);
        // get
        //  btnCalendar.setBackgroundDrawable(android.R.drawable.ic_menu_compass);
        //btnCalendar.performClick();
    }

    private void showAll() {
        adapter.setShowAll(!adapter.isShowAll());
    }

    private void changeSort() {
        adapter.setSortOnlyByDate(!adapter.isSortOnlyByDate());
    }

    public void runListActivity(View v) {
        runInterfac.runListActivity(v);
    }

    public void runDiagramActivity(View v) {
        runInterfac.runDiagramActivity(v);
    }

    public void runCalendarActivity(View v) {
        runInterfac.runCalendarActivity(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preferences: {
                startActivityForResult(new Intent(getApplicationContext(), PrefActivity.class), PREF_ACTIVITY);
            }
            break;
            case R.id.menu_close: {
                stopService(new Intent(this, NotifyService.class));
                this.finish();

            }
            break;
            case R.id.menu_mail: {
                Intent goExtendedMail = new Intent(this, ExtendedMail.class);
                startActivity(goExtendedMail);
            }
            break;
                case R.id.menu_vk: {
                sendVK();
               
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void runEditActivity(int position) {
        //setSelection(position);
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
        adapter.setFilterDate(null);
        adapter.setShowAll(btnShowAll.isChecked());
        adapter.setSortOnlyByDate(btnSort.isChecked());
        adapter.refresh();
        refreshService();
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

    private void sendVK() {
        VkApp va=new VkApp(this);
        va.showLoginDialog();
        va.postToWall(FERTERTERTE);
    }
}
