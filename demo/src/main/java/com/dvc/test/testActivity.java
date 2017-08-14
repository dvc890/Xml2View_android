package com.dvc.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dvc.xml.XmlDynamicView;
import com.dvc.xml.XmlDynamicViewId;
import com.xiaoji.gwlibrary.view.RoundButton;
import com.xiaoji.gwlibrary.view.SeekBarRelativeLayout;

public class testActivity extends Activity implements View.OnClickListener{
	SampleViewHolder holder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View sampleView = XmlDynamicView.createView(this, "layout/example.xml", SampleViewHolder.class);
		holder = (SampleViewHolder) sampleView.getTag();
		holder.ok_btn.setOnClickListener(this);
		holder.speed_edit_decrease.setOnClickListener(this);
		holder.speed_edit_increase.setOnClickListener(this);
		holder.hitspeed_edit_value_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				Toast.makeText(getApplicationContext(), "onProgressChanged:"+i, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		setContentView(sampleView);
	}

    
    static public class SampleViewHolder {
    	@XmlDynamicViewId(id = "speed_edit_decrease")
    	public TextView speed_edit_decrease;
    	@XmlDynamicViewId(id = "speed_edit_increase")
    	public TextView speed_edit_increase;
    	@XmlDynamicViewId(id = "hitspeed_edit_value_seekbar")
    	public SeekBarRelativeLayout hitspeed_edit_value_seekbar;
		@XmlDynamicViewId(id = "ok_btn")
		public RoundButton ok_btn;
        public SampleViewHolder() {
        }
    }

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == holder.speed_edit_decrease.getId()){
			Toast.makeText(getApplicationContext(), "click decrease", Toast.LENGTH_SHORT).show();
		}
		else if (arg0.getId() == holder.speed_edit_increase.getId()){
			Toast.makeText(getApplicationContext(), "click increase", Toast.LENGTH_SHORT).show();
		}
		else if (arg0.getId() == holder.ok_btn.getId()){
			Toast.makeText(getApplicationContext(), "click ok_btn", Toast.LENGTH_SHORT).show();
		}
	}
}
