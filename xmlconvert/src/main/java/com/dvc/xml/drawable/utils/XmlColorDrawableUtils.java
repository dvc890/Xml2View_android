package com.dvc.xml.drawable.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in colordrawable
 */
public class XmlColorDrawableUtils {

    /**
     * Generate a ColorDrawable
     * @param mContext
     * @param properties
     */
    public static ColorDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
        ColorDrawable value = null;

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case COLOR: {
                    switch (dynProp.field) {
                        case color: {
                            switch (dynProp.type) {
                                case PATH: {
                                    value = new ColorDrawable((Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString()));
                                }
                                break;
                                case COLOR: {
                                    value = new ColorDrawable(dynProp.getValueColor());
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
                default: {

                }
                break;
            }
        }
        return value;
    }
}
