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

/**
 * Elle instancie l'activité qui permet de configurer :
 * - adresse et port du serveur
 * - couleur du masque
 * @author alexis
 */
public class ConfigurationActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private Button next;
	private EditText addr;
	private EditText port;
	private SeekBar[] rgb = new SeekBar[3];

	/**
	 * méthode de base qui permet la création/reprise de l'activité
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // création/reprise de l'activité
		
		//#part: configuration de l'écran
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //configure l'écran sans l'actionBar
		this.setContentView(R.layout.activity_configuration2); //application du xml(=layout) à l'écran
		
		/**
		 * pourquoi y'a t-il 2 layout "activity_configuration" ?
		 * car le premier est un relative layout, finalement trop complex, il fut abandonné pour un linear layout,
		 * plus facile à configurer et mettre en place, cependant cet ancien vestige est toujours disponible
		 */
		
		//#part: récupération des éléments
		next = (Button)this.findViewById(R.id.confirm); //le bouton pour passer à l'activité suivante
		addr = (EditText)this.findViewById(R.id.addr); //l'adresse du serveur
		port = (EditText)this.findViewById(R.id.port); //le port du serveur
		rgb[0] = (SeekBar)this.findViewById(R.id.color_red); //quantité de rouge
		rgb[1] = (SeekBar)this.findViewById(R.id.color_green); //quantité de vert
		rgb[2] = (SeekBar)this.findViewById(R.id.color_blue); //quantité de bleu
			//#subpart: configuration de base de l'adresse et du port du serveur Raspi
		addr.setText("172.16.69.255");
		port.setText("9876");
		
		//#part: attachement des événements
		next.setOnClickListener(this); //onClick
		for(int i = 0; i < rgb.length; i++)
			rgb[i].setOnSeekBarChangeListener(this); //onChangeRangeValue
	}

	/**
	 * événement permettant de gérer l'appui sur l'écran
	 * @param obj La vue où est détecté l'événement
	 */
	@Override
	public void onClick(View obj) {
		if(obj.getId() == R.id.confirm)
		{
			//si l'objet appuyé est le bouton confirme alors
			if(addr.length() > 0 && port.length() > 0)
			{
				//si l'adresse et le port ne sont pas vide alors
				if(addr.getText().toString().matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$") && port.getText().toString().matches("^[0-9]+$"))
				{ //vérifie si l'adresse et le port sont correctement formatés (ex: adresse: xxx.xx.x.xx et port: xxxx)
					Intent intent = new Intent(this, FullscreenActivity.class); //créer la nouvelle activité
					intent.putExtra("addr", addr.getText().toString()); //transmet l'adresse à l'activité
					intent.putExtra("port", port.getText().toString()); //transmet le port à l'activité
					startActivity(intent); //démarre l'activité
				}
				else
					Toast.makeText(this, "L'adresse ou le port est invalide !", Toast.LENGTH_LONG).show(); //message d'erreur
			}
			else
				Toast.makeText(this, "Vous devez remplir les champs !", Toast.LENGTH_LONG).show(); //message d'erreur
		}
	}

	/**
	 * événement permettant de gérer les seekbar
	 * @param seekBar La seekbar
	 * @param progress La progression
	 * @param fromUser Booléan pour savoir si l'événement a été lancer par l'utilisateur (=! programmer)
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		//#part: calcul de la colorimétrie
		Vector send = new Vector(rgb.length); //object qui va être envoyé au serveur
		for(int i = 0; i < rgb.length; i++)
		{
			float color = (float)rgb[i].getProgress() / (float)rgb[i].getMax(); //on calcule (valeur actuelle divisée par 255), ce qui permet d'obtenir le pourcentage en décimal
			send.add(color); //la valeur calculée est ajouté à l'objet
		}
		
		//#part: envoyer la colorimétrie au serveur
		OSCMessage msg = new OSCMessage("/mallarme/masque/color", send); //création du paquet OSC
		GestionOSC.OSCSend(addr.getText().toString(), Integer.parseInt(port.getText().toString()), msg); //envoie du paquet
	}

	/**
	 * pourquoi n'utiliser que l'événement "onProgressChanged" ?
	 * cela permet d'envoyer la couleur du masque à chaque changement et donc de voir en direct la couleur visible
	 * tandis qu'avec les événements ci-dessous, la couleur du masque n'était visible qu'au début (=onStartTrackingTouch) ou à la fin (=onStopTrackingTouch)
	 */
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
}