package com.dvc.xml.drawable.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;

import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in scaledrawable
 */
public class XmlScaleDrawableUtils {


    /**
     * Generate a ScaleDrawable
     * @param mContext
     * @param properties
     */
    public static ScaleDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
        float scaleWidth = 0,scaleHeight = 0;
        int scaleGravity = 0;
        boolean useIntrinsicSizeAsMinimum;
        Drawable drawable = null;

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case SCALE: {
                    switch (dynProp.field) {
                        case scaleWidth: {
                            scaleWidth = dynProp.getValueFloat();
                        }
                        break;
                        case scaleHeight: {
                            scaleHeight = dynProp.getValueFloat();
                        }
                        break;
                        case scaleGravity: {
                            scaleGravity = dynProp.getValueInt();
                        }
                        break;
                        case useIntrinsicSizeAsMinimum: {
                            useIntrinsicSizeAsMinimum = dynProp.getValueBoolean();
                            // to be support
                        }
                        break;
                    }

                }
                break;
                default: {
                    Drawable temp = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
                    if(temp != null)
                        drawable = temp;
                }
                break;
            }
        }
        ScaleDrawable value = new ScaleDrawable(drawable,scaleGravity, scaleWidth, scaleHeight);

        return value;
    }
}
