package org.bts_sio.promask;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.http.conn.util.InetAddressUtils;
import org.bts_sio.promask.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.illposed.osc.*;

public class FullscreenActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private SystemUiHider ui;
	private View screen;
	private SeekBar[] writes;
	public static int SEEKBAR_STEP = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		this.setContentView(R.layout.activity_fullscreen); //#detail : application du xml à l'écran
		
		//#part : configuration de l'écran
		screen = this.findViewById(R.id.fullscreen_content);
		screen.setOnClickListener(this);
		ui = SystemUiHider.getInstance(this, screen, SystemUiHider.FLAG_HIDE_NAVIGATION);
		ui.setup();
		
		//#part : configuration des paquets OSC
		/*
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				OSCMessage msg = new OSCMessage("/mallarme/masque/test");

				try {
					OSCPortOut sender = new OSCPortOut(InetAddress.getByName("172.16.100.176"), 12345);
					sender.send(msg);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		*/
		Vector test = new Vector(2);
		test.add(0.5);
		test.add(0.5);
		
		OSCMessage msg = new OSCMessage("/mallarme/masque/A", test);
		this.OSCSend("172.16.100.176", 12345, msg);
		
		//#part : configuration des seekbars
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
	
	public void OSCSend(final String addr, final int port, final OSCMessage msg) {
		Thread temp = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					OSCPortOut sender = new OSCPortOut(InetAddress.getByName(addr), port);
					sender.send(msg);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		temp.start();
	}
}