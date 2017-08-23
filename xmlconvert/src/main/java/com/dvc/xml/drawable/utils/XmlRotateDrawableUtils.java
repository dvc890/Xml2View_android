package com.dvc.xml.drawable.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in rotatedrawable
 */
public class XmlRotateDrawableUtils {

    /**
     * Generate a RotateDrawable
     * @param mContext
     * @param properties
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RotateDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
        RotateDrawable value = new RotateDrawable();

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case ROTATE: {
                    switch (dynProp.field) {
                        case visible: {
                            value.setVisible(dynProp.getValueBoolean(), dynProp.getValueBoolean());
                        }
                        break;
                        case fromDegrees: {
                            value.setFromDegrees(dynProp.getValueFloat());
                        }
                        break;
                        case toDegrees: {
                            value.setToDegrees(dynProp.getValueFloat());
                        }
                        break;
                        case pivotX: {
                            value.setPivotX(dynProp.getValueFloat());
                        }
                        break;
                        case pivotY: {
                            value.setPivotY(dynProp.getValueFloat());
                        }
                        break;
                        case drawable: {
                            value.setDrawable(AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString()));
                        }
                        break;
                    }

                }
                break;
                default: {
                    value.setDrawable(XmlDrawableUtils.generateDrawable(mContext,dynProp.name.name(),dynProp.getValuePropertyList()));
                }
                break;
            }
        }
        return value;
    }
}
