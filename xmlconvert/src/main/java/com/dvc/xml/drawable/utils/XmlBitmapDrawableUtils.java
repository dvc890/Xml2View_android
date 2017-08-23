package com.dvc.xml.drawable.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.XmlDynamicProperty;
import com.dvc.xml.drawable.XmlDrawableProperty;

import java.util.List;

/**
 * Created by dvc on 23/8/2017.
 * Helper function that apply properties in vitmapdrawable
 */
public class XmlBitmapDrawableUtils {

    /**
     * Generate a BitmapDrawable
     * @param mContext
     * @param properties
     */
    public static BitmapDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
        BitmapDrawable value = new BitmapDrawable();

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case BITMAP: {
                    switch (dynProp.field) {
                        case src: {
                            switch (dynProp.type) {
                                case COLOR: {
                                    value = new BitmapDrawable(drawableToBitmap(new ColorDrawable(dynProp.getValueColor())));
                                }
                                break;
                                case PATH: {
                                    value = new BitmapDrawable(drawableToBitmap(AssetsResUtils.getAssetDrawable(mContext,dynProp.getValueString())));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (XmlDrawableProperty dynProp : properties) {
            switch (dynProp.name) {
                case BITMAP: {
                    switch (dynProp.field) {
                        case antialias: {
                            value.setAntiAlias(dynProp.getValueBoolean());
                        }
                        break;
                        case filter: {
                            value.setFilterBitmap(dynProp.getValueBoolean());
                        }
                        break;
                        case dither: {
                            value.setDither(dynProp.getValueBoolean());
                        }
                        break;
                        case gravity: {
                            value.setGravity((Integer) XmlDynamicProperty.getValueInt(Gravity.class,dynProp.getValueString()));
                        }
                        break;
                        case tileMode: {
                            if(!dynProp.getValueString().equals("disabled")) {
                                Shader.TileMode tileMode = Shader.TileMode.valueOf(dynProp.getValueString().toUpperCase().trim());
                                value.setTileModeXY(tileMode, tileMode);
                            }
                        }
                        break;
                        case tileModeX: {
                            if(!dynProp.getValueString().equals("disabled")) {
                                Shader.TileMode tileMode = Shader.TileMode.valueOf(dynProp.getValueString().toUpperCase().trim());
                                value.setTileModeX(tileMode);
                            }
                        }
                        break;
                        case tileModeY: {
                            if(!dynProp.getValueString().equals("disabled")) {
                                Shader.TileMode tileMode = Shader.TileMode.valueOf(dynProp.getValueString().toUpperCase().trim());
                                value.setTileModeY(tileMode);
                            }
                        }
                        break;
                        case mipMap: {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                value.setMipMap(dynProp.getValueBoolean());
                            }
                        }
                        break;
                        case autoMirrored: {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                value.setAutoMirrored(dynProp.getValueBoolean());
                            }
                        }
                        break;
                        case tint: {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                value.setTint(dynProp.getValueColor());
                            }
                        }
                        break;
                        case tintMode: {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                value.setTintMode(PorterDuff.Mode.valueOf(dynProp.getValueString().toUpperCase().trim()));
                            }
                        }
                        break;
                        case alpha: {
                            value.setAlpha(dynProp.getValueInt());
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

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
