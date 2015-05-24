package com.damianin.babyplanner;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.damianin.babyplanner.Adaptors.AdapterLoveDays;
import com.damianin.babyplanner.Adaptors.AdapterNavigationDrawer;
import com.damianin.babyplanner.Helper.NavigationDrawerItems;
import com.damianin.babyplanner.Helper.RoundedTransformation;
import com.damianin.babyplanner.UserInterfaces.LoginActivity;
import com.damianin.babyplanner.UserInterfaces.ManagePartnersMain;
import com.damianin.babyplanner.dialogs.ChangeProfilePic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Main extends ActionBarActivity {

    protected BackendlessUser mCurrentUser;
    protected Toolbar toolbar;
    protected RecyclerView loveDaysCards;

    protected List<BackendlessUser> cardsToDisplay;

    //navigation drawer variables
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListView mDrawerList;
    private LinearLayout mDrawerLinear;
    private Button logoutButtonNavigationDrawer;
    protected ChangeProfilePic changeProfilePic;//tova se izpolzva za onactivity result ako smeniame profile pic


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
        AdapterLoveDays adapter = new AdapterLoveDays(cardsToDisplay, this);
        loveDaysCards.setAdapter(adapter);
    }



     /*
        HELPER METODI ZA NAVIGATION DRAWER
     */

    //setup the drawer
    private void setUpDrawer(){
        //vrazvame navigation drawer
        mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);




        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        )

        {




            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                mDrawerToggle.syncState();



            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                loadHeaderNavigationDrawer();
                mDrawerToggle.syncState();


            }
            //disables animation from hamburger to back arrow
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                mDrawerToggle.syncState();

            }


        };


        mDrawerToggle.syncState();

        //zadavame spisaka, koito shte se pokazva
        List<NavigationDrawerItems> items = new ArrayList<NavigationDrawerItems>();
        //partners
        items.add(new NavigationDrawerItems(R.mipmap.partner_icon,getString(R.string.menu_edit_partner)));
        String partnerOptions[] = getResources().getStringArray(R.array.navigation_drawer_partners_options);
        for(String option: partnerOptions ){
            items.add(new NavigationDrawerItems(option));
        }

        //account settings
        items.add(new NavigationDrawerItems(R.mipmap.ic_action_settings,getString(R.string.account_settings_title)));
        String[] accountOptions =  getResources().getStringArray(R.array.edit_profile_options);
        for(String option: accountOptions) {
            items.add(new NavigationDrawerItems(option));
        }



        // ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.edit_profile_options, android.R.layout.simple_spinner_dropdown_item);
        AdapterNavigationDrawer adapter = new AdapterNavigationDrawer(this,items);

        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //vrazvmam logout butona ot navigation drawer
        logoutButtonNavigationDrawer = (Button) findViewById(R.id.logout_button);
        logoutButtonNavigationDrawer.setOnClickListener(logoutButtonOnClick);


        // Set the drawer toggle as the DrawerListener
        //mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
