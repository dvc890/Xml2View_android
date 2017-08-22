package com.dvc.xml.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.InflateException;

import com.dvc.xml.drawable.XmlDrawableProperty.XD_NAME;
import com.dvc.xml.drawable.utils.XmlAnimationDrawableUtils;
import com.dvc.xml.drawable.utils.XmlLayerDrawableUtils;
import com.dvc.xml.drawable.utils.XmlShapeDrawableUtils;
import com.dvc.xml.drawable.utils.XmlStateListDrawableUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in drawable
 */
public class XmlDrawableUtils {
	/**
	 * Parse XML file to create a Drawable
	 * @param context
	 * @param xmlPath xml file path
	 * @return
	 * @throws IOException
	 */
	public static Drawable createXmlDrawable(Context context, String xmlPath) throws IOException {
		XmlPullParser xmlPullParser = null;
		try {
	        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        xmlPullParser = factory.newPullParser();
	        factory.setValidating(true);
            InputStream is = context.getAssets().open(xmlPath);
        	xmlPullParser.setInput(is, "utf-8");

        } catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}

        return createXmlDrawable(context, xmlPullParser);
	}

	/**
	 * Parse XML data to create a Drawable
	 * @param context
	 * @param xmlPullParser
	 * @return
	 */
	public static Drawable createXmlDrawable (Context context, XmlPullParser xmlPullParser) {
        try {
			if (xmlPullParser.next() != XmlPullParser.START_TAG) {
			    throw new InflateException(xmlPullParser.getPositionDescription()
			            + ": No start tag found!");
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
        String namestr = xmlPullParser.getName();
        ArrayList<XmlDrawableProperty> properties = createXmlDrawableProperties(context, xmlPullParser);
        
		return generateDrawable(context, namestr, properties);
	}

	/**
	 * Parse XML data to create Properties list
	 * @param context
	 * @param xmlPullParser
	 * @return
	 */
	private static ArrayList<XmlDrawableProperty> createXmlDrawableProperties (Context context, XmlPullParser xmlPullParser) {
        ArrayList<XmlDrawableProperty> properties;

        int type;
        properties = new ArrayList<>();
        for(int i = 0; i < xmlPullParser.getAttributeCount(); i++){
        	XmlDrawableProperty p = new XmlDrawableProperty(context, xmlPullParser, i);
            if (p.isValid())
                properties.add(p);
        }
        
        final int depth = xmlPullParser.getDepth();
        try {
			while (((type = xmlPullParser.next()) != XmlPullParser.END_TAG ||
					xmlPullParser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

			    if (type != XmlPullParser.START_TAG) {
			        continue;
			    }
			    String name = xmlPullParser.getName();
			    ArrayList<XmlDrawableProperty> properties_m = createXmlDrawableProperties(context, xmlPullParser);
			    XmlDrawableProperty p = new XmlDrawableProperty(context, name, properties_m);
			    properties.add(p);
			    
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}
        
		return properties;
	}

	/**
	 * Create a Drawable with XML properties
	 * @param mContext
	 * @param namestr Type string of the XML picture
	 * @param properties XML properties lists
	 * @return
	 */
	public static Drawable generateDrawable(Context mContext, String namestr, List<XmlDrawableProperty> properties) {
		Drawable value = null;
        XD_NAME name = XD_NAME.valueOf(namestr.replace("-", "_").toUpperCase().trim());
		switch (name) {
		case SHAPE:
			value = XmlShapeDrawableUtils.generateDrawable(mContext, properties);
			break;
		case SELECTOR:
			value = XmlStateListDrawableUtils.generateDrawable(mContext, properties);
			break;
		case INSET:
			break;
		case BITMAP:
			break;
		case CLIP:
			break;
		case COLOR:
			break;
		case ROTATE:
			break;
		case SCALE:
			break;
		case NINE_PATCH:/*nine-patch*/
		break;
		case LAYER_LIST:/*layer-list*/
			value = XmlLayerDrawableUtils.generateDrawable(mContext, properties);
			break;
		case ANIMATION_LIST:/*animation-list*/
			value = XmlAnimationDrawableUtils.generateDrawable(mContext, properties);
			break;

		default:
			break;
		}
		return value;
	}
}
