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

import java.util.ArrayList;
import java.util.List;

import static com.dvc.xml.AssetsResUtils.getAssetDrawable;

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
				}
				break;

				case ITEM:{
					applyItemProperty(mContext, value, dynProp.getValuePropertyList(), index++);
				}
				break;
			}
		}
		if(pd_l != 0 && pd_t != 0 && pd_r != 0 && pd_b != 0)
			value.setPadding(pd_l, pd_t, pd_r, pd_b);

		return value;
	}

	/**
	 * apply some item property in LayerDrawable
	 */
	@TargetApi(23)
	public static void applyItemProperty(Context mContext, LayerDrawable value, List<XmlDrawableProperty> properties, int index){
		/** PADDING field **/
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
							value.setDrawable(index, getAssetDrawable(mContext, dynProp.getValueString()));
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

	public static LayerDrawable generateDrawable_Simple(Context mContext, List<XmlDrawableProperty> properties){
		List<Drawable> drawables = new ArrayList<>();
		List<Integer> integers = new ArrayList<>();

		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
				case ITEM: {
					Object[] objects = applyItemProperty_Simple(mContext, dynProp.getValuePropertyList());
					integers.add((Integer) objects[0]);
					drawables.add((Drawable) objects[1]);
				}
				break;
			}
		}
		Drawable[] drawablelist=new Drawable[drawables.size()];
		for(int i=0;i<drawables.size();i++){
			drawablelist[i]=drawables.get(i);
		}

		LayerDrawable value = new LayerDrawable(drawablelist);
		int index = 0;
		for (Integer id : integers) {
			if(id != -1)
				value.setId(index,id);
			index++;
		}

		return value;
	}

	public static Object[] applyItemProperty_Simple(Context mContext, List<XmlDrawableProperty> properties){
		int resid = -1;
		Drawable drawable = new ColorDrawable();
		for (XmlDrawableProperty dynProp : properties) {
			switch (dynProp.name) {
				case ITEM: {
					switch (dynProp.field) {
						case id: {
							String idstr = dynProp.getValueString();
							if (idstr.contains("@android:id")) {
								Class<?> clazz;
								try {
									clazz = Class.forName("com.android.internal.R$id");
									Object object = clazz.newInstance();
									try {
										resid = Integer.parseInt(clazz.getField(idstr.split("/")[1])
												.get(object).toString());
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
							} else {
								// to be support
							}
						}
						break;
						case drawable:{
							drawable = AssetsResUtils.getAssetDrawable(mContext, dynProp.getValueString());
						}
						break;
					}
				}
				break;
				default: {
					drawable = XmlDrawableUtils.generateDrawable(mContext, dynProp.name.name(), dynProp.getValuePropertyList());
				}
				break;

			}

		}
		return new Object[]{resid, drawable};
	}
}
