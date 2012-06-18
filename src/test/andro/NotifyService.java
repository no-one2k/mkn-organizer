package test.andro;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author noone
 */
public class NotifyService extends Service {

    public static final int REFRESH_CODE = 1;
    public static final int START_THREAD = 2;
    public static final int STOP_THREAD = 3;
    private Timer timer;
    final Handler uiHandler = new Handler();
    // объект для обработки команд от Activity и посылки ответа
    private Messenger mMessenger = new Messenger(new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case START_THREAD:
                        // start background thread
                        break;
                    case STOP_THREAD:
                        // stop background thread
                        break;
                    case REFRESH_CODE:
                        onRefreshAccepted();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            } catch (Exception e) {
            }
        }
    });
    private SqlTasksAdapter adapter;
    private Task firstTask;
    private boolean sendMail;
    private boolean sendVK;
    private String email;
    private String vkLogin;
    private String vkPass;
    private int period;

    private void onRefreshAccepted() {
        readPreferences();
        prepareTimer();
        firstTask = adapter.getFirstTask();
        if (firstTask != null) {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    uiHandler.post(new Runnable() {

                        public void run() {
                            doNotify();
                        }
                    });

                }
            }, getPeriod (firstTask));
        }
    }
    
    private long getPeriod (Task t){
        Date now=Calendar.getInstance().getTime();
        Date start=t.getStartDate();
        long diff=start.getTime()-now.getTime();
        return 1000;// Math.abs(diff);
    }

    private void prepareTimer() {
        if (timer == null) {
            timer = new Timer("tst");
        } else {
            timer.cancel();
        }
    }

    private void doNotify() {
        Toast.makeText(getApplicationContext(), (firstTask==null)?"dzinn":firstTask.getName(), Toast.LENGTH_LONG).show();
        if (sendMail){
            doSendMail ();
        }
        if (sendVK){
            doSendVK ();
        }
    }
    
    private void readPreferences (){
        SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.settings_name), 0);
        sendMail=sp.getBoolean(getString(R.string.send_mail), false);
        sendVK=sp.getBoolean(getString(R.string.send_vk), false);
        email=sp.getString(getString(R.string.e_mail), "");
        vkLogin=sp.getString(getString(R.string.vk_login),"");
        vkPass=sp.getString(getString(R.string.vk_pass),"");
        period=Integer.valueOf(sp.getString(getString(R.string.period), "1"));
    }

    public IBinder onBind(Intent intent) {
        adapter = SqlTasksAdapter.getInstance(getApplicationContext());
        SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.settings_name), 0);
        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {

            public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
                onRefreshAccepted();
            }
        });
        return mMessenger.getBinder();
    }

    private void doSendMail() {
        
    }

    private void doSendVK() {
        
    }
}