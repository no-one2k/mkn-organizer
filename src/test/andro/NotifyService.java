package test.andro;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author noone
 */
public class NotifyService extends Service {

    public static final int TEST_CODE = 1;
    public static final int START_THREAD = 2;
    public static final int STOP_THREAD = 3;
    final Handler uiHandler = new Handler();
    // объект для обработки команд от Activity и посылки ответа
    private Messenger mMessenger = new Messenger(new Handler() {
        private Timer t;

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
                    case TEST_CODE:
                        if (t==null){
                        t = new Timer("tst");
                        t.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                uiHandler.post(new Runnable() {

                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "dzinn", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }, 3000);
                        }else{
                            t.cancel();
                            t=null;
                        }

                        break;
                    default:
                        super.handleMessage(msg);
                }
            } catch (Exception e) {
            }
        }
    });

    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}