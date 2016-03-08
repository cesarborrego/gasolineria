package com.neo.gas_ec.model;

/**
 * Created by cesar on 23/11/15.
 */
public class Data_test {

    private String tagTitle;
    private String tagData;
    private int position;

    public Data_test() {

    }

    public Data_test(String tagTitle, String tagData, int position) {
        this.tagTitle = tagTitle;
        this.tagData = tagData;
        this.setPosition(position);
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public String getTagData() {
        return tagData;
    }

    public void setTagData(String tagData) {
        this.tagData = tagData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
