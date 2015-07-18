package com.damianin.babyplanner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessBroadcastReceiver;


public class PushReceiver extends BackendlessBroadcastReceiver
{
  @Override
  public boolean onMessage( Context context, Intent intent )
  {
    String tickerText = intent.getStringExtra( PublishOptions.ANDROID_TICKER_TEXT_TAG );
    String contentTitle = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TITLE_TAG );
    String contentText = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TEXT_TAG );
    String messageType = intent.getStringExtra(PublishOptions.MESSAGE_TAG); //tip saobstenie
    String subtopic = intent.getStringExtra( "message" );
    if( tickerText != null && tickerText.length() > 0 )
    {

        createNotification(context, messageType, intent, tickerText,contentTitle, contentText );
    }

    return false;
  }

    // This function will create an intent. This intent must take as parameter the "unique_name" that you registered your activity with
    static void createNotification(Context context, String messageType, Intent intent,
                                   String tickerText, String contentTitle,
                                   String contentText) {
        int id = 0;
        int appIcon = context.getApplicationInfo().icon;

        Intent notificationIntent;

        if (messageType.equals(Statics.TYPE_CALENDAR_UPDATE)) {
            notificationIntent = new Intent(context, Main.class);
            notificationIntent.setFlags(Statics.FLAG_CALENDAR_UPDATE);
            id=3;

        } else if(messageType.equals(Statics.TYPE_PARTNER_REQUEST)){
            notificationIntent = new Intent(context, Main.class);
            notificationIntent.setFlags(Statics.FLAG_PARTNER_REQUEST);
            id=4;
            Statics.pendingPartnerRequest=true;
            //send broadcast
            Intent showAddPartnerIcon = new Intent(Statics.FLAG_INTENT_ADD_PARTNER);
            context.sendBroadcast(showAddPartnerIcon);
        }

        else {
            notificationIntent = new Intent(context, Main.class);

        }


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( context );
        notificationBuilder.setSmallIcon( R.mipmap.launcher_icon );
        notificationBuilder.setTicker(tickerText);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setContentTitle(contentTitle);
        notificationBuilder.setContentText(contentText);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( id, notification );


        //!!!!!!!!!!!!!!!!!!!!!! TOVA RABOTI SAMO, AKO E OTVORENA PROGRAMATA. TOGAVA SE UPDATEVA LOVE BOX
        Intent intentRefresh = new Intent(Statics.KEY_REFRESH_FRAGMENT_LOVE_BOX);

        //put whatever data you want to send, if any
        //intent.putExtra("message", message);

        //send broadcast
        context.sendBroadcast(intentRefresh);
    }
}
                                            