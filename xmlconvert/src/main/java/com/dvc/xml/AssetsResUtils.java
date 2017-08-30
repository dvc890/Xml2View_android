package com.dvc.xml;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.Xml;
import android.view.InflateException;

import com.dvc.xml.drawable.XmlDrawableUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dvc on 14/2/2015
 * Helper function that find Resources in Assets
 */
public class AssetsResUtils {

	public static final String TYPE_COLORS = "color";
	public static final String TYPE_DIMENS = "dimen";
	public static final String TYPE_STRINGS = "string";
	public static final String TYPE_DRAWABLE = "drawable";
	public static final String TYPE_MIPMAP = "mipmap";
	

	/**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * 
     * @param pxValue
     * @return
     */ 
    public static float px2dip(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (pxValue / scale + 0.5f); 
    } 
   
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     * @return
     */ 
    public static float dip2px(Context context, float dipValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (dipValue * scale + 0.5f); 
    } 
    
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @return
     */ 
    public static float px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @return
     */ 
    public static float sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (spValue * fontScale + 0.5f); 
    }

    public static int getAndroidResId(String type, String idname) {
		Class<?> clazz;
		int resid = -1;
		try {
			clazz = Class.forName("com.android.internal.R$"+type);
			Object object = clazz.newInstance();
			try {
				resid = Integer.parseInt(clazz.getField(idname).get(object).toString());
			} catch (IllegalArgumentException | NoSuchFieldException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return resid;
	}

    public static Object getAssetValue(Context context, String path){
    	Object result = null;
    	String key[] = null;
    	
    	if(path.indexOf("@") != -1)
    		key = path.substring(1).split("/");
		else if(path.startsWith("R."))
			key = path.substring(2).split(".");
    	else
    		key = path.split("/");
    	key[0] = key[0].toLowerCase().trim();
    	if(key[0].equalsIgnoreCase(TYPE_STRINGS))
    		result = getString(context, key[1]);
    	else if(key[0].equalsIgnoreCase(TYPE_DIMENS))
			if(path.startsWith("@android:"))
				result = context.getResources().getDimension(getAndroidResId(TYPE_DIMENS, key[1]));
			else
				result = getDimen(context, key[1]);
    	else if(key[0].equalsIgnoreCase(TYPE_COLORS)) {
			if(path.startsWith("@android:"))
				result = context.getResources().getColor(getAndroidResId(TYPE_COLORS, key[1]));
			else
				result = getColor(context, key[1]);
		}else if(key[0].equalsIgnoreCase(TYPE_DRAWABLE) || key[0].equalsIgnoreCase(TYPE_MIPMAP))
			result = getAssetDrawable(context, path);
    	return result;
    }
    
    public static String getString(Context context, String key) {
		String name = key;
		if(key.startsWith("R.")){
			String[] keys = key.split(".");
			name = keys[keys.length-1];
		}
		return getAssetXmlValue(context, TYPE_STRINGS, name);
    }
    
    public static float getDimen(Context context, String key) {
    	float value = 0.0f;
		String name = key;
		if(key.startsWith("R.")){
			String[] keys = key.split(".");
			name = keys[keys.length-1];
		}
    	String valuestr = getAssetXmlValue(context, TYPE_DIMENS, name);
    	if(valuestr.length() > 0){
    		if(valuestr.toLowerCase().contains("dip") || valuestr.toLowerCase().contains("dp")){
    			value = Float.valueOf(valuestr.toLowerCase().replace("dip", "").replace("dp", ""));
    			value = dip2px(context, value);
    		}else if(valuestr.toLowerCase().contains("sp")){
    			value = Float.valueOf(valuestr.toLowerCase().replace("sp", ""));
    			//value = sp2px(context, value);
    		}
    	}
		return value;
    }
    
    public static int getColor(Context context, String key) {
    	int value = -1;
		String name = key;
		if(key.startsWith("R.")){
			String[] keys = key.split(".");
			name = keys[keys.length-1];
		}
    	String valuestr = getAssetXmlValue(context, TYPE_COLORS, name);
    	if(valuestr.length() > 0){
    		valuestr = valuestr.replace("#", "");
	    	if (valuestr.length() == 6) {  
	    		value = Color.rgb(  
	                  Integer.valueOf(valuestr.substring(0, 2), 16),  
	                  Integer.valueOf(valuestr.substring(2, 4), 16),  
	                  Integer.valueOf(valuestr.substring(4, 6), 16));  
	      } else if (valuestr.length() == 8) {  
	    	  value = Color.argb(  
	                  Integer.valueOf(valuestr.substring(0, 2), 16),  
	                  Integer.valueOf(valuestr.substring(2, 4), 16),  
	                  Integer.valueOf(valuestr.substring(4, 6), 16),  
	                  Integer.valueOf(valuestr.substring(6, 8), 16));  
	      }  
    	}
    	return value;
    }
    public static Drawable getAssetDrawable(Context context, String name){
    	Drawable value = null;
    	String path = name+".png";
		if(name.contains("@null"))
			return new PaintDrawable(Color.TRANSPARENT);
		if(name.toLowerCase().contains("@color/"))
			return new ColorDrawable((Integer) getAssetValue(context, name));
    	if(name.startsWith("@"))
    		path = path.substring(1);
		else if(name.startsWith("R."))
			path = name.substring(2).replace(".","/")+".png";
    	try {
			value = new BitmapDrawable(BitmapFactory.decodeStream(context.getResources().getAssets().open(path)));
		} catch (IOException e) {
			path = name+".jpg";
	    	if(path.startsWith("@"))
	    		path = path.substring(1);
			else if(name.startsWith("R."))
				path = name.substring(2).replace(".","/")+".jpg";
			try {
				value = new BitmapDrawable(BitmapFactory.decodeStream(context.getResources().getAssets().open(path)));
			} catch (IOException e1) {
				path = name+".xml";
				if(path.startsWith("@"))
					path = path.substring(1);
				else if(name.startsWith("R."))
					path = name.substring(2).replace(".","/")+".xml";
				try {
					value = XmlDrawableUtils.createXmlDrawable(context, path);
				} catch (IOException e2) {
					throw new InflateException("dvc: getAssetDrawable Not Found \""+name+"\"");
				}
			}
		}
		return value;
    }
    
    public static String getAssetAttrsformatValue(Context context, String className, String funtionName) {
    	InputStream is = null;
		try {
			is = context.getResources().getAssets().open("values/attrs.xml");
			XmlPullParser xrp = Xml.newPullParser();  
			xrp.setInput(is, "UTF-8");
			int type;
			while ((type=xrp.next()) != XmlPullParser.START_TAG &&
					type != XmlPullParser.END_DOCUMENT) {
				// Empty loop
			}
			if (type != XmlPullParser.START_TAG) {
	            throw new InflateException(xrp.getPositionDescription()
	                    + ": No start tag found!");
	        }
	        if(xrp.getName().equals("resources")){
    	        type = xrp.next();
    	        boolean isnotthisDepth = false;
    	        while (type != XmlPullParser.END_DOCUMENT) {
    	        	if(xrp.getName() != null && xrp.getName().equals("declare-styleable")){
            	        int depth = xrp.getDepth();

		    	        for(int i = 0; i < xrp.getAttributeCount(); i++){
	    	                String xmlAttributeName = xrp.getAttributeName(i);
	    	                String xmlAttributeValue = xrp.getAttributeValue(i);
	    	                if(xmlAttributeName.equals("name") && className.contains(xmlAttributeValue)){
	    	                	while(xrp.next() != XmlPullParser.END_DOCUMENT ){
	    	                		if(xrp.getName() == null)
	    	                			continue;
	    	                		if(xrp.getDepth() <= depth)
	    	                			break;
		    	    	        	if(xrp.getName().equals("attr")){
		    	    	        		String name="",format="";
		    			    	        for(int i2 = 0; i2 < xrp.getAttributeCount(); i2++){
		    		    	                if(xrp.getAttributeName(i2).equals("name")){
		    		    	                	name = xrp.getAttributeValue(i2);
		    		    	                }
		    		    	                else if(xrp.getAttributeName(i2).equals("format")){
		    		    	                	format = xrp.getAttributeValue(i2);
		    		    	                }
		    			    	        }
		    			    	        if(name.contains(funtionName))
		    			    	        	return format;
		    	    	        	}
	    	                	}
	    	                }
    	                }
    	                	
    	        	}
	                type = xrp.next();
    	        }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return null;
    }
    
    private static String getAssetXmlValue(Context context, String type, String key){
    	String value = "";
    	InputStream is = null;
    	try {
    		is = context.getResources().getAssets().open("values/"+type+"s.xml");
    		XmlPullParser xrp = Xml.newPullParser();  
    		xrp.setInput(is, "UTF-8"); 
    		boolean find = false;
    		while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) { 
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                	if(xrp.getName().equals(type))
                		find = xrp.getAttributeValue(0).equals(key);
                }else if (xrp.getEventType() == XmlPullParser.END_TAG) {
                	
                }else if (xrp.getEventType() == XmlPullParser.TEXT) {
                	if(find){
                		value = xrp.getText();
                		break;
                	}
                }
                xrp.next();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return value;
    }
}
