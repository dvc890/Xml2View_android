package com.xiaoji.gwlibrary.view;

import com.dvc.xmlviewanalysis.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by dvc on 2017/4/10.
 */

public class SeekBarRelativeLayout extends RelativeLayout {
    Context mContext;
    private boolean isfrist;
    
    public SeekBarRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.seekbarrelativelayout, this, true);
        isfrist = true;
        initSeekBar();
    }

    public SeekBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.seekbarrelativelayout, this, true);
        isfrist = true;
        initSeekBar();
    }
    
    //测试自定义控件的属性设置类
    public void testcustomfunction(String str){
    	Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
    public void testcustomfunction2(int num){
    	Toast.makeText(mContext, num+"", Toast.LENGTH_SHORT).show();
    }
    

    private TextView textView;
    private SeekBar seekBar;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
    private int textViewPaddingLeft = 0;

    private void setText(String str) {
        textView.setText(str);
    }

    public void setMax(int value) {
        seekBar.setMax(value);
    }

    public int getMax(){
        return seekBar.getMax();
    }

    private void setMarginLeftForTextView(int progress) {
        if (seekBar != null && textView != null) {
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
            layoutParams.leftMargin = (int) (((float) progress / seekBar.getMax()) * width);

            setText(Integer.toString(progress));
            setTextSize();
            layoutParams.leftMargin += seekBar.getPaddingRight() - textView.getWidth() / 2 + textViewPaddingLeft;
            if(isfrist){
                isfrist = false;
                layoutParams.leftMargin -= 10;
            }

            textView.setLayoutParams(layoutParams);
        }
    }

    private void setTextSize(){
        int len = textView.getText().length();
        if(len <= 1)
            textView.setTextSize(14);
        else if(len == 2)
            textView.setTextSize(12);
        else if(len == 3)
            textView.setTextSize(10);
        else if(len == 4)
            textView.setTextSize(8);
        else
            textView.setTextSize(6);
    }

    public void setProgress(int process) {
        if (seekBar != null) {
            seekBar.setProgress(process);
        }
    }

    public void setEnabled(boolean enabled) {
        if (seekBar != null) {
            seekBar.setEnabled(enabled);
        }
    }

    public void setTag(Object object) {
        seekBar.setTag(object);
    }

    public void initSeekBar() {
        seekBar = (SeekBar) findViewById(R.id.seek_bar_relative_layout_seek_bar);
        textView = (TextView) findViewById(R.id.seek_bar_relative_layout_text_view);
        //textView.setVisibility(INVISIBLE);

        textViewPaddingLeft = ((LayoutParams) textView.getLayoutParams()).leftMargin;
        if (seekBar != null && textView != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMarginLeftForTextView(seekBar.getProgress());
                }
            },100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setMarginLeftForTextView(progress);
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStartTrackingTouch(seekBar);
                    }

                    //textView.setVisibility(VISIBLE);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStopTrackingTouch(seekBar);
                    }
                    //textView.setVisibility(INVISIBLE);
                }
            });
        }
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }
}