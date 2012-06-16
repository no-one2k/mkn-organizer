package test.andro.mail;

import java.security.Security;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderClass extends javax.mail.Authenticator {

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private Multipart _multipart;

    public void setSetcurityProvider (){
        Security.addProvider(new JSSEProvider());
    }

    public MailSenderClass(String user, String password) {
        this.user = user;
        this.password = password;

        _multipart = new MimeMultipart();

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipient) throws Exception {
        //try {
            MimeMessage message = new MimeMessage(session);

            // ���
            message.setSender(new InternetAddress(sender));
            // � ���
            message.setSubject(subject);
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);


            message.setContent(_multipart);

            Transport.send(message);
//        } catch (Exception e) {
//            Log.e("sendMail", "fail sendMail! ");
//        }
    }
}
