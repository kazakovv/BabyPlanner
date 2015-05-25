package com.damianin.babyplanner.UserInterfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.damianin.babyplanner.DefaultCallback;
import com.damianin.babyplanner.Helper.BackendlessMessage;
import com.damianin.babyplanner.Main;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;
import com.damianin.babyplanner.dialogs.CustomAlertDialog;
import com.damianin.babyplanner.dialogs.WrongPasswordLogin;


public class LoginActivity extends Activity {

    protected TextView signUpClickButton;

    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLoginButton;
    protected static int FORGOT_PASSWORD = 111;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //vrazvame ostanalite TextFields i butona
        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.logInButton);
        //TODO zarezdame emaila na poslednia lognal se potrebitel
        //String emailOfLastLoggedInUser = SharedPrefsHelper.loadEmailOfLastLoggedInUser(LoginActivity.this);
        /*if (emailOfLastLoggedInUser !=null) {
            mEmail.setText(emailOfLastLoggedInUser);
        }
        */
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //razkarvam ako ima intervali v username, passpword i email
                final String userEmail = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(userEmail.isEmpty() || password.isEmpty() ) {
                    String title = getResources().getString(R.string.login_error_title);
                    String message = getResources().getString(R.string.login_error_message);
                    CustomAlertDialog kissDialog = new CustomAlertDialog();
                    Bundle dialogContent = new Bundle();
                    dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                    dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                    kissDialog.setArguments(dialogContent);
                    kissDialog.show(getFragmentManager(),"tag_alert_dialog");
                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(R.string.login_error_title)
                            .setMessage(R.string.login_error_message)
                            .setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                }

                else {
                    //ako username i password ne sa prazni se logvame Backendless

                    BackendlessUser user = new BackendlessUser();
                    user.setEmail(userEmail);
                    user.setPassword(password);

                    String progressSignInMessage = getResources().getString(R.string.progress_sign_in_message);
                    Backendless.UserService.login(userEmail, password,  new DefaultCallback<BackendlessUser>(LoginActivity.this, progressSignInMessage) {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            BackendlessMessage.registerDeviceForPush(backendlessUser);
                            //TODO zapisvame username v shared prefs, za da moze da se zaredi po-lesno sledvashtiat pat
                            //SharedPrefsHelper.saveEmailForLogin(LoginActivity.this, backendlessUser);
                            //User successfully loged in!.Switch to main screen.
                            Intent intent = new Intent(LoginActivity.this, Main.class);
                            //dobaviame flagove, za da ne moze usera da se varne pak kam toya ekran
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            //neuspeshen login
                            //ako ne se izvka dolniat red dialog box varti nerestanno
                            super.handleFault(backendlessFault);
                            String error = backendlessFault.getCode();
                            if(error.equals(Statics.BACKENDLESS_INVALID_LOGIN_OR_PASS_MESSAGE)) {
                                //greshno portrebitelsko ime ili parola
                                WrongPasswordLogin wrongPasswordLogin = new WrongPasswordLogin();
                                wrongPasswordLogin.show(getFragmentManager(),"show");


                            } else {
                                //niakakva greshka sas servera
                                String title = getResources().getString(R.string.login_error_title);
                                String message = getResources().getString(R.string.general_login_error_message);
                                CustomAlertDialog dialogError = new CustomAlertDialog();
                                Bundle dialogContent = new Bundle();
                                dialogContent.putString(Statics.ALERTDIALOG_TITLE, title);
                                dialogContent.putString(Statics.ALERTDIALOG_MESSAGE,message);
                                dialogError.setArguments(dialogContent);
                                dialogError.show(getFragmentManager(),"tag_alert_dialog");

                                /*
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(R.string.login_error_title)
                                        .setMessage(R.string.general_login_error_message)
                                        .setPositiveButton(R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                */
                            }
                        }
                    });
                }
            }
        });



        //onClickListener za signup Activity

        signUpClickButton = (TextView) findViewById(R.id.signUpText);
        signUpClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


    }


}
