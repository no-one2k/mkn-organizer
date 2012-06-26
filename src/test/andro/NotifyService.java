package test.andro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import test.andro.mail.MailSenderClass;

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
            }, getPeriod(firstTask));
        }
    }

    private long getPeriod(Task t) {
        Date now = Calendar.getInstance().getTime();
        Date start = t.getStartDate();
        long diff = start.getTime() - now.getTime();
        return 1000;// (Math.abs(diff) / 1000) * 1000;
    }

    private void prepareTimer() {
        if (timer == null) {
            timer = new Timer("tst");
        } else {
            timer.cancel();
        }
    }

    private void doNotify() {
        String text = (firstTask == null) ? "dzinn" : "Service:" + firstTask.getName();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        notifyToBar(text);
        if (sendMail) {
            doSendMail(text);
        }
        if (sendVK) {
            doSendVK();
        }
    }

    private void readPreferences() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.settings_name), 0);
        sendMail = sp.getBoolean(getString(R.string.send_mail), false);
        sendVK = sp.getBoolean(getString(R.string.send_vk), false);
        email = sp.getString(getString(R.string.e_mail), "");
        vkLogin = sp.getString(getString(R.string.vk_login), "");
        vkPass = sp.getString(getString(R.string.vk_pass), "");
        period = Integer.valueOf(sp.getString(getString(R.string.period), "1"));
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

    private void doSendMail(String text) {
         try {
            MailSenderClass sender = new MailSenderClass("none.from.nowhere@gmail.com", "ytpme2kEmpty");
            sender.setSetcurityProvider();
            sender.sendMail("Organizer", text, "none.from.nowhere@gmail.com", email);

            //                sender_mail_async async_sending = new sender_mail_async();
            //                async_sending.execute();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Ошибка отправки сообщения!", Toast.LENGTH_SHORT).show();
            Logger.getLogger(ExtendedMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doSendVK() {
    }
    
    private static final int HELLO_ID = 1;

    private void notifyToBar(String text) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        Notification notification = new Notification(android.R.drawable.ic_dialog_email, text, System.currentTimeMillis());
        Context context = getApplicationContext();
        CharSequence contentTitle = "Organizer";
        CharSequence contentText = text;
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        

        mNotificationManager.notify(HELLO_ID, notification);
    }
}