package nl.rug.www.rugsummerschools.model;

/**
 * Login information model
 *
 * @since 10/02/2018
 * @author Jeongkyun
 * @version 2.0.0
 */

public class LoginInfo extends Content {

    private String mSchoolId;

    public String getSchoolId() {
        return mSchoolId;
    }

    public void setSchoolId(String schoolId) {
        mSchoolId = schoolId;
    }
}
