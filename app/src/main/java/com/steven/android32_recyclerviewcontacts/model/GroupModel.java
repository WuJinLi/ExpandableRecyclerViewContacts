package com.steven.android32_recyclerviewcontacts.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenWang on 16/6/17.
 */
public class GroupModel {
    private String firstLetter;
    private boolean isExpand;
    private List<UserModel> userModels;
    private int resId = android.R.drawable.arrow_down_float;

    public GroupModel(String firstLetter) {
        this.firstLetter = firstLetter;
        userModels = new ArrayList<>();
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public List<UserModel> getUserModels() {
        return userModels;
    }

    public void setUserModels(List<UserModel> userModels) {
        this.userModels = userModels;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }
}
