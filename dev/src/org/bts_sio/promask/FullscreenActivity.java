package org.bts_sio.promask;

import com.illposed.osc.*;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;

/**
 * Activité qui permet de déplacer les 4 points afin de modifier le masque
 * @author alexis
 */
public class FullscreenActivity extends Activity implements OnTouchListener {
	private View screen;
	private ImageButton[] writes;
	private String addr;
	private int port;

	/**
	 * méthode de base qui permet la création/reprise de l'activité
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //création/reprise de l'activité
		
		//#part : configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_fullscreen); //application du xml(=layout) à l'écran
		
		//#part : récupération des infos envoyées par l'activité précédente
		screen = this.findViewById(R.id.fullscreen_content);
		addr = this.getIntent().getExtras().getString("addr");
		port = Integer.parseInt(this.getIntent().getExtras().getString("port"));
		
		//#part : récupération/configuration des 4 points
		writes = new ImageButton[4];
		writes[0] = (ImageButton)this.findViewById(R.id.TLCorner);
		writes[1] = (ImageButton)this.findViewById(R.id.TRCorner);
		writes[2] = (ImageButton)this.findViewById(R.id.BLCorner);
		writes[3] = (ImageButton)this.findViewById(R.id.BRCorner);
		
		//#part: attachement des événements
		for(int i = 0; i < writes.length; i++)
			writes[i].setOnTouchListener(this); //onTouch
	}
	
	/**
	 * fonction permettant d'obtenir la position au centre d'un élément
	 * @param size Taille de l'élément
	 * @param position Position de l'élément
	 * @return int
	 */
	public int limitScreen(int size, float position) {
		int new_position = (int)position - size / 2;
		return new_position;
	}

	/**
	 * événement gérant le toucher/l'appui sur l'écran
	 * @param obj La vue où est détecté l'événement (=ici il s'agit donc directement de l'objet)
	 * @param event Le type d'événement (ex: down=appui; up=relachement)
	 * @return booléan
	 */
	@Override
	public boolean onTouch(View obj, MotionEvent event) {
		//#part: déplacement de l'objet
		if(event.getAction() ==  MotionEvent.ACTION_UP)
		{
			//lorsque l'on relache l'appui
			if((obj.getTop()+obj.getHeight()) >= screen.getHeight())
				obj.offsetTopAndBottom(- obj.getHeight()*2);
			
			/**
			 * pourquoi décale-t-on l'objet uniquement si il dépasse ?
			 * car il est impossible de placer un objet sur les côtés à cause de leurs dimensions
			 */
		}
		else
			obj.offsetTopAndBottom(limitScreen(obj.getHeight(), event.getY()));
		
		obj.offsetLeftAndRight(limitScreen(obj.getWidth(), event.getX()));
		
		//#part: envoie des infos au serveur
		float X = (float)obj.getLeft() / (float)screen.getWidth() + (float)obj.getWidth() / 2 / (float)screen.getWidth();
		float Y = (float)obj.getTop() / (float)screen.getHeight() + (float)obj.getHeight() / 2 / (float)screen.getHeight();
		Vector send = new Vector(2);
		send.add(X);
		send.add(Y);

		OSCMessage msg = new OSCMessage("/mallarme/masque/"+obj.getResources().getResourceEntryName(obj.getId()), send); //création du paquet
		GestionOSC.OSCSend(addr, port, msg); //envoie du paquet
		return false;
	}
}