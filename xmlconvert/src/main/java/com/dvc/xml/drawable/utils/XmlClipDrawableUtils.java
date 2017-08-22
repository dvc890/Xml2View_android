package com.dvc.xml.drawable.utils;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.XmlDynamicProperty;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 22/8/2017.
 * Helper function that apply properties in Clipdrawable
 */
public class XmlClipDrawableUtils {

    public static ClipDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties) {
        int clipOrientation = 0, gravity = 0;
        Drawable drawable = null;
        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case CLIP: {
                    switch (dynProp.field) {
                        case clipOrientation: {
                            clipOrientation = dynProp.getValueString().equalsIgnoreCase("HORIZONTAL") ? ClipDrawable.HORIZONTAL : ClipDrawable.VERTICAL;

                        }
                        break;
                        case gravity: {
                            gravity = (Integer) XmlDynamicProperty.getValueInt(Gravity.class, dynProp.getValueString().toUpperCase());
                        }
                        break;
                        case drawable: {
                            switch (dynProp.type) {
                                case PATH: {
                                    drawable = AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString());
                                }
                                break;
                                case COLOR: {
                                    drawable = new ColorDrawable(dynProp.getValueColor());
                                }
                                break;

                            }

                        }
                        break;
                    }
                }
                break;
                default: {
                    drawable = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
                }
                break;
            }
        }
        return new ClipDrawable(drawable, gravity, clipOrientation);
    }

}
