package com.dvc.xml.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.ViewGroup;

import com.dvc.xml.XmlDynamicUtils;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Every Property of a xmlDrawable is a Dynaic Property
 */
public class XmlDrawableProperty {

    Context mContext;

    /**
     * 一些可能处理的类型
     * possible types that we handle
     **/
    public enum XD_TYPE {
        NO_VALID,
        STRING,
        DIMEN,
        INTEGER,
        FLOAT,
        COLOR,
        REF,
        BOOLEAN,
        BASE64,
        DRAWABLE,
        JSON,
        PATH,
    }

    public enum XD_NAME {
        SHAPE,
        INSET,
        SELECTOR,
        BITMAP,
        CLIP,
        COLOR,
        ROTATE,
        SCALE,
        NINE_PATCH,/*nine-patch*/
        LAYER_LIST,/*layer-list*/
        ANIMATION_LIST,/*animation-list*/

        SOLID,
        STROKE,
        SIZE,
        CORNERS,
        GRADIENT,
        PADDING,
        ITEM
    }


    /**
     * 一些可能处理的字段
     * possible property field that we handle
     **/
    public enum XD_FIELD {
        no_valid,
        /** shape **/
        visible,
        dither,
        innerRadiusRatio,
        thicknessRatio,
        innerRadius,
        thickness,
        useLevel,
        tint,
        tintMode,
        shape,
        /*** shape.solid.stroke.size ***/
        color,
        width,
        dashWidth,
        dashGap,
        height,
        /*** shape.corners ***/
        radius,
        topLeftRadius,
        topRightRadius,
        bottomLeftRadius,
        bottomRightRadius,
        /*** shape.gradient ***/
        startColor,
        centerColor,
        endColor,
        angle,
        type,
        centerX,
        centerY,
        gradientRadius,
        /*** shape.padding ***/
        left,
        top,
        right,
        bottom,
        /*** inset ***/
        drawable,
        inset,
        insetLeft,
        insetRight,
        insetTop,
        insetBottom,
        /*** clip ***/
        clipOrientation,
        /*** rotate ***/
        fromDegrees,
        toDegrees,
        pivotX,
        pivotY,
        /*** scale ***/
        scaleWidth,
        scaleHeight,
        scaleGravity,
        useIntrinsicSizeAsMinimum,
        /*** bitmap ***/
        src,
        antialias,
        filter,
        tileMode,
        tileModeX,
        tileModeY,
        mipMap,
        alpha,
        /*** selector ***/
        variablePadding,
        constantSize,
        enterFadeDuration,
        exitFadeDuration,
        autoMirrored,
        /*** animation-list ***/
        oneshot,
        /*** layer-list ***/
        opacity,
        paddingMode,
        paddingTop,
        paddingBottom,
        paddingLeft,
        paddingRight,
        paddingStart,
        paddingEnd,
        /*** item ***/
        duration,
        id,
        start,
        end,
        gravity,
        state_focused,
        state_window_focused,
        state_checkable,
        state_checked,
        state_selected,
        state_pressed,
        state_activated,
        state_active,
        state_single,
        state_first,
        state_middle,
        state_last,
        state_accelerated,
        state_hovered,
        state_drag_can_accept,
        state_drag_hovered,
        state_accessibility_focused
    }

    public enum Shape {
        RECTANGLE,
        OVAL,
        LINE,
        RING,
    }

    public enum Gradient {
        LINEAR,
        RADIAL,
        SWEEP,
    }

    public enum PaddingMode {
        NEST,
        STACK
    }

    public XD_NAME name;
    public XD_TYPE type;
    public XD_FIELD field;
    private Object value;
    /**
     * create property and parse xml
     * @param xmlPullParser : xml PullParser
     */
    public XmlDrawableProperty(Context context, XmlPullParser xmlPullParser, int i) {
        super();
        mContext = context;
        String xmlAttributeName = xmlPullParser.getAttributeName(i).split(":")[1];
        String xmlAttributeValue = xmlPullParser.getAttributeValue(i);
        name = XD_NAME.valueOf(xmlPullParser.getName().replace("-", "_").toUpperCase().trim());
        try {
            field = XD_FIELD.valueOf(xmlAttributeName/*.toUpperCase()*/.trim());
        } catch (Exception e) {
            field = XD_FIELD.no_valid;
        }
        try {
            type = getTYPE(xmlAttributeValue);
        } catch (Exception e) {
            type = XD_TYPE.NO_VALID;
        }

        try {
            value = convertValue(xmlAttributeValue);
        } catch (Exception e) {}
    }

    public XmlDrawableProperty(Context context, String name, ArrayList<XmlDrawableProperty> properties) {
        this.name = XD_NAME.valueOf(name.replace("-", "_").toUpperCase().trim());
        field = XD_FIELD.no_valid;
        type = XD_TYPE.NO_VALID;
        this.value = properties;
    }

    public boolean isValid() {
        return value!=null;
    }

    /**
     * @param v value to convert as string
     * @return Value as object depends on the type
     */
    private Object convertValue(Object v) {
        if (v==null)
            return null;
        switch (type) {
            case INTEGER: {
                return Integer.parseInt(v.toString());
            }
            case FLOAT: {
                return Float.parseFloat(v.toString());
            }
            case DIMEN: {
                return  convertDimenToPixel(v.toString());
            }
            case COLOR: {
                return convertColor(v.toString());
            }
            case BOOLEAN: {
                String value = v.toString();
                if (value.equalsIgnoreCase("t")) {
                    return true;
                } else if (value.equalsIgnoreCase("f")) {
                    return false;
                } else if (value.equalsIgnoreCase("true")) {
                    return true;
                } else if (value.equalsIgnoreCase("false")) {
                    return false;
                }
                return Integer.parseInt(value) == 1;
            }
            case BASE64: {
                try {
                    String mbyte = v.toString().replace("%BASE64:", "");
                    InputStream stream = new ByteArrayInputStream(Base64.decode(mbyte, Base64.DEFAULT));
                    return BitmapFactory.decodeStream(stream);
                }
                catch (Exception e) {
                    return null;
                }
            }
            case DRAWABLE: {

            }
        }
        return v;
    }

