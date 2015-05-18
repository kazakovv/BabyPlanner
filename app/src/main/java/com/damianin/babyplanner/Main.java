package com.damianin.babyplanner;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;


public class Main extends ActionBarActivity {

    protected BackendlessUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentUser = Backendless.UserService.CurrentUser();

        if (mCurrentUser == null) {
            navigateToLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void navigateToLogin() {
        //preprashta kam login screen
        Intent intent = new Intent(this, LoginActivity.class);

        //Celta na sledvashtite 2 reda e da ne moze da otidesh ot log-in ekrana
        //kam osnovnia ekran, ako natisnesh back butona

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //sazdavo zadacha
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //iztriva vsichki predishni zadachi.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
