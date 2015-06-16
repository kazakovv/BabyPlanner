package com.damianin.babyplanner.UserInterfaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;

import java.util.Date;

public class babySign extends ActionBarActivity {
    protected Toolbar toolbar;
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
    }
}
