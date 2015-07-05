package com.damianin.babyplanner.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.damianin.babyplanner.DefaultCallback;
import com.damianin.babyplanner.Main;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;
import com.damianin.babyplanner.UserInterfaces.SignUpActivity;

import java.util.Date;


/**
 * Created by Victor on 19/10/2014.
 */
public class GuyOrGirlDialog extends DialogFragment implements DialogInterface.OnShowListener {

    BackendlessUser currentUser;
    Context context;

    protected RadioButton mGuy;
    protected RadioButton mGirl;
    private OnCompleteListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        currentUser = Backendless.UserService.CurrentUser();


        context = getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog_male_or_female,null);
        context = inflatedView.getContext();
        mGuy = (RadioButton) inflatedView.findViewById(R.id.radioButtonGuy);
        mGirl =(RadioButton) inflatedView.findViewById(R.id.radioButtonGirl);
        mGuy.setChecked(true);
        mGuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGirl.isChecked()){
                    mGirl.setChecked(false);
                }
            }
        });
        mGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGuy.isChecked()){
                    mGuy.setChecked(false);
                }
            }
        });

        builder.setView(inflatedView)

                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mGuy.isChecked()) {
                            currentUser.setProperty(Statics.KEY_MALE_OR_FEMALE, Statics.SEX_MALE);
                        } else {
                            currentUser.setProperty(Statics.KEY_MALE_OR_FEMALE, Statics.SEX_FEMALE);
                        }

                        //predavame current user na main za refresh na cardlist
                        Main activity = (Main) getActivity();
                        activity.onComplete(currentUser);

                        dialog.dismiss();
                        //mainMessage.setText(R.string.main_message_male);

                        //update v backendless

                        String messageCallback = context.getResources().getString(R.string.saving_preferences_message);

                        Backendless.UserService.update(currentUser, new DefaultCallback<BackendlessUser>(context, messageCallback) {
                            @Override
                            public void handleResponse(BackendlessUser backendlessUser) {
                                super.handleResponse(backendlessUser);
                                //zapisvame lokalno
                                Backendless.UserService.setCurrentUser(currentUser);
                                //TODO refresh cards
                                //((Main)getActivity()).loadCardList(currentUser);

                                Toast.makeText(context,
                                        R.string.selection_saved_successfully, Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                super.handleFault(backendlessFault);
                                String error = backendlessFault.getMessage();
                                Toast.makeText(context,
                                        R.string.selection_not_saved, Toast.LENGTH_LONG).show();

                            }
                        });
                    }//krai na on click ok button

                })//end ok button
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GuyOrGirlDialog.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();

        dialog.setOnShowListener(this);
        return dialog;

    }

    //interface za pass value to SignUp Activity
    public static interface OnCompleteListener {
        public abstract void onComplete(BackendlessUser currentUser);
    }


    //tova promenia cveta na butona kato se klikne na nego
    @Override
    public void onShow(DialogInterface dialog) {

        Button positiveButton = ((AlertDialog) dialog)
                .getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackgroundResource(R.drawable.custom_dialog_button);

        Button negativeButton = ((AlertDialog) dialog)
                .getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundResource(R.drawable.custom_dialog_button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }
}
