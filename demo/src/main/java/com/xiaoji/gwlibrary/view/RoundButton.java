/**
 *
 */
package com.xiaoji.gwlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.dvc.xml.AssetsResUtils;


/**
 * @author Administrator
 *
 */

/**
 *
 * @author liushen
 */
public class RoundButton extends View {
	/**
	 * �ı�
	 */
	private String mTitleText = "";
	/**
	 * �ı�����ɫ
	 */
	private int mTitleTextColor = Color.WHITE;
	/**
	 * �ı�����ɫ
	 */
	private int mTitleBackgroundColor = AssetsResUtils.getColor(getContext(), "colorPrimary");

	private int mTitleCircleColor = AssetsResUtils.getColor(getContext(), "colorPrimary");

	private int mTitleCornerRadius = -1;

	private int tempColor;
	//小红点
	private boolean showRedDot=false;
	/**
	 * �ı��Ĵ�С
	 */
	private int mTitleTextSize;
	/**
	 * ����ʱ�����ı����Ƶķ�Χ
	 */
	private Rect mBound;
	private Paint mPaint;

	private static int sCorner;
	private boolean hasClicEffect = true;

	public RoundButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundButton(Context context) {
		this(context, null);
	}

	public RoundButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/**
		 * �������������Զ�����ʽ����
		 */
		sCorner = (int) AssetsResUtils.dip2px(context,1);
		tempColor = mTitleBackgroundColor;
		mTitleTextSize = (int) AssetsResUtils.sp2px(context, 15);
//		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
//				R.styleable.RoundButton, defStyle, 0);
//		int n = a.getIndexCount();
//		for (int i = 0; i < n; i++) {
//			int attr = a.getIndex(i);
//			if (attr == R.styleable.RoundButton_RoundButtonText) {
//				mTitleText = a.getString(attr);
//
//			} else if (attr == R.styleable.RoundButton_RoundButtonTextColor) {// Ĭ����ɫ����Ϊ��ɫ
//				mTitleTextColor = a.getColor(attr, Color.WHITE);
//
//			} else if (attr == R.styleable.RoundButton_RoundButtonBackground) {// Ĭ����ɫ����Ϊ��ɫ
//				mTitleBackgroundColor = a.getColor(attr, mTitleBackgroundColor);
//				tempColor = mTitleBackgroundColor;
//
//			} else if (attr == R.styleable.RoundButton_RoundButtonCircleColor) {// Ĭ����ɫ����Ϊ��ɫ
//				mTitleCircleColor = a.getColor(attr, mTitleCircleColor);
//
//			} else if (attr == R.styleable.RoundButton_RoundButtonTextSize) {// Ĭ������Ϊ16sp��TypeValueҲ���԰�spת��Ϊpx
//				mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
//						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15,
//								getResources().getDisplayMetrics()));
//
//			}else if (attr == R.styleable.RoundButton_RoundButtonCornerRadius) {
//				mTitleCornerRadius = a.getDimensionPixelSize(attr, (int) TypedValue
//						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1,
//								getResources().getDisplayMetrics()));
//			}
//
//		}
//		a.recycle();
		if (mTitleTextSize == 0) {
			mTitleTextSize = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, 14, getResources()
							.getDisplayMetrics());
		}

		/**
		 * ��û����ı��Ŀ�͸�
		 */
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(mTitleTextSize);
		// mPaint.setColor(mTitleTextColor);
		mBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// TODO Auto-generated method stub
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = 0;
		int height = 0;

		/**
		 * ���ÿ��
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:// ��ȷָ����
			width = getPaddingLeft() + getPaddingRight() + specSize;
			break;
		case MeasureSpec.AT_MOST:// һ��ΪWARP_CONTENT
			width = getPaddingLeft() + getPaddingRight() + mBound.width();
			break;
		}

		/**
		 * ���ø߶�
		 */
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:// ��ȷָ����
			height = getPaddingTop() + getPaddingBottom() + specSize;
			break;
		case MeasureSpec.AT_MOST:// һ��ΪWARP_CONTENT
			height = getPaddingTop() + getPaddingBottom() + mBound.height();
			break;
		}
		setMeasuredDimension(width, height);
	}
	Canvas canvas;
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		onDrawnormal(canvas,mTitleCircleColor,mTitleBackgroundColor,mTitleTextColor,mTitleCornerRadius);

	}

	private void onDrawnormal(Canvas canvas ,int mTitleCircleColor,int mTitleBackgroundColor,int mTitleTextColor, int mTitleCornerRadius) {
		RectF r1 = new RectF();
		r1.left = 0;
		r1.right = getWidth();
		r1.top = 0;
		r1.bottom = getHeight();
		mPaint.setColor(mTitleCircleColor);
		canvas.drawRoundRect(r1, mTitleCornerRadius == -1 ? getHeight() / 2 : mTitleCornerRadius,
				mTitleCornerRadius == -1 ? getHeight() / 2 : mTitleCornerRadius, mPaint);

		RectF r2 = new RectF();
		r2.left = 1;
		r2.right = getWidth()-1;
		r2.top = 1;
		r2.bottom = getHeight()-1;
		mPaint.setColor(mTitleBackgroundColor);
//		mPaint.setStrokeWidth(sCorner);
//		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawRoundRect(r2, mTitleCornerRadius == -1 ? getHeight() / 2 : mTitleCornerRadius,
				mTitleCornerRadius == -1 ? getHeight() / 2 : mTitleCornerRadius, mPaint);


		mPaint.setColor(mTitleTextColor);
		// 测量基线，保证居中对齐
		FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
		int baseline = (getHeight() - fontMetrics.bottom + fontMetrics.top) / 2
				- fontMetrics.top;
		canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2,
				baseline, mPaint);
		//小红点
		if (showRedDot){
			int dotRadius=(int) AssetsResUtils.dip2px(getContext(),2);
			float y=baseline+fontMetrics.ascent;
			float x = mPaint.measureText(mTitleText)+getWidth() / 2 - mBound.width() / 2;
			mPaint.setColor(Color.RED);
			canvas.drawCircle(x+dotRadius,y+dotRadius,dotRadius,mPaint);
		}

	}

	public void setText(String text){
		mTitleText = text;
		mBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
		invalidate();
	}

	public String getText(){
		return mTitleText;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(this.hasClicEffect)
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mTitleBackgroundColor = 0xffbbbbbb;
				invalidate();
			} else if (event.getAction() == MotionEvent.ACTION_UP
					||event.getAction()==MotionEvent.ACTION_CANCEL) {
				mTitleBackgroundColor = tempColor;
				invalidate();
			}

		return super.onTouchEvent(event);
	}

	public void setClickEffect(boolean state){
		this.hasClicEffect = state;
	}

	public void setRoundButtonBackground(int color){
		mTitleBackgroundColor = color;
		tempColor = mTitleBackgroundColor;
		invalidate();
	}

	public void setRoundButtonCircleColor(int color){
		mTitleCircleColor = color;
		invalidate();
	}

	public void setTextColor(int color) {
		mTitleTextColor = color;
		invalidate();
	}
	public void showReddot(){
		showRedDot=true;
		invalidate();
	}
	public void hideRedDot(){
		showRedDot=false;
		invalidate();
	}
	public void setRedDot(boolean isShow){
		showRedDot=isShow;
		invalidate();
	}
	
	private void RoundButtonText(String text) {
		setText(text);
	}
	private void RoundButtonTextColor(int color) {
		setTextColor(color);
	}
	private void RoundButtonTextSize(float size) {
		mTitleTextSize = (int)size;
		mPaint.setTextSize(mTitleTextSize);
		mBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
		invalidate();
	}
	private void RoundButtonBackground(int color) {
		setRoundButtonBackground(color);
	}
	private void RoundButtonCircleColor(int color) {
		setRoundButtonCircleColor(color);
	}
	private void RoundButtonCornerRadius(float size) {
		mTitleCornerRadius = (int)size;
		invalidate();
	}

}
