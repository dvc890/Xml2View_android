package com.dvc.xml;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dvc on 17/7/2017.
 * parse the json as a tree and create View with its dynamicProperties
 */
public class XmlDynamicView {

    private static final String TAG_MERGE = "merge";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_1995 = "blink";
    private static final String TAG_REQUEST_FOCUS = "requestFocus";
    private static final String TAG_TAG = "tag";
    static int mCurrentId = 13;
    static int INTERNAL_TAG_ID = 0x7f020000;
    static final Class<?>[] mConstructorSignature = new Class[] {
            Context.class, AttributeSet.class};
    private static boolean DEBUG = true;

    /**
     * @param context
     * @param xmlPath : a XML file path in the assets path
     * @param object : use @XmlDynamicViewId to find the view in the object
     * @return the view that created
     */
    public static View createView (Context context, String xmlPath, Object object) {
        return createView(context, xmlPath, null, object);
    }

    /**
     * @param context
     * @param xmlPath : a XML file path in the assets path
     * @param parent : parent viewGroup
     * @param object : use @XmlDynamicViewId to find the view in the object
     * @return the view that created
     */
    public static View createView (Context context, String xmlPath, ViewGroup parent, Object object) {
        XmlPullParser xmlPullParser = null;
        InputStream is = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();
            factory.setValidating(true);
            is = context.getAssets().open(xmlPath);
            try {
                xmlPullParser.setInput(is, "utf-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        View view = createView(context, xmlPullParser, parent, object);
        if(is != null)
            try {is.close();} catch (IOException e) {}
        return view;
    }

    /**
     * @param xmlPullParser : xml PullParser
     * @param object : use @XmlDynamicViewId to find the view in the object
     * @return the view that created
     */
    public static View createView (Context context, XmlPullParser xmlPullParser, Object object) {
        return createView(context, xmlPullParser, null, object);
    }


    /**
     * @param xmlPullParser : xml PullParser
     * @param parent : parent viewGroup
     * @param object : class that will be created as an holder and attached as a tag in the View, If contains HashMap ids will replaced with idsMap
     * @return the view that created
     */
    public static View createView (Context context, XmlPullParser xmlPullParser, ViewGroup parent, Object object) {

        if(xmlPullParser == null )
            return null;
        HashMap<String, Integer> ids = new HashMap<>();

        try {
            if (xmlPullParser.next() != XmlPullParser.START_TAG) {
                throw new InflateException(xmlPullParser.getPositionDescription()
                        + ": No start tag found!");
            }
        } catch (XmlPullParserException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(parent == null){
            parent = new FrameLayout(context);
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        View container = createViewInternal(context, xmlPullParser, parent, ids);

        if (container==null)
            return null;

        if (container.getTag(INTERNAL_TAG_ID) != null)
            XmlDynamicUtils.applyLayoutProperties(container, (List<XmlDynamicProperty>) container.getTag(INTERNAL_TAG_ID), parent, ids);

        /* clear tag from properties */
        container.setTag(INTERNAL_TAG_ID, null);

        if (object!= null) {
            if(object instanceof Class<?>) {
                try {
                    Object holder = ((Class<?>)object).getConstructor().newInstance();
                    XmlDynamicUtils.parseDynamicView(holder, container, ids);
                    parent.setTag(holder);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                XmlDynamicUtils.parseDynamicView(object, container, ids);
                parent.setTag(ids);
            }

        } else {
            parent.setTag(ids);
        }

        parent.addView(container);
        return parent;
    }

    /**
     * @param layoutid : include layoutid
     * @param parent : parent viewGroup
     * @param ids : the hashMap where we keep ids as string from json to ids as int in the layout
     * @return the includelayout that created
     */
    public static View createIncludeView (Context context, String layoutid, ViewGroup parent, HashMap<String, Integer> ids, ArrayList<XmlDynamicProperty> properties) {
        XmlPullParser xmlPullParser = null;
        InputStream is = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();
            factory.setValidating(true);
            is = context.getAssets().open(layoutid.substring(1)+".xml");
            try {
                xmlPullParser.setInput(is, "utf-8");
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if(xmlPullParser == null )
            return null;

        try {
            if (xmlPullParser.next() != XmlPullParser.START_TAG) {
                throw new InflateException(xmlPullParser.getPositionDescription()
                        + ": No start tag found!");
            }
        } catch (XmlPullParserException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        View container = createViewInternal(context, xmlPullParser, parent, ids);

        if (container==null)
            return null;

        if (container.getTag(INTERNAL_TAG_ID) != null)
            XmlDynamicUtils.applyLayoutProperties(container, (List<XmlDynamicProperty>) container.getTag(INTERNAL_TAG_ID), parent, ids);
        if(properties.size() > 0) {
            XmlDynamicUtils.applyStyleProperties(container, properties);
        }

        if(is != null)
            try {is.close();} catch (IOException e) {}

        return container;
    }


    /**
     * use internal to parse the json as a tree to create View
     * @param xmlPullParser : xml PullParser
     * @param ids : the hashMap where we keep ids as string from json to ids as int in the layout
     * @return the view that created
     */
    private static View createViewInternal (Context context, XmlPullParser xmlPullParser, ViewGroup parent, HashMap<String, Integer> ids) {
        View view = null;

        ArrayList<XmlDynamicProperty> properties;

        // Look for the root node.
        int type;
        String widget = xmlPullParser.getName();
        try {


            if (DEBUG ) {
                System.out.println("**************************");
                System.out.println("Creating root view: "
                        + widget);
                System.out.println("**************************");
            }
            if (TAG_INCLUDE.equals(widget)) {
                if (xmlPullParser.getDepth() == 0) {
                    throw new InflateException("<include /> cannot be the root element");
                }
                String layoutid = "";
                properties = new ArrayList<>();
                for(int i = 0; i < xmlPullParser.getAttributeCount(); i++){
                    if(xmlPullParser.getAttributeName(i).equals("layout")){
                        layoutid = xmlPullParser.getAttributeValue(i);
                    }else {
                        XmlDynamicProperty p = new XmlDynamicProperty(context, xmlPullParser, i);
                        if (p.isValid())
                            properties.add(p);
                    }
                }
                if(layoutid.length()>0)
                    /* create a include layout view */
                    return createIncludeView(context, layoutid, parent, ids, properties);
            } else{
                if (!xmlPullParser.getName().contains(".")) {
                    widget = "android.widget." + xmlPullParser.getName();
                    if(!XmlDynamicUtils.classExists(widget)){
                        widget = "android.view." + xmlPullParser.getName();
                    }
                }
                Class<?> viewClass = Class.forName(widget);
                try{
                    view = (View) viewClass.getConstructor(Context.class).newInstance(new Object[] { context });
                } catch (NoSuchMethodException e) {
                    try {
                        view = (View) viewClass.getConstructor(mConstructorSignature).newInstance(new Object[] { context, null });
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (view==null) return null;

        /* default Layout in case the user not set it */
        ViewGroup.LayoutParams params = XmlDynamicUtils.createLayoutParams(parent);
        view.setLayoutParams(params);

        /* iterrate json and get all properties in array */
        properties = new ArrayList<>();
        for(int i = 0; i < xmlPullParser.getAttributeCount(); i++){
            XmlDynamicProperty p = new XmlDynamicProperty(context, xmlPullParser, i);
            if (p.isValid())
                properties.add(p);
        }
        /* keep properties obj as a tag */
        view.setTag(INTERNAL_TAG_ID, properties);

        XmlDynamicUtils.applyLayoutProperties(view, properties, parent, ids);
        /* add and integer as a universal id  and keep it in a hashmap */
        String id = XmlDynamicUtils.applyStyleProperties(view, properties);
        if (!TextUtils.isEmpty(id)) {
            /* to target older versions we cannot use View.generateViewId();  */
            mCurrentId = Math.abs(id.hashCode());
            ids.put(id, Integer.valueOf(mCurrentId));
            view.setId(mCurrentId);
//            ids.put(id, mCurrentId);
//            view.setId( mCurrentId );
//            mCurrentId++;
        }
        /* if view is type of ViewGroup check for its children view in xml */
        if (view instanceof ViewGroup) {

            ViewGroup viewGroup = (ViewGroup) view;
            /* parse the aray to get the children views */
            List<View> views = new ArrayList<>();
            try {
                final int depth = xmlPullParser.getDepth();
                while (((type = xmlPullParser.next()) != XmlPullParser.END_TAG ||
                        xmlPullParser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

                    if (type != XmlPullParser.START_TAG) {
                        continue;
                    }
                    /* create every child add it in viewGroup and set its tag with its properties */
                    View dynamicChildView = XmlDynamicView.createViewInternal(context, xmlPullParser, viewGroup, ids);
                    if (dynamicChildView!=null) {
                        views.add(dynamicChildView);
                        viewGroup.addView(dynamicChildView);
                    }
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            /* after create all the children apply layout properties
            * we need to do this after al children creation to have create all possible ids */
            for(View v : views) {
                XmlDynamicUtils.applyLayoutProperties(v, (List<XmlDynamicProperty>) v.getTag(INTERNAL_TAG_ID), viewGroup, ids);
                /* clear tag from properties */
                v.setTag(INTERNAL_TAG_ID, null);
            }
        }

        return view;
    }
}
