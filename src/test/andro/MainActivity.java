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
import java.util.Calendar;

public class MainActivity extends ListActivity implements RunInterface {

    public static final int PREF_ACTIVITY = 200;
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
    private RunInterface runInterfac;

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
        runInterfac = new RunInterfaceImpl(this);
        setContentView(R.layout.main);
        adapter = SqlTasksAdapter.getInstance(this);
        adapter.setFilterDate(Calendar.getInstance().getTime());
        setListAdapter(adapter);
        btnCalendar = (Button) findViewById(R.id.calendar_button);
//        btnCalendar.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                runCalendarActivity();
//            }
//        });
        btnDiagram = (Button) findViewById(R.id.diagram_button);
//        btnDiagram.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                runDiagramActivity();
//            }
//        });
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
            case R.id.menu_mail: {
                Intent goExtendedMail = new Intent(this, ExtendedMail.class);
				startActivity(goExtendedMail);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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

//    private void sendMail() {
//        try {
//            sender_mail_async async_sending = new sender_mail_async();
//            async_sending.execute();
//        } catch (Exception ex) {
//            Log.e("mail", "fail mail", ex);
//        }
//
//    }

//    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
//
//        ProgressDialog WaitingDialog;
//
//        @Override
//        protected void onPreExecute() {
//            WaitingDialog = ProgressDialog.show(MainActivity.this, "Отправка данных", "Отправляем сообщение...", true);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            WaitingDialog.dismiss();
//            Toast.makeText(MainActivity.this, "Отправка завершена!!!", Toast.LENGTH_LONG).show();
//            //((Activity)mainContext).finish();
//        }
//
//        @Override
//        protected Boolean doInBackground(Object... params) {
//
//            try {
//
//                MailSenderClass sender = new MailSenderClass("none.from.nowhere@gmail.com", "ytpme2kEmpty");
//
//                sender.sendMail("ta-dam", "important message", "none.from.nowhere@gmail.com", "no-one2k@yandex.ru");
//            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, "Ошибка отправки сообщения!", Toast.LENGTH_SHORT).show();
//            }
//
//            return false;
//        }
//    }
}
