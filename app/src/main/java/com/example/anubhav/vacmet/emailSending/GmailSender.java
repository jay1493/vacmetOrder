package com.example.anubhav.vacmet.emailSending;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.anubhav.vacmet.LoginActivity;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by anubhav on 21/1/17.
 */

public class GmailSender extends Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Context context;
    private Session session;

    /**
     * Try with commenting below static block...see if it works...
     */
    static {
        Security.addProvider(new JSSEProvider());
    }

    private ProgressDialog progressDialog;

    public GmailSender(final String user, final String password,Context context) {
        this.user = user;
        this.context = context;
        this.password = password;
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.host",mailhost);
        properties.setProperty("mail.smtp.auth", "true");
        //below is also useful property
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.port", "465");
        //Port to allow smtp socket
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        //Give Exceptions on Transport Send Message(Success/Failed)
        properties.setProperty("mail.smtp.reportsuccess","true");
        //Default class is net.ssl.SSLSocketFactory(used to create smtp ssl sockets), or we can use any class extending this class.
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        //If it is true, then if Socket creation fails, then a new socket is created using java.net.socket class
        properties.setProperty("mail.smtp.socketFactory.fallback", "true");
        //Transport Layer Security(higher ver. then ssl) if enabled, depends on mail host server who is using it
        properties.setProperty("mail.smtp.starttls.enable", "true");
        //If set to false then quit command is sent immediately and connection is closed immediately,
        //if true then transport will wait for the response of quit command
        properties.setProperty("mail.smtp.quitwait", "false");
        //Get Instance makes a newInstance of Session irrespective a session object already exists.
        //Get Default Instance helps in making duplicate instances, here a defaultInstance is returned if it exists,otherwise new Session Object is returned.

     /*   session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user,password);
            }
        });*/
        session = Session.getDefaultInstance(properties);


    }
    public synchronized void sendMail(String subject, String body,String sender, String recipients)
            throws Exception {
        //MimeMessage is a subclass of Message Class(this Message class is Abstract so we use this MimeMsg.)
        final MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteBodyConverter(body.getBytes(),"text/plain"));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if(recipients.indexOf(',')>0){
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
        }
        else {
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recipients));
        }
        //We could use below functions for authenticating if we are not using above Authenticator Callback
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport tr = session.getTransport("smtps");
                    tr.connect(mailhost, 465,user, password);
                    message.saveChanges();
                    tr.sendMessage(message, message.getAllRecipients());
                    tr.close();
                    ((LoginActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Mail Sent...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }catch (Exception e){
                     e.printStackTrace();
                    Log.e("", "run: "+e.toString());
                }
            }
        });
        thread.start();

//        Used Static Method here, as we already created a Session with following properties.

//        Transport.send(message);


    }
}
