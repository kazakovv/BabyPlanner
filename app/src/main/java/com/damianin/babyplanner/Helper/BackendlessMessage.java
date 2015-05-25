package com.damianin.babyplanner.Helper;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushPolicyEnum;
import com.backendless.persistence.BackendlessDataQuery;
import com.damianin.babyplanner.BackendlessClasses.Messages;
import com.damianin.babyplanner.Keys;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Victor on 15/02/2015.
 */
public class BackendlessMessage {



    /*
        IZPRASHTANE NA PUSH MESSAGES
         */
    public static void sendPush(BackendlessUser sender, BackendlessUser recipient, Messages message,  final Context context, String TYPE_MESSAGE) {

        String deviceID = null;
        String senderUsername = (String) sender.getProperty(Statics.KEY_USERNAME);
        String channel =  recipient.getEmail();
        if(recipient.getProperty(Statics.KEY_DEVICE_ID) != null) {
            deviceID = (String) recipient.getProperty(Statics.KEY_DEVICE_ID);

        }

        String messagePush="";
        String messageType="";

        if(TYPE_MESSAGE.equals(Statics.TYPE_CALENDAR_UPDATE)) {
            messagePush = senderUsername + " " +  context.getResources().getString(R.string.push_calendar_update);
            messageType = Statics.TYPE_CALENDAR_UPDATE;
        }  else if( TYPE_MESSAGE.equals(Statics.KEY_PARTNER_REQUEST)) {
            messagePush = senderUsername + " " + context.getResources().getString(R.string.new_partner_request_push);
            messageType = Statics.KEY_PARTNER_REQUEST;
        } else if( TYPE_MESSAGE.equals(Statics.KEY_PARTNER_REQUEST_APPROVED)) {
            messagePush = senderUsername + " " + context.getResources().getString(R.string.partner_request_approved);
            messageType = Statics.KEY_PARTNER_REQUEST_APPROVED;

        } else if( TYPE_MESSAGE.equals(Statics.KEY_UPDATE_SEXY_STATUS)) {
            //zadavame saobshtenieto
            if(sender.getProperty(Statics.KEY_MALE_OR_FEMALE).equals(Statics.SEX_FEMALE)){
                messagePush = sender.getProperty(Statics.KEY_USERNAME) + " " +
                        context.getString(R.string.push_sexy_status_updated_female);
            } else {
                messagePush = sender.getProperty(Statics.KEY_USERNAME) + " " +
                        context.getString(R.string.push_sexy_status_updated_male);
            }
            messageType = Statics.KEY_UPDATE_SEXY_STATUS;
        }
        PublishOptions publishOptions = new PublishOptions();

        publishOptions.putHeader(PublishOptions.ANDROID_TICKER_TEXT_TAG, messagePush);
        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TITLE_TAG, context.getResources().getString(R.string.app_name));
        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TEXT_TAG, messagePush);
        publishOptions.putHeader(PublishOptions.MESSAGE_TAG,messageType);

        //dobaviame info za message. Tova se pravi, za da moze kato caknem na push notificationa da se otvori
        if(message != null){
            //TODO eventualno da se otori neshto
            /*
            if(TYPE_MESSAGE.equals(Statics.TYPE_TEXTMESSAGE)) {
                publishOptions.putHeader(Statics.KEY_LOVE_MESSAGE, message.getLoveMessage());
                publishOptions.putHeader(Statics.KEY_USERNAME_SENDER, message.getSenderUsername());
                publishOptions.putHeader(Statics.KEY_MESSAGE_ID, message.getObjectId());

            } else if(TYPE_MESSAGE.equals(Statics.TYPE_IMAGE_MESSAGE)) {
                publishOptions.putHeader(Statics.KEY_URL, message.getMediaUrl());
                publishOptions.putHeader(Statics.KEY_LOVE_MESSAGE, message.getLoveMessage());
                publishOptions.putHeader(Statics.KEY_USERNAME_SENDER, message.getSenderUsername());
                publishOptions.putHeader(Statics.KEY_MESSAGE_ID, message.getObjectId());

            } else if(TYPE_MESSAGE.equals(Statics.TYPE_KISS)){
                publishOptions.putHeader(Statics.KEY_LOVE_MESSAGE, message.getLoveMessage());
                publishOptions.putHeader(Statics.KEY_USERNAME_SENDER, message.getSenderUsername());
                publishOptions.putHeader(Statics.KEY_NUMBER_OF_KISSES, String.valueOf(message.getKissNumber()));
            }
            */
        }


        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.setPushPolicy(PushPolicyEnum.ONLY);
        if(deviceID !=null) {
            deliveryOptions.addPushSinglecast(deviceID);


            Backendless.Messaging.publish(channel, TYPE_MESSAGE, publishOptions, deliveryOptions, new AsyncCallback<MessageStatus>() {
                @Override
                public void handleResponse(MessageStatus messageStatus) {

                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {

                }
            });
        }//krai na check dali deviceId e null
    }//krai na send push

    //namirane na backendless user v spisaka na partniorite na tekushtia potrebitel po email

    public static BackendlessUser findBackendlessUserByEmail(BackendlessUser currentUser, String emailOfPartner) {
        BackendlessUser recipientBackendlessUser = null;
        //probvame da namerim poluchatelia po emaila v spisaka na partniorite na currentUser
        if(currentUser.getProperty(Statics.KEY_PARTNERS) instanceof BackendlessUser[]) {
            BackendlessUser[] partners = (BackendlessUser[]) currentUser.getProperty(Statics.KEY_PARTNERS);
            for(BackendlessUser partner: partners) {
                if(partner.getEmail().equals(emailOfPartner)) {
                    //namirame recepient kato Backendless user v spisakat ot partniori
                    recipientBackendlessUser = partner;
                }
            }

        }
        return recipientBackendlessUser;
    }

    public static void registerDeviceForPush(final BackendlessUser currentUser){

        final String channel = currentUser.getEmail();


        Backendless.Messaging.registerDevice(Keys.googleProjectNumber, channel, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                //Get registration and re-register....
                Backendless.Messaging.getRegistrations(new AsyncCallback<DeviceRegistration>() {
                    @Override
                    public void handleResponse(final DeviceRegistration deviceRegistration) {
                        String token = deviceRegistration.getDeviceToken();
                        List<String> channels = new ArrayList<String>();
                        channels.add(channel);
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.YEAR, 10);
                        Date expiration = c.getTime();
                        Backendless.Messaging.registerDeviceOnServer(token, channels, expiration.getTime(), new AsyncCallback<String>() {
                            @Override
                            public void handleResponse(String s) {

                                currentUser.setProperty(Statics.KEY_DEVICE_ID,deviceRegistration.getDeviceId());


                                Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser backendlessUser) {
                                        Log.d("Vic","good");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {
                                        Log.d("Vic","good");

                                    }
                                });
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Log.d("Vic","good");

                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        //TODO handle fault
                        Log.d("Vic","good");

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //TODO handle fault
                String s = backendlessFault.toString();
                Log.d("Vic","good");

            }
        });

    }

    public static void findMessageAndSetDateOpened(String messageID){
        String whereClause = "objectId='" + messageID +"'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);

        //find message
        Backendless.Data.of(Messages.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Messages>>() {
            @Override
            public void handleResponse(BackendlessCollection<Messages> message) {

                if(message.getCurrentPage().size() > 0) {
                    //namereno e saobshtenieto
                    //updatevame koga e otvoreno

                    Messages messageToUpdate = message.getCurrentPage().get(0);//tr da ima samo 1 saobstehnie
                    if(messageToUpdate.getOpened() == null) { }
                    //ne e zadadeno koga e bilo otvoreno
                    Calendar c = Calendar.getInstance();
                    messageToUpdate.setOpened(c.getTime());

                    //zapazvame go na servera
                    Backendless.Data.of(Messages.class).save(messageToUpdate, new AsyncCallback<Messages>() {
                        @Override
                        public void handleResponse(Messages messages) {

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });

                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //nishto ne moze da se napravi,
            }
        });

    }
}
