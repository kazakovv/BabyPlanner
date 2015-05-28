package com.damianin.babyplanner;

/**
 * Created by Victor on 18/05/2015.
 */
public class Statics {
    //Backendless KEYS

    public static final String KEY_USERNAME = "userName";
    public static final String KEY_RECEPIENT_IDS = "recepientIDs";
    public static final String KEY_RECEPIENT_EMAILS = "recepientsEmails";

    public static final String KEY_PARTNERS = "partners";

    public static final String KEY_MALE_OR_FEMALE = "maleOrFemale";


    public static final String TYPE_IMAGE_MESSAGE = "image";
    public static final String TYPE_CALENDAR_UPDATE = "calendarUpdate";
    public static final String TYPE_PARTNER_REQUEST ="partnerRequest";

    public static final String KEY_EMAIL_CALENDAR = "emailCalendar";
    public static final String SEX_MALE = "Male";
    public static final String SEX_FEMALE = "Female";

    public static final String KEY_DATE_OF_BIRTH = "dateOfBirth";

    public static final String KEY_URL = "urlAddress";

    //keys za podavane na values m/u Private Days calendar i Love Days fragment
    public static final String CALENDAR_YEAR = "caldenarYear";
    public static final String CALENDAR_MONTH = "calendarMonth";
    public static final String CALENDAR_DAY = "calendarDay";
    public static final String AVERAGE_LENGTH_OF_MENSTRUAL_CYCLE = "averageLengthOfCycle";
    public static final String TITLE_CYCLE = "titleCycle";
    public static final String FIRST_DAY_OF_CYCLE = "firstDayOfCycle";
    public static final String SEND_SEXY_CALENDAR_UPDATE_TO_PARTNERS = "sendSexyCalendarUpdate";


    //keys za tarsene v Backendless table s cycle statusi i titles
    public static final String KEY_MENSTRUATION = "Menstruation";
    public static final String KEY_OVULATION = "Ovulation";
    public static final String KEY_FOLLICULAR = "Follicular";
    public static final String KEY_LUTEAL = "Luteal";

    public static final String KEY_SEXY_STATUS = "sexyStatus";


    public static final String KEY_PARTNER_REQUEST = "partnerRequest";
    public static final String KEY_PARTNER_DELETE = "deletePartner";

    public static final String KEY_PARTNERS_SELECT_TAB = "selectTab";
    public static final String KEY_PARTNERS_SELECT_SEARCH = "selectSearchPartnersTab";
    public static final String KEY_PARTNERS_SELECT_PENDING_REQUESTS = "selectPendingRequestsTab";
    public static final String KEY_PARTNERS_SELECT_EXISTING_PARTNERS = "selectExistingPartnersTab";


    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_MESSAGE_ID = "messageId";

    public static final String KEY_PROFILE_PIC_PATH = "profilePicPath";

    public static final int SHORT_SIDE_TARGET_THUMBNAIL = 100;

    public static final String KEY_SET_STATUS = "setStatus";

    public static final String BACKENDLESS_INVALID_LOGIN_OR_PASS_MESSAGE = "3003";
    public static final String BACKENDLESS_INVALID_EMAIL_PASSWORD_RECOVERY = "3020";
    public static final String BACKENDLESS_TABLE_NOT_FOUND_CODE = "1009";

    public static final String KEY_PARTNER_REQUEST_APPROVED = "partnerRequestApproved";

    public static final String KEY_UPDATE_SEXY_STATUS = "updateSexyStatus";

    public static final String KEY_USER_REQUESTING_TO_UPDATE_PARTNERS = "userRequestingToUpdatePartners";

    public static final int PICASSO_ROUNDED_CORNERS = 30;


    //construct message for dialog box one love message per day

    //onactivity result codes
    public static final int MENSTRUAL_CALENDAR_DIALOG = 11;
    public static final int UPDATE_STATUS = 22;


    public static final String KEY_REFRESH_FRAGMENT_LOVE_BOX = "fragmentLoveBox";

    //max size na pic

    //custom alert dialog
    public static final String ALERTDIALOG_TITLE = "titleAD";
    public static final String ALERTDIALOG_MESSAGE="messageAD";

    public static final String BREAK_UP_DIALOG_POSITION_TO_REMOVE = "positionToRemove";

    public static final String SHARED_PREFS="myPrefs";
    public static final String KEY_SAVED_EMAIL_FOR_LOGIN = "emailForLogin";
    //izpolzva se za reference dali ima chakashti zaiavki za partniori. Ako e true pokazvame buton na main t
    public static Boolean pendingPartnerRequest = false;



    //flags push receiver
    public static int FLAG_CALENDAR_UPDATE = 1001;
    public static int FLAG_PARTNER_REQUEST = 1002;

    //name for partner request intent da pokazva add partner icon if activity is running
    public static String FLAG_INTENT_ADD_PARTNER = "addPartnerRequest";


}
