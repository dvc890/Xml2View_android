package com.dvc.xml.drawable.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in statelistdrawable
 */
public class XmlStateListDrawableUtils {

	/**
	 * Generate a StateListDrawable
	 * @param mContext
	 * @param properties
	 */
	@SuppressLint("NewApi")
	public static StateListDrawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties){
		StateListDrawable value = new StateListDrawable();
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case SELECTOR:{
				switch (dynProp.field) {
				case visible:{
					value.setVisible(dynProp.getValueBoolean(), dynProp.getValueBoolean());
				}
				break;
				case dither:{
					value.setDither(dynProp.getValueBoolean());
				}
				break;
				case enterFadeDuration:{
					if(android.os.Build.VERSION.SDK_INT >11)
						value.setEnterFadeDuration(dynProp.getValueInt());
				}
				break;
				case exitFadeDuration:{
					if(android.os.Build.VERSION.SDK_INT >11)
						value.setExitFadeDuration(dynProp.getValueInt());
				}
				break;
				case autoMirrored:{
					if(android.os.Build.VERSION.SDK_INT >11)
						value.setAutoMirrored(dynProp.getValueBoolean());
				}
				break;
				case variablePadding:{
					// to be support
				}
				break;
				case constantSize:{
					// to be support
				}
				break;
				}
			}
			break;

			case ITEM:{
				applyItemProperty(mContext, value, dynProp.getValuePropertyList());
			}
			break;
			}
		}

		return value;
	}

	/**
	 * apply some item properties in StateListDrawable
	 */
	public static void applyItemProperty(Context mContext, StateListDrawable value, List<XmlDrawableProperty> properties){
		/** ITEM field **/
		Drawable drawable = null;
		boolean state = false;
		int resid = 0;
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case ITEM:{
				switch (dynProp.field) {
				case drawable:{
					drawable = AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString());
				}
				break;
				case state_focused:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_focused * (state?1:-1);
				}
				break;
				case state_window_focused:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_window_focused * (state?1:-1);
				}
				break;
				case state_checkable:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_checkable * (state?1:-1);
				}
				break;
				case state_checked:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_checked * (state?1:-1);
				}
				break;
				case state_selected:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_selected * (state?1:-1);
				}
				break;
				case state_pressed:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_pressed * (state?1:-1);
				}
				break;
				case state_activated:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_activated * (state?1:-1);
				}
				break;
				case state_active:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_active * (state?1:-1);
				}
				break;
				case state_single:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_single * (state?1:-1);
				}
				break;
				case state_first:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_first * (state?1:-1);
				}
				break;
				case state_middle:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_middle * (state?1:-1);
				}
				break;
				case state_last:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_last * (state?1:-1);
				}
				break;
				case state_accelerated:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_accelerated * (state?1:-1);
				}
				break;
				case state_hovered:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_hovered * (state?1:-1);
				}
				break;
				case state_drag_can_accept:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_drag_can_accept * (state?1:-1);
				}
				break;
				case state_drag_hovered:{
					state = dynProp.getValueBoolean();
					resid = android.R.attr.state_drag_hovered * (state?1:-1);
				}
				break;
				case state_accessibility_focused:{
					state = dynProp.getValueBoolean();
					Class<?> clazz;
					try {
						clazz = Class.forName("com.android.internal.R$attr");
						Object object = clazz.newInstance();
						try {
							resid = Integer.parseInt(clazz.getField("state_accessibility_focused")
									.get(object).toString());
						} catch (IllegalArgumentException
								| NoSuchFieldException e) {
							e.printStackTrace();
						}
						resid = resid * (state?1:-1);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				break;

				}
			}
			break;
			default:{
				drawable = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
			}
			break;
			}
		}
			value.addState(new int[]{resid}, drawable);
		}
	}
