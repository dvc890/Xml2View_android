package com.dvc.xml;

/**
 * Created by dvc on 2017/8/23.
 */

public class ID {
    public static int getID(String idStr) {
        return Math.abs(idStr.hashCode());
    }
}
