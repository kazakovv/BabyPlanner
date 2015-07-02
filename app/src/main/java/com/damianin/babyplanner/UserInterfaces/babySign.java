package com.damianin.babyplanner.UserInterfaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.damianin.babyplanner.Adaptors.AdapterLoveDays;
import com.damianin.babyplanner.Adaptors.AdaptorBabySign;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class babySign extends ActionBarActivity {
    protected Toolbar toolbar;
    protected RecyclerView babySignCards;

    protected int averageLengthOfCycle;
    protected Date firstDayOfCycle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_sign);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        averageLengthOfCycle = getIntent().getIntExtra(Statics.AVERAGE_LENGTH_OF_MENSTRUAL_CYCLE,0);
        firstDayOfCycle = (Date) getIntent().getSerializableExtra(Statics.FIRST_DAY_OF_CYCLE);

        loadCardList(averageLengthOfCycle,firstDayOfCycle);
    }

    public void loadCardList(int averageLengthOfCycle, Date firstDayOfCycle) {

        babySignCards = (RecyclerView) findViewById(R.id.cardList);
        babySignCards.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        babySignCards.setLayoutManager(llm);




        //zarezdame adaptora
        AdaptorBabySign adapter = new AdaptorBabySign(averageLengthOfCycle,firstDayOfCycle, this);
        babySignCards.setAdapter(adapter);
    }

}
