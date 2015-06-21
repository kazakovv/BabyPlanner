package com.damianin.babyplanner.UserInterfaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class babySign extends ActionBarActivity {
    protected Toolbar toolbar;
    protected int averageLengthOfCycle;
    protected Date firstDayOfCycle;
    protected Button nextDateForConceiving;
    protected TextView nextDateGivingBirth;
    protected TextView nextConceivingSign;

    Calendar dateBorn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_sign);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        averageLengthOfCycle = getIntent().getIntExtra(Statics.AVERAGE_LENGTH_OF_MENSTRUAL_CYCLE,0);
        firstDayOfCycle = (Date) getIntent().getSerializableExtra(Statics.FIRST_DAY_OF_CYCLE);
        nextDateForConceiving = (Button) findViewById(R.id.buttonNextConceivingDate);
        nextDateForConceiving.setOnClickListener(buttonNextConceivingDate);

        nextDateGivingBirth = (TextView) findViewById(R.id.textViewNextConceivingDate);
        nextConceivingSign = (TextView) findViewById(R.id.textViewNextConceivingSign);

        calculateDateBorn();
    }

    protected Calendar calculateFirstDayOfOvulation(){
        Calendar firstDayOfOvulation  = Calendar.getInstance();
        firstDayOfOvulation.setTime(firstDayOfCycle);
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH, averageLengthOfCycle - 14);
        return firstDayOfOvulation;
    }

    private View.OnClickListener buttonNextConceivingDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //we add 48 weeks every time the button is pressed
            dateBorn.add(Calendar.WEEK_OF_YEAR, 48);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String nextDateToGiveBirth;
            nextDateToGiveBirth = formatter.format(dateBorn.getTime());
            nextDateGivingBirth.setText(nextDateToGiveBirth);
            nextConceivingSign.setText(returnZodiacSign(dateBorn));
        }
    };

    protected void calculateDateBorn(){
        TextView firstDate = (TextView) findViewById(R.id.firstDate);
        TextView secondtDate = (TextView) findViewById(R.id.secondDate);
        TextView thirdDate = (TextView) findViewById(R.id.thirdDate);
        TextView fourthDate = (TextView) findViewById(R.id.fourthDate);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String dateToDisplay;

        dateBorn = Calendar.getInstance();
        dateBorn = calculateFirstDayOfOvulation();
        dateBorn.add(Calendar.WEEK_OF_YEAR, 48); //add 48 weeks since pregnancy
        dateToDisplay = formatter.format(dateBorn.getTime());
        firstDate.setText(dateToDisplay);

        dateBorn.add(Calendar.DAY_OF_MONTH, 1);
        dateToDisplay = formatter.format(dateBorn.getTime());
        secondtDate.setText(dateToDisplay);

        dateBorn.add(Calendar.DAY_OF_MONTH, 1);
        dateToDisplay = formatter.format(dateBorn.getTime());
        thirdDate.setText( dateToDisplay);

        dateBorn.add(Calendar.DAY_OF_MONTH, 1);
        dateToDisplay = formatter.format(dateBorn.getTime());
        fourthDate.setText( dateToDisplay);

        //test
        String[] signs = new String[4];
        for(int i = 0; i<4 ; i++){
            dateBorn.add(Calendar.DAY_OF_MONTH,-i);
            signs[i] = returnZodiacSign(dateBorn);
        }
        String babySign = signs[0];
        String alternativeBabySign= signs[3];

        thirdDate.setText(babySign);
        fourthDate.setText(babySign);
    }

    protected String returnZodiacSign(Calendar dateOfBirth){
        String[] zodiacSigns = new String[]{
                        "Capricorn","Aquarius","Pisces","Aries","Taurus","Gemini",
                        "Cancer","Leo","Virgo","Libra",
                        "Scorpio","Sagittarius","none"
                };

        int month = dateOfBirth.get(Calendar.MONTH);
        int day = dateOfBirth.get(Calendar.DAY_OF_MONTH);

        // Capricorn December 22 - January 19            jan=0
        // Aquarius January 20 - February 18             feb=1
        // February 19 - March 20 pisces                 mar=2
        // Aries March 21 - April 19                     apr=3
        // Taurus April 20 - May 20                      may=4
        // Gemini May 21 - June 20                       jun=5
        // Cancer June 21 - July 22                      jul=6
        // Leo July 23 - August 22                       aug=7
        // Virgo August 23 - September 22                sep=8
        // Libra September 23 - October 22               oct=9
        // Scorpio October 23 - November 21              nov=10
        // Sagittarius November 22 - December 21         dec=11

        if((month == 11) && ( day>= 22) || (month == 0) && (day <= 19)) {
            return zodiacSigns[0];
        }else if((month == 0) && (day >= 20)  || (month == 1) && (day <= 18)) {
            return zodiacSigns[1];
        } else if((month == 1) && (day >= 19)  || (month == 2) && (day <= 20)) {
            return zodiacSigns[2];
        } else if((month == 2) && (day >= 21)  || (month == 3) && (day <= 19)) {
            return zodiacSigns[3];
        } else if((month == 3) && (day >= 20)  || (month == 4) && (day <= 20)) {
            return zodiacSigns[4];
        } else if((month == 4) && (day >= 21)  || (month == 5) && (day <= 20)) {
            return zodiacSigns[5];
        } else if((month == 5) && (day >= 21)  || (month == 6) && (day <= 22)) {
            return zodiacSigns[6];
        } else if((month == 6) && (day >= 23)  || (month == 7) && (day <= 22)) {
            return zodiacSigns[7];
        } else if((month == 7) && (day >= 23)  || (month == 8) && (day <= 21)) {
            return zodiacSigns[8];
        } else if((month == 8) && (day >= 22) || (month == 9) && (day <= 21)) {
            return zodiacSigns[9];
        } else if((month == 9) && (day >= 24)  || (month == 10) && (day <= 22)) {
            return zodiacSigns[10];
        } else if((month == 10) && (day >= 23)  || (month == 11) && (day <= 21)) {
            return zodiacSigns[11];
        } else {
            return null;
        }
    }
}
