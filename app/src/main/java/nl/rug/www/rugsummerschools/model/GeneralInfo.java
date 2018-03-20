package nl.rug.www.rugsummerschools.model;

/**
 * This class is a model of general information that contains
 * attributes that Content class has.
 *
 * @author Jeongkyun
 * @version 2.0.0
 * @since 13/04/2017
 */

public class GeneralInfo extends Content {

    private String mCategory;

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
