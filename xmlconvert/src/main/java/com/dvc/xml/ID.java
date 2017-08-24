package com.dvc.xml;

/**
 * Created by dvc on 2017/8/23.
 */

public class ID {
    //private static int mCurrentId = 13;
    public static int generateViewId(String idStr) {
        return Math.abs(idStr.hashCode());
        //return mCurrentId++;
    }
    public static int getID(String idStr) {
        return Math.abs(idStr.hashCode());
    }
}
