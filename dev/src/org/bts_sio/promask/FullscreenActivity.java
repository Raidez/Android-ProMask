package org.bts_sio.promask;

import com.illposed.osc.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;

public class FullscreenActivity extends Activity implements OnTouchListener {
	private View screen;
	private ImageButton[] writes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //#detail : configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_fullscreen); //#detail : application du xml à l'écran
		
		//#part : configuration de l'écran
		screen = this.findViewById(R.id.fullscreen_content);
		
		//#part : récupération/configuration des bouttons
		writes = new ImageButton[4];
		writes[0] = (ImageButton)this.findViewById(R.id.TLCorner);
		writes[1] = (ImageButton)this.findViewById(R.id.TRCorner);
		writes[2] = (ImageButton)this.findViewById(R.id.BLCorner);
		writes[3] = (ImageButton)this.findViewById(R.id.BRCorner);
		
		for(int i = 0; i < writes.length; i++)
			writes[i].setOnTouchListener(this);
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
	
	public int limitScreen(int size, float position) {
		int new_position = (int)position - size / 2;
		return new_position;
	}

	@Override
	public boolean onTouch(View obj, MotionEvent event) {
		/*
		v.offsetLeftAndRight(limitScreen(v.getWidth(), event.getX()));
		v.offsetTopAndBottom(limitScreen(v.getHeight(), event.getY()));
		Log.d("taille du layout", screen.getHeight()+" x "+screen.getWidth());
		
		
		return false;*/
		//Log.d("screenY", ""+screen.getHeight());
		Log.d("action", ""+event.getAction());
		if(event.getAction() ==  MotionEvent.ACTION_UP)
		{
			if((obj.getTop()+obj.getHeight()) >= screen.getHeight())
				obj.offsetTopAndBottom(- obj.getHeight()*2);
		}
		else
			obj.offsetTopAndBottom(limitScreen(obj.getHeight(), event.getY()));
		
		obj.offsetLeftAndRight(limitScreen(obj.getWidth(), event.getX()));
		
		
		float X = (float)obj.getLeft() / (float)screen.getWidth() + (float)obj.getWidth() / 2 / (float)screen.getWidth();
		float Y = (float)obj.getTop() / (float)screen.getHeight() + (float)obj.getHeight() / 2 / (float)screen.getHeight();
		Vector send = new Vector(2);
		send.add(X);
		send.add(Y);

		OSCMessage msg = new OSCMessage("/mallarme/masque/"+obj.getResources().getResourceEntryName(obj.getId()), send);
		OSCSend("172.16.100.176", 12345, msg); //#detail : mon addr
		//OSCSend("172.16.100.145", 9876, msg); //#detail : addr de M.David
		return false;
	}
}