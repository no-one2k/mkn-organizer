package test.andro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import test.andro.mail.Mail;
import test.andro.mail.MailSenderClass;

public class ExtendedMail extends Activity {

    int SELECTION = 3;
    Context mainContext;
    String title;
    String text;
    String from;
    String where;
    String attach;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extended_mail);

        mainContext = this;
        attach = "";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ((Button) findViewById(R.id.screen_sendnews_btn_send)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                send();
            }
        });
    }
    
    private void send3(){
        MessagingException messagingException = new javax.mail.MessagingException();
    }

    private void send() {
        try {
            MailSenderClass sender = new MailSenderClass("none.from.nowhere@gmail.com", "ytpme2kEmpty");
            sender.setSetcurityProvider();
            sender.sendMail("ta-dam", "important message", "none.from.nowhere@gmail.com", "no-one2k@yandex.ru");

            //                sender_mail_async async_sending = new sender_mail_async();
            //                async_sending.execute();
        } catch (Exception ex) {
            Toast.makeText(mainContext, "Ошибка отправки сообщения!", Toast.LENGTH_SHORT).show();
            Logger.getLogger(ExtendedMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void send2() {
        Mail m = new Mail("none.from.nowhere@gmail.com", "ytpme2kEmpty");

        String[] toArr = {"no-one2k@yandex.ru"};
        m.setTo(toArr);
        m.setFrom("no-one2k@yandex.ru");
        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
        m.setBody("Email body.");

        try {
            if (m.send()) {
                Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
            Log.e("MailApp", "Could not send email", e);
        }
    }

    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {

        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(ExtendedMail.this, "Отправка данных", "Отправляем сообщение...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(mainContext, "Отправка завершена!!!", Toast.LENGTH_LONG).show();
            ((Activity) mainContext).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                title = ((EditText) findViewById(R.id.screen_sendnews_et_title)).getText().toString();
                text = ((EditText) findViewById(R.id.screen_sendnews_et_text)).getText().toString();

                from = "from_post_msg@gmail.com";
                where = "where_post_msg@yandex.ru";

            } catch (Exception e) {
                Toast.makeText(mainContext, "Ошибка отправки сообщения!", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }
}