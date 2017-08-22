package com.dvc.xml;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.ViewGroup;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by dvc on 17/7/2017.
 * Every Property of a View is a Dynaic Property
 */
public class XmlDynamicProperty {

    Context mContext;

    /**
     * 一些可能处理的类型
     * possible types that we handle
     **/
    public enum TYPE {
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
        PATH
    }

    /**
     * 一些可能处理的字段
     * possible property field that we handle
     **/
    public enum FIELD {
        NO_VALID,
        ID,
        LAYOUT_WIDTH,
        LAYOUT_HEIGHT,
        PADDINGLEFT,
        PADDINGRIGHT,
        PADDINGTOP,
        PADDINGBOTTOM,
        PADDING,
        LAYOUT_MARGINLEFT,
        LAYOUT_MARGINRIGHT,
        LAYOUT_MARGINTOP,
        LAYOUT_MARGINBOTTOM,
        LAYOUT_MARGIN,
        BACKGROUND,
        ENABLED,
        SELECTED,
        CLICKABLE,
        SCALEX,
        SCALEY,
        MINWIDTH,
        MINHEIGHT,
        MAXWIDTH,
        MAXHEIGHT,
        VISIBILITY,
        /* textView */
        TEXT,
        HINT,
        TEXTCOLOR,
        TEXTSIZE,
        TEXTSTYLE,
        ELLIPSIZE,
        MAXLINES,
        GRAVITY,
        DRAWABLETOP,
        DRAWABLEBOTTOM,
        DRAWABLELEFT,
        DRAWABLERIGHT,
        /* imageView */
        SRC,
        SCALETYPE,
        ADJUSTVIEWBOUNDS,
        /* SeekBar */
        MAX,
        PROGRESS,
        SPLITTRACK,
        THUMB,
        THUMBOFFSET,
        PROGRESSDRAWABLE,
        /* layout */
        LAYOUT_ABOVE,
        LAYOUT_ALIGNBASELINE,
        LAYOUT_ALIGNBOTTOM,
        LAYOUT_ALIGNEND,
        LAYOUT_ALIGNLEFT,
        LAYOUT_ALIGNPARENTBOTTOM,
        LAYOUT_ALIGNPARENTEND,
        LAYOUT_ALIGNPARENTLEFT,
        LAYOUT_ALIGNPARENTRIGHT,
        LAYOUT_ALIGNPARENTSTART,
        LAYOUT_ALIGNPARENTTOP,
        LAYOUT_ALIGNRIGHT,
        LAYOUT_ALIGNSTART,
        LAYOUT_ALIGNTOP,
        LAYOUT_ALIGNWITHPARENTIFMISSING,
        LAYOUT_BELOW,
        LAYOUT_CENTERHORIZONTAL,
        LAYOUT_CENTERINPARENT,
        LAYOUT_CENTERVERTICAL,
        LAYOUT_TOENDOF,
        LAYOUT_TOLEFTOF,
        LAYOUT_TORIGHTOF,
        LAYOUT_TOSTARTOF,
        LAYOUT_GRAVITY,
        LAYOUT_WEIGHT,
        WEIGHTSUM,
        ORIENTATION,

        FUNCTION,
        TAG,
    }

