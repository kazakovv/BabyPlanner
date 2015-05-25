package com.damianin.babyplanner.UserInterfaces;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.damianin.babyplanner.DefaultCallback;
import com.damianin.babyplanner.Helper.BackendlessMessage;
import com.damianin.babyplanner.Main;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;
import com.damianin.babyplanner.dialogs.CustomAlertDialog;
import com.damianin.babyplanner.dialogs.SetBirthdaySignUp;

import java.util.Date;


public class SignUpActivity extends FragmentActivity implements SetBirthdaySignUp.OnCompleteListener {
    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;
    protected Spinner spinner;

    //promenlivi za stoinostite ot gornite EditText
    protected String userName;
    protected String password;
    protected String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Vrazvame opciite za spinner
        spinner = (Spinner) findViewById(R.id.spinnerMaleOrFemale);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //vrazvame ostanalite TextFields i butona
        mUserName = (EditText) findViewById(R.id.sign_up_username);
        mPassword = (EditText) findViewById(R.id.sign_up_password);
        mEmail = (EditText) findViewById(R.id.sign_up_email);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //razkarvam ako ima intervali v username, passpword i email
                userName = mUserName.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                email = mEmail.getText().toString().trim();

                if (userName.isEmpty() || password.isEmpty() || email.isEmpty()) {

                    String title = getResources().getString(R.string.sign_up_error_title);
                    String message = getResources().getString(R.string.sign_up_error_message);
                    CustomAlertDialog dialogError = new CustomAlertDialog();
                    Bundle dialogContent = new Bundle();
                    dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                    dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                    dialogError.setArguments(dialogContent);
                    dialogError.show(getFragmentManager(),"tag_alert_dialog");


                } else {
                    //ako username, password, email ne sa prazni gi zapisvame v backendless

                    //parvo proveriavame dali ima veche user sas sashtia email
                    String whereClause = "email='" + email + "'";
                    BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                    dataQuery.setWhereClause(whereClause);

                    String checkEmailMessage = getResources().getString(R.string.check_email_sign_up_message);
                    Backendless.Data.of(BackendlessUser.class).find(dataQuery,
                            new DefaultCallback<BackendlessCollection<BackendlessUser>>(SignUpActivity.this, checkEmailMessage) {
                                @Override
                                public void handleResponse(BackendlessCollection<BackendlessUser> user) {
                                    super.handleResponse(user);
                                    if (user.getCurrentPage().size() > 0) {
                                        //veche ima user registriran s toya email
                                        String title = getResources().getString(R.string.sign_up_error_title);
                                        String message = getResources().getString(R.string.email_in_use);
                                        CustomAlertDialog dialogError = new CustomAlertDialog();
                                        Bundle dialogContent = new Bundle();
                                        dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                        dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                                        dialogError.setArguments(dialogContent);
                                        dialogError.show(getFragmentManager(),"tag_alert_dialog");

                                        /*
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                        builder.setTitle(R.string.sign_up_error_title)
                                                .setMessage(R.string.email_in_use)
                                                .setPositiveButton(R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        */
                                    } else {

                                        SetBirthdaySignUp birthdaySignUp = new SetBirthdaySignUp();
                                        birthdaySignUp.show(getSupportFragmentManager(),"Welcome");
                                        //sled kato se izbere Rozhden den, prodalzavame natatak s registraciata
                                        //Kodat za tova e v onClomplete

                                    }
                                }//krai na uspeshna parva query dali ima veche registriran takav email

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    //greshka na parvata query dali ima takav email
                                    super.handleFault(backendlessFault);
                                    String error = backendlessFault.toString();
                                    String title = getResources().getString(R.string.sign_up_error_title);
                                    String message = getResources().getString(R.string.error_unable_to_sign_in);
                                    CustomAlertDialog dialogError = new CustomAlertDialog();
                                    Bundle dialogContent = new Bundle();
                                    dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                    dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                                    dialogError.setArguments(dialogContent);
                                    dialogError.show(getFragmentManager(),"tag_alert_dialog");

                                    /*
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                    builder.setTitle(R.string.sign_up_error_title)
                                            .setMessage(R.string.error_unable_to_sign_in)
                                            .setPositiveButton(R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    */
                                }
                            });//krai na check if email is already in use

                }//krai na zapisvane v backendless
            }
        });//krai na sign up button on click listener

    }

    //vrashtame date of Birth i prodalzhavame sign up procedure
    @Override
    public void onComplete(Date dateOfBirth) {
        //niama takav registriran user. Prodalzhavame natatak
        BackendlessUser  newUser = new BackendlessUser();
        newUser.setProperty(Statics.KEY_DATE_OF_BIRTH, dateOfBirth);

        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setProperty(Statics.KEY_USERNAME, userName);
        newUser.setProperty(Statics.KEY_MALE_OR_FEMALE, spinner.getSelectedItem().toString());
        //zadavame rozdenia den


        final String message = getResources().getString(R.string.signing_in_message);
        Backendless.UserService.register(newUser,
                new DefaultCallback<BackendlessUser>(SignUpActivity.this, message) {

                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        BackendlessMessage.registerDeviceForPush(backendlessUser);
                        //!!!!!!!!!!!!!!!!!!
                        //User successfully created!
                        //TODO zapisvame emaila v shared preferences, za da moze posle da se logvame po-lesno
                        //SharedPrefsHelper.saveEmailForLogin(SignUpActivity.this, backendlessUser);
                        //log in!
                        Backendless.UserService.login(email, password,
                                new DefaultCallback<BackendlessUser>(SignUpActivity.this, message) {
                                    @Override
                                    public void handleResponse(BackendlessUser backendlessUser) {
                                        super.handleResponse(backendlessUser);
                                        //TODO registrirame user za push
                                        //BackendlessMessage.registerDeviceForPush(backendlessUser);
                                        // Switch to main screen.
                                        Intent intent = new Intent(SignUpActivity.this, Main.class);
                                        //dobaviame flagove, za da ne moze usera da se varne pak kam toya ekran
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {
                                        super.handleFault(backendlessFault);

                                        String title = getResources().getString(R.string.sign_up_error_title);
                                        String message = getResources().getString(R.string.error_user_registered_but_unable_to_log_in);
                                        CustomAlertDialog dialogError = new CustomAlertDialog();
                                        Bundle dialogContent = new Bundle();
                                        dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                        dialogContent.putString(Statics.ALERTDIALOG_MESSAGE, message);
                                        dialogError.setArguments(dialogContent);
                                        dialogError.show(getFragmentManager(), "tag_alert_dialog");

                                         /*

                                         setProgressBarIndeterminateVisibility(false);
                                         AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                         builder.setTitle(R.string.sign_up_error_title)
                                                 .setMessage(R.string.error_user_registered_but_unable_to_log_in)
                                                 .setPositiveButton(R.string.ok, null);
                                         AlertDialog dialog = builder.create();
                                         dialog.show();
                                         */
                                    }
                                });
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        super.handleFault(backendlessFault);
                        String title = getResources().getString(R.string.sign_up_error_title);
                        String message = getResources().getString(R.string.error_unable_to_sign_in);
                        CustomAlertDialog dialogError = new CustomAlertDialog();
                        Bundle dialogContent = new Bundle();
                        dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                        dialogContent.putString(Statics.ALERTDIALOG_MESSAGE, message);
                        dialogError.setArguments(dialogContent);
                        dialogError.show(getFragmentManager(), "tag_alert_dialog");

                         /*
                         AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                         builder.setTitle(R.string.sign_up_error_title)
                                 .setMessage(R.string.error_unable_to_sign_in)
                                 .setPositiveButton(R.string.ok, null);
                         AlertDialog dialog = builder.create();
                         dialog.show();
                         */
                    }
                });

    }




}
