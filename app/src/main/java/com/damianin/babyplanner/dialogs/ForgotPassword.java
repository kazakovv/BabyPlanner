package com.damianin.babyplanner.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;

public class ForgotPassword extends DialogFragment implements DialogInterface.OnShowListener {
    protected BackendlessUser mCurrentUser;
    protected Context mContext;
    protected EditText mEmailForPasswordRecovery;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(Backendless.UserService.CurrentUser() != null) {
            mCurrentUser = Backendless.UserService.CurrentUser();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog_forgot_password,null);
        mContext = inflatedView.getContext();
        mEmailForPasswordRecovery = (EditText) inflatedView.findViewById(R.id.emailForPasswordRecovery);

        builder.setView(inflatedView)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int id) {
                        String email = mEmailForPasswordRecovery.getText().toString().trim();
                        if(!email.isEmpty()) {
                            //izprashtame email s password recovery
                            Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                                @Override
                                public void handleResponse(Void aVoid) {

                                    Toast.makeText(mContext,R.string.recovery_email_sent_dialog,Toast.LENGTH_LONG).show();


                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {

                                    String error = backendlessFault.getCode();
                                    //niama nameren takav email
                                    if(error.equals(Statics.BACKENDLESS_INVALID_EMAIL_PASSWORD_RECOVERY)) {
                                        String s = backendlessFault.getMessage();
                                        Toast.makeText(mContext,R.string.email_not_found,Toast.LENGTH_LONG).show();

                                        /*
                                        String title = getResources().getString(R.string.general_error_title);
                                        String message = getResources().getString(R.string.email_not_found);
                                        CustomAlertDialog dialogError = new CustomAlertDialog();
                                        Bundle dialogContent = new Bundle();
                                        dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                        dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                                        dialogError.setArguments(dialogContent);
                                        dialogError.show(getActivity().getFragmentManager(),"tag_alert_dialog");
                                        /*
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle(R.string.general_error_title)
                                                .setMessage(R.string.email_not_found)
                                                .setPositiveButton(R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        */
                                    } else {
                                        //niakakva druga greshka
                                        Toast.makeText(mContext,R.string.general_server_error,Toast.LENGTH_LONG).show();
                                        //Toast.makeText(mContext,R.string.general_server_error,Toast.LENGTH_LONG).show();
                                        /*
                                        String title = getResources().getString(R.string.general_error_title);
                                        String message = getResources().getString(R.string.general_server_error);
                                        CustomAlertDialog dialogError = new CustomAlertDialog();
                                        Bundle dialogContent = new Bundle();
                                        dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                        dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                                        dialogError.setArguments(dialogContent);
                                        dialogError.show(getActivity().getFragmentManager(),"tag_alert_dialog");

                                        /*
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setTitle(R.string.general_error_title)
                                                .setMessage(R.string.general_server_error)
                                                .setPositiveButton(R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        */
                                    }
                                }
                            });
                        }
                    }//krai na on click ok button

                })//end ok button
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ForgotPassword.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;

    }

    //tova promenia cveta na butona kato se klikne na nego
    @Override
    public void onShow(DialogInterface dialog) {

        Button positiveButton = ((AlertDialog) dialog)
                .getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackgroundResource(R.drawable.custom_dialog_button);
        positiveButton.setTextColor(getResources().getColor(R.color.color_black));

        Button negativeButton = ((AlertDialog) dialog)
                .getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundResource(R.drawable.custom_dialog_button);
        negativeButton.setTextColor(getResources().getColor(R.color.color_black));

    }
}
