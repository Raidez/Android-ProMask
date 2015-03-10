package org.bts_sio.promask;

import org.bts_sio.promask.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FullscreenActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private SystemUiHider ui;
	private View screen;
	private SeekBar[] writes;
	public static int SEEKBAR_STEP = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_fullscreen);
		
		screen = this.findViewById(R.id.fullscreen_content);
		screen.setOnClickListener(this);
		ui = SystemUiHider.getInstance(this, screen, SystemUiHider.FLAG_HIDE_NAVIGATION);
		ui.setup();
		
		writes = new SeekBar[4];
		writes[0] = (SeekBar) this.findViewById(R.id.writeA);
		writes[1] = (SeekBar) this.findViewById(R.id.writeB);
		writes[2] = (SeekBar) this.findViewById(R.id.writeC);
		writes[3] = (SeekBar) this.findViewById(R.id.writeD);
		
		for(int i = 0; i < writes.length; i++)
		{
			writes[i].setMax(SEEKBAR_STEP);
			writes[i].setOnSeekBarChangeListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.fullscreen_content) {
			ui.toggle();
		}
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		double percentage = (double)seekBar.getProgress() / (double)SEEKBAR_STEP;
		Log.d("pourcentage de la selection", "" + percentage);
	}
}