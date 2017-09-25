package com.dvc.xml.drawable.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in shapedrawable
 */
public class XmlShapeDrawableUtils {

	/**
	 * Generate a ShapeDrawable
	 * @param mContext
	 * @param properties
	 */
	@SuppressLint("NewApi")
	public static GradientDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
		GradientDrawable value = new GradientDrawable();
		
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case SHAPE: {
				switch (dynProp.field) {
				case visible:{
					value.setVisible(dynProp.getValueBoolean(), dynProp.getValueBoolean());
				}
				break;
				case dither:{
					value.setDither(dynProp.getValueBoolean());
				}
				break;
				case tint:{
					if(android.os.Build.VERSION.SDK_INT >21)
						value.setTint(dynProp.getValueColor());
				}
				break;
				case shape:{
					XmlDrawableProperty.Shape shape = XmlDrawableProperty.Shape.valueOf(dynProp.getValueString().toUpperCase().trim());
					switch (shape) {
					case LINE:
						value.setShape(GradientDrawable.LINE);
						break;
					case OVAL:
						value.setShape(GradientDrawable.OVAL);
						break;
					case RECTANGLE:
						value.setShape(GradientDrawable.RECTANGLE);
						break;
					case RING:
						break;
					}
				}
				break;
				case useLevel:{
					value.setUseLevel(dynProp.getValueBoolean());
				}
				break;
				case tintMode:{
					// to be support
				}
				break;
				case innerRadiusRatio:{
					// to be support
				}
				break;
				case thicknessRatio:{
					// to be support
				}
				break;
				case innerRadius:{
					// to be support
				}
				break;
				case thickness:{
					// to be support
				}
				break;
				}
			}
			break;