/*
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        */
        mDrawerToggle.syncState();


    }

    //load header
    private void loadHeaderNavigationDrawer(){
        if(mCurrentUser != null) {
            mCurrentUser = Backendless.UserService.CurrentUser();
            //zarezdame profile pic, ako ima takava
            if (mCurrentUser.getProperty(Statics.KEY_PROFILE_PIC_PATH) != null) {
                //ako ima profile pic ia zarezdame s picaso
                //existingprofilePicPath se izpolzva i v sluchaite, kogato user si smenia profile pic
                // togava kachvame na servera novata kartinka i izpolzvame tazi promenliva,
                // za da iztriem starata profile pic ot servera
                String existingProfilePicPath = (String) mCurrentUser.getProperty(Statics.KEY_PROFILE_PIC_PATH);
                ImageView profilePicture = (ImageView) findViewById(R.id.drawer_header_image);
                Picasso.with(Main.this)
                        .load(existingProfilePicPath)
                        .transform(new RoundedTransformation(Statics.PICASSO_ROUNDED_CORNERS, 0))
                        .into(profilePicture);

            }
            //vrazvame username i password
            TextView usernameDrawerHeader = (TextView) findViewById(R.id.username);
            TextView emailDrawerHeader = (TextView) findViewById(R.id.emailUser);

            emailDrawerHeader.setText(mCurrentUser.getEmail());
            usernameDrawerHeader.setText((String)mCurrentUser.getProperty(Statics.KEY_USERNAME));
        }
    }

    /*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
ON CLICK LISTENER ZA NAVIGATION DRAWER
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
    //on lick za logout buttona
    private View.OnClickListener logoutButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //logout
            Backendless.UserService
                    .logout(new DefaultCallback<Void>(Main.this, getResources().getString(R.string.logout_message)) {
                        @Override
                        public void handleResponse(Void aVoid) {

                            //prashta kam login screen
                            navigateToLogin();
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            super.handleFault(backendlessFault);
                            Toast.makeText(Main.this, R.string.logout_error, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    };

    //on click za ostanalite opcii
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        protected String mMessageType;



        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    //zaglavna linia Partners

                    return;
                case 1:
                    //search for partners
                    Intent searchPartner = new Intent(Main.this, ManagePartnersMain.class);
                    //slagame toya KEY, za da prevkluchim na pravilia tab ot drugata strana kato otvorim ekrana
                    searchPartner.putExtra(Statics.KEY_PARTNERS_SELECT_TAB, Statics.KEY_PARTNERS_SELECT_SEARCH);
                    startActivity(searchPartner);
                    return;
                case 2:
                    //pending partner requests
                    Intent partnerRequest = new Intent(Main.this, ManagePartnersMain.class);
                    //slagame toya KEY, za da prevkluchim na pravilia tab ot drugata strana kato otvorim ekrana
                    partnerRequest.putExtra(Statics.KEY_PARTNERS_SELECT_TAB, Statics.KEY_PARTNERS_SELECT_PENDING_REQUESTS);
                    startActivity(partnerRequest);
                    return;
                case 3:
                    //existing partners
                    Intent existingPartners = new Intent(Main.this, ManagePartnersMain.class);
                    //slagame toya KEY, za da prevkluchim na pravilia tab ot drugata strana kato otvorim ekrana
                    existingPartners.putExtra(Statics.KEY_PARTNERS_SELECT_TAB, Statics.KEY_PARTNERS_SELECT_EXISTING_PARTNERS);
                    startActivity(existingPartners);
                    return;
                case 4:
                    //zaglavna lina za account settings
                    return;
                case 5:
                    //change sex
                    DialogFragment sexDialog = new GuyOrGirlDialog();
                    sexDialog.show(getFragmentManager(), "Welcome");
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                    return;
                case 6:
                    //change date of birth

                    SetBirthday setBirthday = new SetBirthday();
                    //setBirthday.setTargetFragment(FragmentEditProfileActivity.this,SET_BIRTHDAY);

                    setBirthday.show(getSupportFragmentManager(),"Welcome");
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                    return;
                case 7:
                    //change password

                    ChangePassword changePassword = new ChangePassword();
                    //changePassword.setTargetFragment(FragmentEditProfileActivity.this, CHANGE_PASSWORD);
                    changePassword.show(getSupportFragmentManager(),"Welcome");

                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                    return;
                case 8:
                    //change profile picture

                    //rezultatat se obrabotva v OnActivityResult v ChangeProfilePic dialog
                    changeProfilePic = new ChangeProfilePic();
                    changeProfilePic.show(getSupportFragmentManager(),"Welcome");
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                    return;
                case 9:
                    //change username
                    ChangeUsername changeUsername = new ChangeUsername();
                    //changeUsername.setTargetFragment(FragmentEditProfileActivity.this, CHANGE_USERNAME);
                    changeUsername.show(getSupportFragmentManager(),"Welcome");
                    mDrawerList.setItemChecked(position, true);
                    mDrawerLayout.closeDrawer(mDrawerLinear);
                    return;

            }
        }




    }

}
