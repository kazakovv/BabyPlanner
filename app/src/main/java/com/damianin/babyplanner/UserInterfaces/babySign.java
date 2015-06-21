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
    protected Button previousDateForConceiving;
    protected TextView DateGivingBirth;
    protected TextView DateConceiving;
    protected TextView ConceivingSign;

    Calendar dateBorn;
    Calendar dateConceiving;

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

        previousDateForConceiving = (Button) findViewById(R.id.buttonPreviousConceivingDate);
        previousDateForConceiving.setOnClickListener(buttonPreviousConceivingDate);
        DateConceiving = (TextView) findViewById(R.id.textViewConceivingDate);
        DateGivingBirth = (TextView) findViewById(R.id.textViewDateGiveBirth);
        ConceivingSign = (TextView) findViewById(R.id.textViewConceivingSign);

        dateConceiving = calculateDateConceiving();
        displayNextPossibleBirthStats();
        //calculateDateBorn();
    }


    //on click listener for next button
    private View.OnClickListener buttonNextConceivingDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            NextAndPreviousZodiacButtonsHelper(Statics.NEXT_ZODIAC_SIGN);

        }
    };

    //on click listener for previous button
    private View.OnClickListener buttonPreviousConceivingDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NextAndPreviousZodiacButtonsHelper(Statics.PREVIOUS_ZODIAC_SIGN);
        }
    };

    protected void NextAndPreviousZodiacButtonsHelper(int NextOrPreviousZodiacSign ){
        Calendar dateBorn = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        // we add the average length of cycle to calculate the next first day of ovulation
        if(NextOrPreviousZodiacSign == Statics.NEXT_ZODIAC_SIGN) {
            dateConceiving.add(Calendar.DAY_OF_MONTH, averageLengthOfCycle);
            dateBorn = calculateDateBorn(dateConceiving, Statics.NEXT_ZODIAC_SIGN);
        } else if (NextOrPreviousZodiacSign == Statics.PREVIOUS_ZODIAC_SIGN){
            dateConceiving.add(Calendar.DAY_OF_MONTH, -averageLengthOfCycle);
            dateBorn = calculateDateBorn(dateConceiving, Statics.NEXT_ZODIAC_SIGN);
        }
        String dateToConceive = getResources().getString(R.string.next_date_to_conceive)
                +" " +  formatter.format(dateConceiving.getTime());
        DateConceiving.setText(dateToConceive);
        //we add 48 weeks from the date of conceiving to calculate when the baby will be born

        String nextDateToGiveBirth;

        nextDateToGiveBirth = getResources().getString(R.string.next_date_to_give_birth)
                + " " + formatter.format(dateBorn.getTime());
        DateGivingBirth.setText(nextDateToGiveBirth);
        String zodiacSign = getResources().getString(R.string.sign_of_baby_will_be)
                + " " + returnZodiacSign(dateBorn);
        ConceivingSign.setText(zodiacSign);
    }

    protected Calendar calculateDateConceiving(){
        Calendar dateConceiving = Calendar.getInstance();
        dateConceiving = calculateFirstDayOfOvulation();

        return dateConceiving;
    }

    protected Calendar calculateDateBorn(Calendar dateConceiving, int NextOrPreviousZodiacSign) {
        Calendar dateToBeBorn = Calendar.getInstance();
        dateToBeBorn.setTime(dateConceiving.getTime());
        if(NextOrPreviousZodiacSign == Statics.NEXT_ZODIAC_SIGN) {
            dateToBeBorn.add(Calendar.WEEK_OF_YEAR, 48);
        } else if (NextOrPreviousZodiacSign == Statics.PREVIOUS_ZODIAC_SIGN){
            dateToBeBorn.add(Calendar.WEEK_OF_YEAR, -48);
        }
        return dateToBeBorn;
    }

    protected void displayNextPossibleBirthStats(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        TextView datesToConceive = (TextView) findViewById(R.id.datesToConceive);
        TextView datesToGiveBirth = (TextView) findViewById(R.id.datesToGiveBirth);
        TextView zodiacSignOfBaby = (TextView) findViewById(R.id.zodiacSignOfBaby);

        //set dates to conceive
        String conceiveDates = getResources().getString(R.string.current_dates_to_conceive);
        Calendar firstDayOfOvulation = calculateFirstDayOfOvulation();
        conceiveDates += " " + formatter.format(firstDayOfOvulation.getTime()) + " "
                + getResources().getString(R.string.and) + " ";
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH,4);
        conceiveDates += formatter.format(firstDayOfOvulation.getTime());

        datesToConceive.setText(conceiveDates);

        //set dates to give birth
        //substract four days to obtain the initial first day of ovulation
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH,-4);
        Calendar giveBirth = calculateDateBorn(firstDayOfOvulation, Statics.NEXT_ZODIAC_SIGN);
        String giveBirthMessage = getResources().getString(R.string.baby_sign_next_date_give_birth)
                + " " + formatter.format(giveBirth.getTime()) + getResources().getString(R.string.and);
        giveBirth.add(Calendar.DAY_OF_MONTH,4);
        giveBirthMessage +=  " " + formatter.format(giveBirth.getTime());
        datesToGiveBirth.setText(giveBirthMessage);

        //set zodiac sign message
        //substract 4 days to get the inititial first day the baby could be born
        giveBirth.add(Calendar.DAY_OF_MONTH,-4);
        String[] signs = new String[4];
        for(int i = 0; i<4 ; i++){
            giveBirth.add(Calendar.DAY_OF_MONTH,i);
            signs[i] = returnZodiacSign(giveBirth);
        }
        String babySign = signs[0];
        String alternativeBabySign = signs[3];
        String zodiacSignOfBabyMessage;
        if(babySign == alternativeBabySign ) {
            zodiacSignOfBabyMessage = getResources().getString(R.string.baby_sign_zodiac)
                    + " " + babySign;
            zodiacSignOfBaby.setText(zodiacSignOfBabyMessage);
        } else {
            zodiacSignOfBabyMessage = getResources().getString(R.string.baby_sign_zodiac)
                    + " " + babySign + " " + getResources().getString(R.string.or)
                    + " " + alternativeBabySign;
            zodiacSignOfBaby.setText(zodiacSignOfBabyMessage);
        }
    }

    protected Calendar calculateFirstDayOfOvulation(){
        Calendar firstDayOfOvulation  = Calendar.getInstance();
        firstDayOfOvulation.setTime(firstDayOfCycle);
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH, averageLengthOfCycle - 14);
        return firstDayOfOvulation;
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
