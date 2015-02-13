package org.celllife.hpv;
/**
 * This class contains all the constants required to automatically load
 * HPV forms into ODK
 */
public class HPVConsts {

    // Name of the HPV form
    public static final String HPV_FORM_NAME = "HPV Learner Vaccination";
    
    // Name of the school login form (this is a "proxy" form as it does not really exist - see below)
    public static final String HPV_SCHOOL_LOGIN_FORM_NAME = "HPV School Login";
    
    // Name of the file that contains the hardcoded school login xform (a subset of the original HPV form)
    public static final String HPV_SCHOOL_LOGIN_FORM_FILENAME = "school-login-xform.xml";
    // Name of the file that contains the data captured in the School Login form
    public static final String HPV_SCHOOL_LOGIN_DATA_FILENAME = "school-login-data.xml";
    // CSV file that contains the mapping of school emis to name
    public static final String SCHOOL_EMIS_CSV_FILENAME = "schoolemis.csv";
    
    // School login form bindings
    public static final String HPV_FORM_BINDING_EMIS_NUMBER = "emis_number";
    public static final String HPV_FORM_BINDING_SCHOOL_NAME = "school_name";
    public static final String HPV_FORM_BINDING_SCHOOL_NAME_ENTRY = "school_name_entry";
    public static final String HPV_FORM_BINDING_SCHOOL_REP = "school_rep";
    public static final String HPV_FORM_BINDING_SCHOOL_REP_CONTACT = "school_rep_contact";
    public static final String HPV_FORM_BINDING_SCHOOL_GPS = "school_gps";
    
    // Start of Learner registration form
    public static final String HPV_FORM_BINDING_LEARNER_NAME = "learner_name";
    public static final String HPV_FORM_BINDING_LEARNER_SURNAME = "learner_surname";
    
    // Key used when loading a form to control where the form should start
    public static final String HPV_QUESTION_INDEX = "hpv_question_index";
    // Key used when loading a form to indicate that the form being loaded is the HPV form and needs data pre-loading
    public static final String HPV_FORM_KEY = "HPV_FORM";
    // Key used when loading a form to indicate that the form data must be saved to a particular file instead of the usual instance file
    public static final String HPV_PROXY_FORM_FILE_KEY = "PROXY_FORM_FILE";

}