			case SOLID: {
				applySolidProperty(mContext, value ,dynProp.getValuePropertyList());
			}
			break;
			case STROKE: {
				applyStrokeProperty(mContext, value, dynProp.getValuePropertyList());
			}
			break;
			case SIZE: {
				applySizeProperty(value, dynProp.getValuePropertyList());
			}
			break;
			case CORNERS: {
				applyCornersProperty(value, dynProp.getValuePropertyList());
			}
			break;
			case GRADIENT: {
				applyGradientProperty(mContext, value, dynProp.getValuePropertyList());
			}
			break;
			case PADDING: {
				applyPaddingProperty(value, dynProp.getValuePropertyList());
			}
			break;
			}
		}

		return value;
	}

	/**
	 * apply some solid properties in ShapeDrawable
	 */
	public static void applySolidProperty(Context mContext, GradientDrawable value, List<XmlDrawableProperty> properties){

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case color:{
				switch (dynProp.type) {
					case PATH: {
						value.setColor((Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString()));
					}
					break;
					case COLOR: {
						value.setColor(dynProp.getValueColor());
					}
					break;
				}
			}
			break;

			}
		}
	}

	/**
	 * apply some stroke properties in ShapeDrawable
	 */
	public static void applyStrokeProperty(Context mContext, GradientDrawable value, List<XmlDrawableProperty> properties){
		/** STROKE field **/
		int st_c = 0,st_w = 0;
		float st_dw=0f,st_dg=0f;

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case color:{
				switch (dynProp.type) {
					case PATH: {
						st_c = (Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString());
					}
					break;
					case COLOR: {
						st_c = dynProp.getValueColor();
					}
					break;
				}
			}
			break;
			case width:{
				st_w=dynProp.getValueInt();
			}
			break;
			case dashWidth:{
				st_dw=dynProp.getValueFloat();
			}
			break;
			case dashGap:{
				st_dg=dynProp.getValueFloat();
			}
			break;
			}
		}
		if(st_dw!=0f && st_dg!=0f){
			value.setStroke(st_w, st_c, st_dw, st_dg);
		}else{
			value.setStroke(st_w, st_c);
		}
	}

	/**
	 * apply some size properties in ShapeDrawable
	 */
	public static void applySizeProperty(GradientDrawable value, List<XmlDrawableProperty> properties){
		/** SIZE field **/
		int sz_w=0, sz_h=0;

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case width:{
				sz_w=dynProp.getValueInt();
			}
			break;
			case height:{
				sz_h=dynProp.getValueInt();
			}
			break;
			}
		}
		if(sz_w!=0 && sz_h!=0)
			value.setSize(sz_w, sz_h);
	}

	/**
	 * apply some corners properties in ShapeDrawable
	 */
	public static void applyCornersProperty(GradientDrawable value, List<XmlDrawableProperty> properties){
		/** CORNERS field **/
		float cr_r=0f,cr_tl=0,cr_tr=0,cr_bl=0,cr_br=0;
		
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case radius:{
				cr_r = dynProp.getValueFloat();
			}
			break;
			case topLeftRadius:{
				cr_tl = dynProp.getValueFloat();
			}
			break;
			case topRightRadius:{
				cr_tr = dynProp.getValueFloat();
			}
			break;
			case bottomLeftRadius:{
				cr_bl = dynProp.getValueFloat();
			}
			break;
			case bottomRightRadius:{
				cr_br = dynProp.getValueFloat();
			}
			break;
			}
		}
		
		if(cr_r != 0f)
			value.setCornerRadius(cr_r);
		else
			value.setCornerRadii(
					new float[]
							{
							cr_tl, cr_tl,
							cr_tr, cr_tr,
							cr_br, cr_br,
							cr_bl, cr_bl
							}
					);

	}

	/**
	 * apply some gradient properties in ShapeDrawable
	 */
	@SuppressLint("NewApi")
	public static void applyGradientProperty(Context mContext, GradientDrawable value, List<XmlDrawableProperty> properties){
		/** GRADIENT field **/
		int gd_x=0,gd_y=0,sc=0,cc=0,ec=0;
		value.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case useLevel:{
				value.setUseLevel(dynProp.getValueBoolean());
			}
			break;
			case type:{
				XmlDrawableProperty.Gradient gradient = XmlDrawableProperty.Gradient.valueOf(dynProp.getValueString().toUpperCase().trim());
				switch (gradient) {
				case LINEAR:{
					value.setGradientType(GradientDrawable.LINEAR_GRADIENT);
				}
				break;
				case RADIAL:{
					value.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				}
				break;
				case SWEEP:{
					value.setGradientType(GradientDrawable.SWEEP_GRADIENT);
				}
				break;
				}
			}
			break;
			case centerX:{
				gd_x = dynProp.getValueInt();
			}
			break;
			case centerY:{
				gd_y = dynProp.getValueInt();
			}
			break;
			case gradientRadius:{
				value.setGradientRadius(dynProp.getValueFloat());
			}
			break;
			case startColor:{
				switch (dynProp.type) {
					case PATH: {
						sc = (Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString());
					}
					break;
					case COLOR: {
						sc = dynProp.getValueColor();
					}
					break;
				}
			}
			break;
			case centerColor:{
				switch (dynProp.type) {
					case PATH: {
						cc = (Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString());
					}
					break;
					case COLOR: {
						cc = dynProp.getValueColor();
					}
					break;
				}
			}
			break;
			case endColor:{
				switch (dynProp.type) {
					case PATH: {
						ec = (Integer) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString());
					}
					break;
					case COLOR: {
						ec = dynProp.getValueColor();
					}
					break;
				}
			}
			break;
			case angle:{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					switch (dynProp.getValueInt()){
						case 45: {
							value.setOrientation(GradientDrawable.Orientation.TL_BR);
						}
						break;
						case 90: {
							value.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
						}
						break;
						case 135: {
							value.setOrientation(GradientDrawable.Orientation.TR_BL);
						}
						break;
						case 180: {
							value.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
						}
						break;
						case 225: {
							value.setOrientation(GradientDrawable.Orientation.BR_TL);
						}
						break;
						case 270: {
							value.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
						}
						break;
						case 315: {
							value.setOrientation(GradientDrawable.Orientation.BL_TR);
						}
						break;
						case 360:
						case 0: {
							value.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
						}
						break;
					}
				}
			}
			break;
			}
		}

		if(gd_x != 0 && gd_y != 0)
			value.setGradientCenter(gd_x, gd_y);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if(cc != 0)
				value.setColors(new int[]{sc,cc,ec});
			else
				value.setColors(new int[]{sc,ec});
		}
	}

	/**
	 * apply some padding properties in ShapeDrawable
	 */
	public static void applyPaddingProperty(GradientDrawable value, List<XmlDrawableProperty> properties){
		/** PADDING field **/
		int pd_l=0,pd_t=0,pd_r=0,pd_b=0;

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.field) {
			case left:{
				pd_l = dynProp.getValueInt();
			}
			break;
			case top:{
				pd_t = dynProp.getValueInt();
			}
			break;
			case right:{
				pd_r = dynProp.getValueInt();
			}
			break;
			case bottom:{
				pd_b = dynProp.getValueInt();
			}
			break;
			}
		}
		
		value.setBounds(pd_l, pd_t, pd_r, pd_b);
	}

}
