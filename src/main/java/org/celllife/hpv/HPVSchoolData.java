package org.celllife.hpv;

import android.location.Location;

/**
 * This class contains all the HPV school data
 */
public class HPVSchoolData {

    // EMIS number: emis_number
    // School Name: school_name
    // Name of school representative: school_rep
    // School representative contact number: school_rep_contact
    // Take GPS co-ordinates of school: school_gps
    
    private Integer emisNumber;
    private String schoolName;
    private String schoolRep;
    private String schoolRepContact;
    private Location schoolGps;
    
    public HPVSchoolData() {
        
    }
    
    public Integer getEmisNumber() {
        return emisNumber;
    }
    
    public void setEmisNumber(Integer emisNumber) {
        this.emisNumber = emisNumber;
    }
    
    public String getSchoolName() {
        return schoolName;
    }
    
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    
    public String getSchoolRep() {
        return schoolRep;
    }
    
    public void setSchoolRep(String schoolRep) {
        this.schoolRep = schoolRep;
    }
    
    public String getSchoolRepContact() {
        return schoolRepContact;
    }
    
    public void setSchoolRepContact(String schoolRepContact) {
        this.schoolRepContact = schoolRepContact;
    }
    
    public Location getSchoolGps() {
        return schoolGps;
    }
    
    public void setSchoolGps(Location schoolGps) {
        this.schoolGps = schoolGps;
    }
}
