package org.bts_sio.promask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class ConfigurationActivity extends Activity implements OnClickListener {
	private View screen;
	private Button next;
	private EditText addr;
	private EditText port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //#detail : configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_configuration); //#detail : application du xml à l'écran
		
		//#part : récupération des éléments
		screen = this.findViewById(R.id.fullscreen_content);
		next = (Button)this.findViewById(R.id.confirm);
		addr = (EditText)this.findViewById(R.id.addr);
		port =(EditText)this.findViewById(R.id.port);
		
		//#part : événement
		next.setOnClickListener(this);
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
}