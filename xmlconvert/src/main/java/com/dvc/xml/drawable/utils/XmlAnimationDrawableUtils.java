package com.dvc.xml.drawable.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.dvc.xml.AssetsResUtils;
import com.dvc.xml.drawable.XmlDrawableProperty;
import com.dvc.xml.drawable.XmlDrawableUtils;

import java.util.List;

/**
 * Created by dvc on 18/8/2017.
 * Helper function that apply properties in animationdrawable
 */
public class XmlAnimationDrawableUtils {

	/**
	 * Generate a AnimationDrawable
	 * @param mContext
	 * @param properties
	 */
	public static Drawable generateDrawable(Context mContext, List<XmlDrawableProperty> properties) {

		AnimationDrawable value = new AnimationDrawable();
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case ANIMATION_LIST: {
				switch (dynProp.field) {
				case oneshot: {
					value.setOneShot(dynProp.getValueBoolean());
				}
				break;
				case visible: {
					value.setVisible(dynProp.getValueBoolean(), dynProp.getValueBoolean());
				}
				break;
				case variablePadding: {
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
	 * apply some item property in AnimationDrawable
	 */
	private static void applyItemProperty(Context mContext, AnimationDrawable value, List<XmlDrawableProperty> properties) {
		Drawable frame = null;
		int duration = 0;
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
			case ITEM:{
				switch (dynProp.field) {
				case drawable:{
					frame = AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString());
				}
				break;
				case duration:{
					duration = dynProp.getValueInt();
				}
				break;
				}

			}
			break;

			default:{
				frame = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
			}
			break;
			}
		}
		if(frame != null)
			value.addFrame(frame, duration);
	}

}
