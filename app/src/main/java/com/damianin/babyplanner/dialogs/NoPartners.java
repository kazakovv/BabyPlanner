package com.damianin.babyplanner.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.damianin.babyplanner.R;
import com.damianin.babyplanner.UserInterfaces.ManagePartnersMain;


/**
 * Created by Victor on 24/03/2015.
 */
public class NoPartners extends DialogFragment implements DialogInterface.OnShowListener {
    TextView timeLeft;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.dialog_no_partners, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflatedView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoPartners.this.getDialog().cancel();
                        Intent intent = new Intent(getActivity(), ManagePartnersMain.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoPartners.this.getDialog().cancel();
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

        Button negativeButton = ((AlertDialog) dialog)
                .getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setBackgroundResource(R.drawable.custom_dialog_button);
    }
}