package org.bts_sio.promask;

import com.illposed.osc.*;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;

public class FullscreenActivity extends Activity implements OnTouchListener {
	private View screen;
	private ImageButton[] writes;
	private String addr;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //#detail : configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_fullscreen); //#detail : application du xml à l'écran
		
		//#part : configuration de l'écran
		screen = this.findViewById(R.id.fullscreen_content);
		addr = this.getIntent().getExtras().getString("addr");
		port = Integer.parseInt(this.getIntent().getExtras().getString("port"));
		
		//#part : récupération/configuration des bouttons
		writes = new ImageButton[4];
		writes[0] = (ImageButton)this.findViewById(R.id.TLCorner);
		writes[1] = (ImageButton)this.findViewById(R.id.TRCorner);
		writes[2] = (ImageButton)this.findViewById(R.id.BLCorner);
		writes[3] = (ImageButton)this.findViewById(R.id.BRCorner);
		
		for(int i = 0; i < writes.length; i++)
			writes[i].setOnTouchListener(this);
	}
	
	public int limitScreen(int size, float position) {
		int new_position = (int)position - size / 2;
		return new_position;
	}

	@Override
	public boolean onTouch(View obj, MotionEvent event) {
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
		GestionOSC.OSCSend(addr, port, msg);
		return false;
	}
}