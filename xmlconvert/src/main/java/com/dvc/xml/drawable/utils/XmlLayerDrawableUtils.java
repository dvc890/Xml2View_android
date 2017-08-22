package com.dvc.xml.drawable.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableProperty.PaddingMode;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in layerdrawable
 */
public class XmlLayerDrawableUtils {

	/**
	 * Generate a LayerDrawable
	 * @param mContext
	 * @param properties
	 */
	@SuppressLint("NewApi")
	public static LayerDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
		/** PADDING field **/
		int pd_l=0,pd_t=0,pd_r=0,pd_b=0,pd_s=0,pd_e=0;
		int index = 0;

		LayerDrawable value = new LayerDrawable(new Drawable[]{new BitmapDrawable()});
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case LAYER_LIST: {
				switch (dynProp.field) {
				case opacity:{
					if(dynProp.getValueString().equals("opaque")){
						value.setOpacity(0xff);
					}else if(dynProp.getValueString().equals("transparent")){
						value.setOpacity(0);
					}else if(dynProp.getValueString().equals("translucent")){
						value.setOpacity(0xff/2);
					}else{
						value.setOpacity(Integer.valueOf(dynProp.getValueString()));
					}
				}
				break;
				case autoMirrored:{
					value.setAutoMirrored(dynProp.getValueBoolean());
				}
				break;
				case paddingMode:{
					switch (PaddingMode.valueOf(dynProp.getValueString().toUpperCase().trim())) {
					case NEST:
						value.setPaddingMode(LayerDrawable.PADDING_MODE_NEST);
						break;
					case STACK:
						value.setPaddingMode(LayerDrawable.PADDING_MODE_STACK);
						break;
					}
				}
				break;
				case paddingTop:{
					pd_t = dynProp.getValueInt();
				}
				break;
				case paddingBottom:{
					pd_b = dynProp.getValueInt();
				}
				break;
				case paddingLeft:{
					pd_l = dynProp.getValueInt();
				}
				break;
				case paddingRight:{
					pd_r = dynProp.getValueInt();
				}
				break;
				case paddingStart:{
					pd_s = dynProp.getValueInt();
				}
				break;
				case paddingEnd:{
					pd_e = dynProp.getValueInt();
				}
				break;
				}
				value.setPadding(pd_l, pd_t, pd_r, pd_b);
			}
			break;

			case ITEM:{
				applyItemProperty(mContext, value, dynProp.getValuePropertyList(), index++);
			}
			break;
			}
		}

		return value;
	}

	/**
	 * apply some item property in LayerDrawable
	 */
	@TargetApi(23)
	public static void applyItemProperty(Context mContext, LayerDrawable value, List<XmlDrawableProperty> properties, int index){
		/** PADDING field **/
		int pd_l=0,pd_t=0,pd_r=0,pd_b=0;
		if(index >0)
			value.addLayer(new BitmapDrawable());
		int resid = -1;

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case ITEM:{
				switch (dynProp.field) {
				case id: {
					String idstr = dynProp.getValueString();
					if(idstr.contains("@android:id")){
						Class<?> clazz;
						try {
							clazz = Class.forName("com.android.internal.R$id");
							Object object = clazz.newInstance();
							try {
								resid = Integer.parseInt(clazz.getField(idstr.split("/")[1])
										.get(object).toString());
								value.setId(index, resid);
							} catch (IllegalArgumentException
									| NoSuchFieldException e) {
								e.printStackTrace();
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}else{
						// to be support
					}
				}
				break;
				case left:{
					value.setLayerInsetLeft(index, dynProp.getValueInt());
				}
				break;
				case top:{
					value.setLayerInsetTop(index, dynProp.getValueInt());
				}
				break;
				case right:{
					value.setLayerInsetRight(index, dynProp.getValueInt());
				}
				break;
				case bottom:{
					value.setLayerInsetBottom(index, dynProp.getValueInt());
				}
				break;
				case start:{
					value.setLayerInsetStart(index, dynProp.getValueInt());
				}
				break;
				case end:{
					value.setLayerInsetEnd(index, dynProp.getValueInt());
				}
				break;
				case width:{
					value.setLayerInsetEnd(index, dynProp.getValueInt());
				}
				break;
				case height:{
					value.setLayerInsetEnd(index, dynProp.getValueInt());
				}
				break;
				case gravity:{
					value.setLayerInsetEnd(index, dynProp.getValueInt());
				}
				break;
				case drawable:{
					try {
						value.setDrawable(index, AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString()));
					} catch (Exception e) {
						try {
							value.setDrawable(index, new ColorDrawable((int) AssetsResUtils.getAssetValue(mContext, dynProp.getValueString())));
						} catch (Exception e2) {
						}
					}
				}
				break;
				}
			}
			break;
			
			default:{
				value.setDrawable(index, XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList()));
			}
			break;
			
			}
		}

	}
}
