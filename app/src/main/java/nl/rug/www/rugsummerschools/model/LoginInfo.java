package nl.rug.www.rugsummerschools.model;

/**
 * Login information model
 *
 * @author Jeongkyun
 * @version 2.0.0
 * @since 10/02/2018
 */

public class LoginInfo extends Content {

    private String mSchoolId;
    private String mSchoolName;

    public String getSchoolName() {
        return mSchoolName;
    }

    public void setSchoolName(String schoolName) {
        mSchoolName = schoolName;
    }

    public String getSchoolId() {
        return mSchoolId;
    }

    public void setSchoolId(String schoolId) {
        mSchoolId = schoolId;
    }
}
