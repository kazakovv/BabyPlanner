package com.damianin.babyplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.damianin.babyplanner.Adaptors.AdapterLoveDays;
import com.damianin.babyplanner.UserInterfaces.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class Main extends ActionBarActivity {

    protected BackendlessUser mCurrentUser;
    protected Toolbar toolbar;
    protected RecyclerView loveDaysCards;

    protected List<BackendlessUser> cardsToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentUser = Backendless.UserService.CurrentUser();


        if (mCurrentUser == null) {
            navigateToLogin();
        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



            loadCardList(mCurrentUser);
        }
    }

    /*
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        loveDaysCards = (RecyclerView) parent.findViewById(R.id.cardList);
        loveDaysCards.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        loveDaysCards.setLayoutManager(llm);

        return super.onCreateView(parent, name, context, attrs);

    }

    */

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

    protected void loadCardList(BackendlessUser currentUser) {

        loveDaysCards = (RecyclerView) findViewById(R.id.cardList);
        loveDaysCards.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        loveDaysCards.setLayoutManager(llm);

        cardsToDisplay = new ArrayList<BackendlessUser>();
        cardsToDisplay.add(currentUser);

        if(currentUser.getProperty(Statics.KEY_PARTNERS) instanceof BackendlessUser[]   ) {
            BackendlessUser[] partners = (BackendlessUser[]) currentUser.getProperty(Statics.KEY_PARTNERS);
            //updatevame cardList i prezarezhdame list

            for(BackendlessUser partner : partners) {
                cardsToDisplay.add(partner);
            }
        }
        //zarezdame adaptora
        AdapterLoveDays adapter = new AdapterLoveDays(cardsToDisplay, this, this);
        loveDaysCards.setAdapter(adapter);
    }

}
