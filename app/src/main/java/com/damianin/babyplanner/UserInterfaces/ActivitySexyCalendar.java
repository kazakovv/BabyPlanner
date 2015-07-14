package com.damianin.babyplanner.UserInterfaces;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;
import com.roomorama.caldroid.CaldroidFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivitySexyCalendar extends ActionBarActivity {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    protected Toolbar toolbar;

    protected BackendlessUser mCurrentUser;
    //passed on from AdapterLoveDays
    protected Date firstDayOfCycle;
    protected int averageCycleLength;
    protected String emailToSearch;

    protected RecyclerView listWithCalendar;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();


            //caldroidFragment.setBackgroundResourceForDate(R.color.apptheme_color,blueDate);

            //Load colors for different dates linked to cycle into calendar
            calendarChangeColors(firstDayOfCycle, averageCycleLength);

            //caldroidFragment.setTextColorForDate(R.color.color_white, blueDate);
            //caldroidFragment.setTextColorForDate(R.color.color_white, greenDate);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sexy_calendar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_action_back); //white arrow
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentUser = Backendless.UserService.CurrentUser();
        emailToSearch = getIntent().getStringExtra(Statics.KEY_EMAIL_CALENDAR);
        firstDayOfCycle = (Date) getIntent().getSerializableExtra(Statics.FIRST_DAY_OF_CYCLE);
        averageCycleLength = getIntent().getIntExtra(Statics.AVERAGE_LENGTH_OF_MENSTRUAL_CYCLE,0);



        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
            args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday


            // Uncomment this line to use dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefault);

            caldroidFragment.setArguments(args);



        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();


    }

    protected void calendarChangeColors(Date firstDayOfCycle, int averageLengthOfCycle){
        Calendar now = Calendar.getInstance();
        Calendar cycleFirstDay  = Calendar.getInstance();
        cycleFirstDay.setTime(firstDayOfCycle);
        long difference = now.getTimeInMillis() - cycleFirstDay.getTimeInMillis();

        final int days = (int) (difference / (24 * 60 * 60 * 1000));
        final int firstDayOfOvulation = averageLengthOfCycle - 14;
        final int lastDayOfOvulation = averageLengthOfCycle - 10;

        int daysUntilEndOfCycle = averageLengthOfCycle - days;

        Calendar dateToChangeColor = Calendar.getInstance();
        int dayToLookup = days;
        for (int i = 0; i <= daysUntilEndOfCycle; i++ ) {
            colorsForDates(dayToLookup,firstDayOfOvulation,lastDayOfOvulation,averageLengthOfCycle,dateToChangeColor.getTime());
            dayToLookup +=1;
            dateToChangeColor.add(Calendar.DAY_OF_MONTH,1);
        }
    }

    protected void colorsForDates(int days, int firstDayOfOvulation, int lastDayOfOvulation,
                                  int averageLengthOfCycle, Date dateToChangeColor ) {

        if (days >= 0 && days <= 4) {
            //bleeding
            caldroidFragment.setBackgroundResourceForDate(R.color.calendar_bleeding,
                    dateToChangeColor);
        } else if (days > 4 && days < firstDayOfOvulation) {
            //folicurar phase
            // active energetic
            caldroidFragment.setBackgroundResourceForDate(R.color.calendar_folicurar,
                    dateToChangeColor);
        } else if (days >= firstDayOfOvulation && days < lastDayOfOvulation) {
            //ovulation
            //sexy
            caldroidFragment.setBackgroundResourceForDate(R.color.calendar_ovulation,
                    dateToChangeColor);
        } else if (days >= lastDayOfOvulation && days <= averageLengthOfCycle) {
            //luteal
            caldroidFragment.setBackgroundResourceForDate(R.color.calendar_luteal,
                    dateToChangeColor);
        }
        caldroidFragment.setTextColorForDate(R.color.color_white,dateToChangeColor);
    }



}
