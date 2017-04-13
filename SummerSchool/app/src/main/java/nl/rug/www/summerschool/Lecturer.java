package nl.rug.www.summerschool;

/**
 * This class is a model of lecturer information that contains
 *
 * @since 13/04/2017
 * @author Jeongkyun
 */

public class Lecturer extends Content {

    private String mDepartment;

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }
}
