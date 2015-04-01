package org.bts_sio.promask;

import com.illposed.osc.OSCMessage;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ConfigurationActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private Button next;
	private EditText addr;
	private EditText port;
	private SeekBar[] rgb = new SeekBar[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //#detail : configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_configuration2); //#detail : application du xml à l'écran
		
		//#part : récupération des éléments
		next = (Button)this.findViewById(R.id.confirm);
		addr = (EditText)this.findViewById(R.id.addr);
		addr.setText("172.16.69.255");
		port = (EditText)this.findViewById(R.id.port);
		port.setText("9876");
		rgb[0] = (SeekBar)this.findViewById(R.id.color_red);
		rgb[1] = (SeekBar)this.findViewById(R.id.color_green);
		rgb[2] = (SeekBar)this.findViewById(R.id.color_blue);
		
		//#part : événement
		next.setOnClickListener(this);
		for(int i = 0; i < rgb.length; i++)
			rgb[i].setOnSeekBarChangeListener(this);
	}

	@Override
	public void onClick(View obj) {
		if(obj.getId() == R.id.confirm)
		{
			if(addr.length() > 0 && port.length() > 0)
			{
				if(addr.getText().toString().matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$") && port.getText().toString().matches("^[0-9]+$"))
				{
					Intent intent = new Intent(this, FullscreenActivity.class);
					intent.putExtra("addr", addr.getText().toString());
					intent.putExtra("port", port.getText().toString());
					startActivity(intent);
				}
				else
					Toast.makeText(this, "L'adresse ou le port est invalide !", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(this, "Vous devez remplir les champs !", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Vector send = new Vector(rgb.length);
		for(int i = 0; i < rgb.length; i++)
		{
			float color = (float)rgb[i].getProgress() / (float)rgb[i].getMax();
			send.add(color);
		}

		OSCMessage msg = new OSCMessage("/mallarme/masque/color", send);
		GestionOSC.OSCSend(addr.getText().toString(), Integer.parseInt(port.getText().toString()), msg);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}