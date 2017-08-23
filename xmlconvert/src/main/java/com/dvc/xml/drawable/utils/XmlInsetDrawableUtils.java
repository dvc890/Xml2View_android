package com.dvc.xml.drawable.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in Insetdrawable
 */
public class XmlInsetDrawableUtils {

    /**
     * Generate a InsetDrawable
     * @param mContext
     * @param properties
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static InsetDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
        int inset = 0, insetLeft = 0, insetRight = 0, insetTop = 0, insetBottom = 0;
        boolean visible = true;
        Drawable drawable = null;
        InsetDrawable value;

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case INSET: {
                    switch (dynProp.field) {
                        case visible: {
                            visible = dynProp.getValueBoolean();
                        }
                        break;
                        case drawable: {
                            drawable = AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString());
                        }
                        break;
                        case inset: {
                            inset = dynProp.getValueInt();
                        }
                        break;
                        case insetLeft: {
                            insetLeft = dynProp.getValueInt();
                        }
                        break;
                        case insetRight: {
                            insetRight = dynProp.getValueInt();
                        }
                        break;
                        case insetTop: {
                            insetTop = dynProp.getValueInt();
                        }
                        break;
                        case insetBottom: {
                            insetBottom = dynProp.getValueInt();
                        }
                        break;

                    }
                }
                break;
                default: {
                    Drawable temp = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
                    if (temp != null)
                        drawable = temp;
                }
            }
        }

        if (inset != 0)
            value = new InsetDrawable(drawable,inset);
        else
            value = new InsetDrawable(drawable,insetLeft, insetTop, insetRight, insetBottom);
        value.setVisible(visible,visible);

        return value;
    }
}
