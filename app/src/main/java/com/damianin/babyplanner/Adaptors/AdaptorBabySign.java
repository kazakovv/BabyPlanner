package com.damianin.babyplanner.Adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.damianin.babyplanner.R;
import com.damianin.babyplanner.Statics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Victor on 30/06/2015.
 */
public class AdaptorBabySign extends RecyclerView.Adapter<AdaptorBabySign.ContactViewHolder> {
    private static int FIRST_CARD = 0;
    private static int SECOND_CARD = 1;

    private int averageLengthOfCycle;
    private Date firstDayOfCycle;
    private Context context;
    private Calendar dateConceiving;

    public AdaptorBabySign(int averageLengthOfCycle, Date firstDayOfCycle, Context context){
        this.averageLengthOfCycle = averageLengthOfCycle;
        this.firstDayOfCycle = firstDayOfCycle;
        this.context = context;
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType == FIRST_CARD){
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_baby_sign_first_card, parent, false);
        } else if (viewType == SECOND_CARD){
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_baby_sign_second_card, parent, false);
        }
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        dateConceiving = calculateDateConceiving();
        if (position == FIRST_CARD) {
            displayNextPossibleBirthStats(holder);
        }

        if(position == SECOND_CARD) {
            holder.vPreviousConceivingDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NextAndPreviousZodiacButtonsHelper(Statics.PREVIOUS_ZODIAC_SIGN, holder);
                }
            });

            holder.vNextConceivingDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NextAndPreviousZodiacButtonsHelper(Statics.NEXT_ZODIAC_SIGN, holder);
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        //vrashta view dali e parvata ili vtorta karta
        int viewType=0;
        if(position==0) {
            viewType = FIRST_CARD;
        }

        if(position == 1) {
            viewType = SECOND_CARD;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        //first card
        protected TextView vTitleBabySign;
        protected TextView vDatesToConceive;
        protected TextView vDatesToGiveBirth;
        protected TextView vZodiacSignOfBaby;
        protected ImageView vImageSignFirstCard;
        protected ImageView vImageSignFirstCardAlternative;

        //second card
        protected Button vPreviousConceivingDate;
        protected Button vNextConceivingDate;
        protected TextView vSecondDatesToConceive;
        protected TextView vSecondDatesToGiveBirth;
        protected TextView vSecondZodiacSignOfBaby;
        protected ImageView vImageSignSecondCard;
        protected ImageView vImageSignSecondCardAlternative;



        public ContactViewHolder(View v) {

            super(v);


            //first card
            vTitleBabySign = (TextView) v.findViewById(R.id.title_baby_sign);
            vDatesToConceive = (TextView) v.findViewById(R.id.datesToConceive);
            vDatesToGiveBirth = (TextView) v.findViewById(R.id.datesToGiveBirth);
            vZodiacSignOfBaby = (TextView) v.findViewById(R.id.zodiacSignOfBaby);
            vImageSignFirstCard = (ImageView) v.findViewById(R.id.image_sign_first_card);
            vImageSignFirstCardAlternative = (ImageView) v.findViewById(R.id.image_sign_first_card_alternative);

            //second card
            vSecondDatesToConceive = (TextView) v.findViewById(R.id.textViewConceivingDate);
            vSecondDatesToGiveBirth = (TextView) v.findViewById(R.id.textViewDateGiveBirth);
            vSecondZodiacSignOfBaby = (TextView) v.findViewById(R.id.textViewConceivingSign);
            vImageSignSecondCard = (ImageView) v.findViewById(R.id.image_sign_second_card);
            vImageSignSecondCardAlternative = (ImageView) v.findViewById(R.id.image_sign_second_card_alternative);

            vPreviousConceivingDate = (Button) v.findViewById(R.id.buttonPreviousConceivingDate);
            if(vPreviousConceivingDate != null) {
                vPreviousConceivingDate.setTag(this);
            }
            vNextConceivingDate = (Button) v.findViewById(R.id.buttonNextConceivingDate);

            if (vNextConceivingDate !=null){
                vNextConceivingDate.setTag(this);
            }
        }



    }


    protected void NextAndPreviousZodiacButtonsHelper(int NextOrPreviousZodiacSign, ContactViewHolder holder){
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
        String dateToConceive = context.getResources().getString(R.string.next_date_to_conceive)
                +" " +  formatter.format(dateConceiving.getTime());

        dateConceiving.add(Calendar.DAY_OF_MONTH,4);
        dateToConceive += " " + context.getResources().getString(R.string.and)
                + " " + formatter.format(dateConceiving.getTime());
        holder.vSecondDatesToConceive.setText(dateToConceive);
        //we add 48 weeks from the date of conceiving to calculate when the baby will be born
        //substract four days to ge the initial date of conceiving
        dateConceiving.add(Calendar.DAY_OF_MONTH,-4);
        String nextDateToGiveBirth;

        nextDateToGiveBirth = context.getResources().getString(R.string.next_date_to_give_birth)
                + " " + formatter.format(dateBorn.getTime())
                + " " + context.getResources().getString(R.string.and);
        dateBorn.add(Calendar.DAY_OF_MONTH,4);
        nextDateToGiveBirth += " " + formatter.format(dateBorn.getTime());
        holder.vSecondDatesToGiveBirth.setText(nextDateToGiveBirth);
        //calculate zodiac sign
        //substract four days to obtain the initial date of birth
        dateBorn.add(Calendar.DAY_OF_MONTH,-4);
        String zodiacSignFirstOption = returnZodiacSign(dateBorn);
        //add again four days to calculate the alternative zodiac sign
        dateBorn.add(Calendar.DAY_OF_MONTH,4);
        String alternativeZodiacSign = returnZodiacSign(dateBorn);
        String zodiacSign;
        if (zodiacSignFirstOption == alternativeZodiacSign) {
            zodiacSign = context.getResources().getString(R.string.sign_of_baby_will_be)
                    + " " + zodiacSignFirstOption;
            //set the image for the zodiac sign
            holder.vImageSignSecondCard.setImageResource(imageZodiacSignBaby(zodiacSignFirstOption));
            holder.vImageSignSecondCardAlternative.setImageResource(0);
        } else {
            zodiacSign = context.getResources().getString(R.string.sign_of_baby_will_be)
                    + " " + zodiacSignFirstOption + " " + context.getResources().getString(R.string.or) +
                    " " + alternativeZodiacSign;

            //set the image for the zodiac sign
            holder.vImageSignSecondCard.setImageResource(imageZodiacSignBaby(zodiacSignFirstOption));
            holder.vImageSignSecondCardAlternative.setImageResource(imageZodiacSignBaby(alternativeZodiacSign));
        }

        holder.vSecondZodiacSignOfBaby.setText(zodiacSign);
    }

    protected void displayNextPossibleBirthStats(ContactViewHolder holder){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");


        //set dates to conceive
        String conceiveDates = context.getResources().getString(R.string.current_dates_to_conceive);
        Calendar firstDayOfOvulation = calculateFirstDayOfOvulation();
        conceiveDates += " " + formatter.format(firstDayOfOvulation.getTime()) + " "
                + context.getResources().getString(R.string.and) + " ";
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH,4);
        conceiveDates += formatter.format(firstDayOfOvulation.getTime());

        holder.vDatesToConceive.setText(conceiveDates);


        //set dates to give birth
        //substract four days to obtain the initial first day of ovulation
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH,-4);
        Calendar giveBirth = calculateDateBorn(firstDayOfOvulation, Statics.NEXT_ZODIAC_SIGN);
        String giveBirthMessage = context.getResources().getString(R.string.baby_sign_next_date_give_birth)
                + " " + formatter.format(giveBirth.getTime()) + " " + context.getResources().getString(R.string.and);
        giveBirth.add(Calendar.DAY_OF_MONTH,4);
        giveBirthMessage +=  " " + formatter.format(giveBirth.getTime());
        if(holder.vDatesToGiveBirth !=null) {
            holder.vDatesToGiveBirth.setText(giveBirthMessage);
        }

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
            zodiacSignOfBabyMessage = context.getResources().getString(R.string.baby_sign_zodiac)
                    + " " + babySign;

                holder.vZodiacSignOfBaby.setText(zodiacSignOfBabyMessage);

                //set up the image
                holder.vImageSignFirstCard.setImageResource(imageZodiacSignBaby(babySign));
                holder.vImageSignFirstCardAlternative.setImageResource(0);

        } else {
            zodiacSignOfBabyMessage = context.getResources().getString(R.string.baby_sign_zodiac)
                    + " " + babySign + " " + context.getResources().getString(R.string.or)
                    + " " + alternativeBabySign;

                holder.vZodiacSignOfBaby.setText(zodiacSignOfBabyMessage);
            //set up the image
            holder.vImageSignFirstCard.setImageResource(imageZodiacSignBaby(babySign));
            holder.vImageSignFirstCardAlternative.setImageResource(imageZodiacSignBaby(alternativeBabySign));
        }
        //show picture of sign

    }

    protected int imageZodiacSignBaby(String babySign) {



        if (babySign == "Capricorn") {
            return R.mipmap.capricorn_sign;
        } else if( babySign == "Aquarius") {
            return R.mipmap.aquarius_sign;
        } else if( babySign == "Pisces") {
            return R.mipmap.pisces_sign;
        } else if( babySign == "Aries") {
            return R.mipmap.aries_sign;
        } else if( babySign == "Taurus") {
            return R.mipmap.taurus_sign;
        } else if( babySign == "Gemini") {
            return R.mipmap.gemini_sign;
        } else if( babySign == "Cancer") {
            return R.mipmap.cancer_sign;
        } else if( babySign == "Leo") {
            return R.mipmap.leo_sign;
        } else if( babySign == "Virgo") {
            return R.mipmap.virgo_sign;
        } else if( babySign == "Libra") {
            return R.mipmap.libra_sign;
        } else if( babySign == "Scorpio") {
            return R.mipmap.scorpio_sign;
        } else if( babySign == "Sagittarius") {
            return R.mipmap.sagittarius_sign;
        } else {
            return 0;
        }

    }


    protected Calendar calculateDateConceiving(){
        Calendar dateConceiving = Calendar.getInstance();
        dateConceiving = calculateFirstDayOfOvulation();

        return dateConceiving;
    }
    protected Calendar calculateFirstDayOfOvulation(){
        Calendar firstDayOfOvulation  = Calendar.getInstance();
        firstDayOfOvulation.setTime(firstDayOfCycle);
        firstDayOfOvulation.add(Calendar.DAY_OF_MONTH, averageLengthOfCycle - 14);
        return firstDayOfOvulation;
    }

    protected static Calendar calculateDateBorn(Calendar dateConceiving, int NextOrPreviousZodiacSign) {
        Calendar dateToBeBorn = Calendar.getInstance();
        dateToBeBorn.setTime(dateConceiving.getTime());
        if(NextOrPreviousZodiacSign == Statics.NEXT_ZODIAC_SIGN) {
            dateToBeBorn.add(Calendar.DAY_OF_MONTH, 280);
        } else if (NextOrPreviousZodiacSign == Statics.PREVIOUS_ZODIAC_SIGN){
            dateToBeBorn.add(Calendar.DAY_OF_MONTH, -280);
        }
        return dateToBeBorn;
    }

    protected static String returnZodiacSign(Calendar dateOfBirth){
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
        } else if((month == 8) && (day >= 22) || (month == 9) && (day <= 22)) {
            return zodiacSigns[9];
        } else if((month == 9) && (day >= 23)  || (month == 10) && (day <= 22)) {
            return zodiacSigns[10];
        } else if((month == 10) && (day >= 23)  || (month == 11) && (day <= 21)) {
            return zodiacSigns[11];
        } else {
            return null;
        }
    }
}