    public FIELD field;
    public TYPE type;
    private Object value;
    private String functionstr;

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
                //come soon
            }
        }
        return v;
    }

    /**
     * create property and parse xml
     * @param xmlPullParser : xml PullParser
     */
    public XmlDynamicProperty(Context context, XmlPullParser xmlPullParser, int i) {
        super();
        mContext = context;
        String xmlAttributeName = xmlPullParser.getAttributeName(i).split(":")[1];
        String xmlAttributeValue = xmlPullParser.getAttributeValue(i);
        boolean iscustom = (!xmlPullParser.getAttributeName(i).split(":")[0].equals("android") && !xmlPullParser.getAttributeName(i).split(":")[0].equals("xmlns"));
        try {
            if(iscustom){
                field = FIELD.FUNCTION;
                functionstr = xmlAttributeName;
            }else
                field = FIELD.valueOf(xmlAttributeName.toUpperCase().trim());
        } catch (Exception e) {
            field = FIELD.NO_VALID;
        }
        try {
            if(iscustom){
                if(xmlAttributeValue.startsWith("@"))
                    type = TYPE.PATH;
                else
                    type = getAttrsType(mContext,xmlPullParser.getName(),xmlAttributeName);
            }else
                type = getTYPE(xmlAttributeValue);
        } catch (Exception e) {
            type = TYPE.NO_VALID;
        }
        try {
            value = convertValue(xmlAttributeValue);
        } catch (Exception e) {}
    }

    public boolean isValid() {
        return value!=null;
    }

    /**
     * Get data types through field(Not completed)
     * @param v
     * @return
     */
    public TYPE getTYPE(Object v){
        if(v.toString().startsWith("@")){
            return TYPE.PATH;
        }
        switch (field) {
            case LAYOUT_WIDTH:
            case LAYOUT_HEIGHT:
            case PADDINGLEFT:
            case PADDINGRIGHT:
            case PADDINGTOP:
            case PADDINGBOTTOM:
            case PADDING:
            case LAYOUT_MARGINLEFT:
            case LAYOUT_MARGINRIGHT:
            case LAYOUT_MARGINTOP:
            case LAYOUT_MARGINBOTTOM:
            case LAYOUT_MARGIN:
            case SCALEX:
            case SCALEY:
            case MINWIDTH:
            case MINHEIGHT:
            case MAXWIDTH:
            case MAXHEIGHT:
            case TEXTSIZE:
            case THUMBOFFSET:
                return TYPE.DIMEN;
            case BACKGROUND:
                if(v.toString().startsWith("@"))
                    return TYPE.PATH;
                else if(v.toString().startsWith("%ref:"))
                    return TYPE.REF;
                else if(v.toString().startsWith("%BASE64:"))
                    return TYPE.BASE64;
                return TYPE.COLOR;
            case ENABLED:
            case SELECTED:
            case CLICKABLE:
            case LAYOUT_CENTERHORIZONTAL:
            case LAYOUT_CENTERINPARENT:
            case LAYOUT_CENTERVERTICAL:
            case ADJUSTVIEWBOUNDS:
            case SPLITTRACK:
                return TYPE.BOOLEAN;
            case TEXT:
            case HINT:
                if(v.toString().startsWith("@"))
                    return TYPE.PATH;
                else if(v.toString().startsWith("%ref:"))
                    return TYPE.REF;
            case VISIBILITY:
            case GRAVITY:
            case ORIENTATION:
            case LAYOUT_GRAVITY:
            case ELLIPSIZE:
            case SCALETYPE:
                return TYPE.STRING;
            case TEXTCOLOR:
                return TYPE.COLOR;
            case MAXLINES:
            case MAX:
            case PROGRESS:
                return TYPE.INTEGER;
            case LAYOUT_WEIGHT:
            case WEIGHTSUM:
                return TYPE.FLOAT;
            case TEXTSTYLE:
            case DRAWABLETOP:
            case DRAWABLEBOTTOM:
            case DRAWABLELEFT:
            case DRAWABLERIGHT:
            case SRC:
            case PROGRESSDRAWABLE:
            case THUMB:
                if(v.toString().startsWith("@"))
                    return TYPE.PATH;
                else if(v.toString().startsWith("%ref:"))
                    return TYPE.REF;
                else if(v.toString().startsWith("%BASE64:"))
                    return TYPE.BASE64;
                break;
            case LAYOUT_ABOVE:
            case LAYOUT_ALIGNBASELINE:
            case LAYOUT_ALIGNBOTTOM:
            case LAYOUT_ALIGNEND:
            case LAYOUT_ALIGNLEFT:
            case LAYOUT_ALIGNPARENTBOTTOM:
            case LAYOUT_ALIGNPARENTEND:
            case LAYOUT_ALIGNPARENTLEFT:
            case LAYOUT_ALIGNPARENTRIGHT:
            case LAYOUT_ALIGNPARENTSTART:
            case LAYOUT_ALIGNPARENTTOP:
            case LAYOUT_ALIGNRIGHT:
            case LAYOUT_ALIGNSTART:
            case LAYOUT_ALIGNTOP:
            case LAYOUT_ALIGNWITHPARENTIFMISSING:
            case LAYOUT_BELOW:
            case LAYOUT_TOENDOF:
            case LAYOUT_TOLEFTOF:
            case LAYOUT_TORIGHTOF:
            case LAYOUT_TOSTARTOF:

            case TAG:
                return TYPE.NO_VALID;
        }

        return TYPE.NO_VALID;
    }

    public static TYPE getAttrsType(Context context, String className, String funtionName){
        String format = AssetsResUtils.getAssetAttrsformatValue(context, className, funtionName);
        if(format == null)
            return TYPE.NO_VALID;
        if(format.contains("string")){
            return TYPE.STRING;
        }else if(format.contains("color")){
            return TYPE.COLOR;
        }else if(format.contains("dimension")){
            return TYPE.DIMEN;
        }else if(format.contains("boolean")){
            return TYPE.BOOLEAN;
        }else if(format.contains("float")){
            return TYPE.FLOAT;
        }else if(format.contains("integer")){
            return TYPE.INTEGER;
        }else if(format.contains("reference")){
            return TYPE.REF;
        }
        return TYPE.NO_VALID;
    }

    /**
     * @param clazz
     * @param varName
     * @return search in clazz of possible variable field (varName) and return its value
     */
    public static Object getValueInt(Class clazz, String varName) {

        java.lang.reflect.Field fieldRequested = null;

        try {
            fieldRequested = clazz.getField(varName);
            if (fieldRequested!=null) {
                return fieldRequested.get(clazz);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    /** next function just cast value and return the object **/

    public int getValueColor() {
        if (type == TYPE.COLOR) return Integer.class.cast(value);
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

    public String getFunctionStr(){
        return functionstr;
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
