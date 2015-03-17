package org.bts_sio.promask;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Vector;

import org.bts_sio.promask.util.SystemUiHider;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import com.illposed.osc.*;

public class FullscreenActivity extends Activity implements OnClickListener, OnTouchListener {
	//private SystemUiHider ui;
	private int mActionBarSize;
	private View screen;
	private ImageButton[] writes;
	public static int SEEKBAR_STEP = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		this.setContentView(R.layout.activity_fullscreen); //#detail : application du xml à l'écran
		
		//#part : configuration de l'écran
		screen = this.findViewById(R.id.fullscreen_content);
		screen.setOnClickListener(this);
		//ui = SystemUiHider.getInstance(this, screen, SystemUiHider.FLAG_HIDE_NAVIGATION);
		//ui.setup();
		final TypedArray styledAttributes = getBaseContext().getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.actionBarSize });
		mActionBarSize = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		
		//#part : configuration du test
		writes = new ImageButton[4];
		writes[0] = (ImageButton)this.findViewById(R.id.A);
		writes[1] = (ImageButton)this.findViewById(R.id.B);
		writes[2] = (ImageButton)this.findViewById(R.id.C);
		writes[3] = (ImageButton)this.findViewById(R.id.D);
		
		for(int i = 0; i < writes.length; i++)
			writes[i].setOnTouchListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.fullscreen_content) {
			//ui.toggle();
		}
	}
	/*
		OSCMessage msg = new OSCMessage("/mallarme/masque/A", data);
		this.OSCSend("172.16.100.176", 12345, msg);*/
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
	
	public int limitScreen(int size, float position) {
		int new_position = (int)position - size / 2;
		return new_position;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.offsetLeftAndRight(limitScreen(v.getWidth(), event.getX()));
		v.offsetTopAndBottom(limitScreen(v.getHeight(), event.getY()));
		
		float X = (float)v.getLeft() / (float)screen.getWidth() + (float)v.getWidth() / 2 / (float)screen.getWidth();
		float Y = (float)v.getTop() / ((float)screen.getHeight() + mActionBarSize) + (float)v.getHeight() / 2 / ((float)screen.getHeight() + mActionBarSize);
		Vector send = new Vector(2);
		send.add(X);
		send.add(Y);
		OSCMessage msg = new OSCMessage("/mallarme/masque/"+v.getResources().getResourceEntryName(v.getId()), send);
		OSCSend("172.16.100.176", 12345, msg);
		return false;
	}
}