    /**
     * Get data types through NAME(Not completed)
     * @param v
     * @return
     */
    public XD_TYPE getTYPE(Object v){
        if(v.toString().startsWith("@")){
            return XD_TYPE.PATH;
        }
        switch (field) {
            case visible:
            case dither:
            case useLevel:
            case useIntrinsicSizeAsMinimum:
            case variablePadding:
            case constantSize:
            case autoMirrored:
            case oneshot:
            case state_focused:
            case state_window_focused:
            case state_checkable:
            case state_checked:
            case state_selected:
            case state_pressed:
            case state_activated:
            case state_active:
            case state_single:
            case state_first:
            case state_middle:
            case state_last:
            case state_accelerated:
            case state_hovered:
            case state_drag_can_accept:
            case state_drag_hovered:
            case state_accessibility_focused:
            case antialias:
            case filter:
            case mipMap:
                return XD_TYPE.BOOLEAN;
            case innerRadiusRatio:
            case thicknessRatio:
            case angle:
            case centerX:
            case centerY:
            case gradientRadius:
            case fromDegrees:
            case toDegrees:
            case pivotX:
            case pivotY:
            case alpha:
                return XD_TYPE.FLOAT;
            case innerRadius:
            case thickness:
            case width:
            case dashWidth:
            case dashGap:
            case height:
            case left:
            case top:
            case right:
            case bottom:
            case radius:
            case topLeftRadius:
            case topRightRadius:
            case bottomLeftRadius:
            case bottomRightRadius:
            case inset:
            case insetLeft:
            case insetRight:
            case insetTop:
            case insetBottom:
            case paddingTop:
            case paddingBottom:
            case paddingLeft:
            case paddingRight:
            case paddingStart:
            case paddingEnd:
            case start:
            case end:
                return XD_TYPE.DIMEN;
            case tint:
            case color:
            case startColor:
            case centerColor:
            case endColor:
                return XD_TYPE.COLOR;
            case scaleWidth:
            case scaleHeight:
                return XD_TYPE.STRING;
            case enterFadeDuration:
            case exitFadeDuration:
            case duration:
                return XD_TYPE.INTEGER;

            case src:
            case drawable:
                if(v.toString().startsWith("@"))
                    return XD_TYPE.PATH;
                else if(v.toString().startsWith("%ref:"))
                    return XD_TYPE.REF;
                else if(v.toString().startsWith("%BASE64:"))
                    return XD_TYPE.BASE64;
                return XD_TYPE.COLOR;
            case id:
            case gravity:
            case shape:
            case tintMode:
            case type:
            case clipOrientation:
            case scaleGravity:
            case opacity:
            case paddingMode:
            case tileMode:
            case tileModeX:
            case tileModeY:
                break;
        }
        return XD_TYPE.NO_VALID;
    }

    /** next function just cast value and return the object **/

    public int getValueColor() {
        if (type == XD_TYPE.COLOR) return Integer.class.cast(value);
        return -1;
    }
    public String getValueString() {
        return String.class.cast(value);
    }
    public int getValueInt() {
        if (value instanceof Integer)
            return Integer.class.cast(value);
        else if (value instanceof Float)
            return (int) getValueFloat();
        else
            return (int) value;
    }
    public float getValueFloat() {
        return Float.class.cast(value);
    }
    public Boolean getValueBoolean() {
        return Boolean.class.cast(value);
    }
    public Bitmap getValueBitmap() {
        return (Bitmap)value;
    }
    public Drawable getValueBitmapDrawable() {
        return new BitmapDrawable(Resources.getSystem(), getValueBitmap());
    }
    public Drawable getValueGradientDrawable() {
        return (Drawable)value;
    }
    public JSONObject getValueJSON() {
        return JSONObject.class.cast(value);
    }
    public Object getValueObject() {
        return value;
    }
    public List<XmlDrawableProperty> getValuePropertyList() {
        return (List<XmlDrawableProperty>) value;
    }

    int convertColor(String color) {
        if (color.startsWith("0x")) {
            return (int) Long.parseLong(color.substring(2), 16);
        }
        return Color.parseColor(color);
    }

    float convertDimenToPixel(String dimen) {
        if (dimen.endsWith("dp"))
            return XmlDynamicUtils.dpToPx(Float.parseFloat(dimen.substring(0, dimen.length() - 2)));
        else if (dimen.endsWith("sp"))
            return XmlDynamicUtils.spToPx(Float.parseFloat(dimen.substring(0, dimen.length() - 2)));
        else if (dimen.endsWith("px"))
            return Integer.parseInt(dimen.substring(0, dimen.length() - 2));
        else if (dimen.endsWith("%"))
            return (int)(Float.parseFloat(dimen.substring(0, dimen.length() - 1))/100f * XmlDynamicUtils.getDeviceWidth());
        else if (dimen.equalsIgnoreCase("match_parent"))
            return ViewGroup.LayoutParams.MATCH_PARENT;
        else if (dimen.equalsIgnoreCase("wrap_content"))
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        else
            return Integer.parseInt(dimen);
    }
}
