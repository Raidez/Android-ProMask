package org.bts_sio.promask;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class ConfigurationActivity extends Activity implements OnClickListener {
	private View screen;
	private Button next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //#detail : création/reprise de l'activité
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //#detail : configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_configuration); //#detail : application du xml à l'écran
		
		//#part : récupération des éléments
		screen = this.findViewById(R.id.fullscreen_content);
		next = (Button)this.findViewById(R.id.confirm);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View obj) {
		if(obj.getId() == R.id.confirm)
		{
			Toast.makeText(this, "Vérifiez vos paramètres", Toast.LENGTH_LONG).show();
		}
	}
}