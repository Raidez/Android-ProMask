package org.bts_sio.promask;

import java.util.Vector;

import android.view.View;
import com.illposed.osc.OSCMessage;

public class Cardinalite {
	//attr
	private int x, y; //position de l'objet sur l'écran (note: le CENTRE de l'objet)
	private float fx, fy; //position en % de l'objet selon l'écran (de 0.~1 à 1)
	private View obj; //objet représentant la variable (dans la vue)
	
	//construct
	public Cardinalite(View obj) {
		this.obj = obj;
	};
	
	//method
	/**
	 * fonction permettant d'obtenir la position au centre d'un élément
	 * @param size Taille de l'élément
	 * @param position Position de l'élément
	 * @return int
	 */
	public int getPositionCenter(int size, float position) {
		int new_position = (int)position - size / 2;
		return new_position;
	};
	/**
	 * fonction pour mettre à jour la position de l'objet et calculer les positions F
	 * @param x
	 * @param y
	 * @param screenWidth Largeur de l'écran
	 * @param screenHeight Hauteur de l'écran
	 */
	public void updatePosition(float x, float y, int screenWidth, int screenHeight) {
		//modification de la position de l'objet
		obj.offsetLeftAndRight(this.getPositionCenter(obj.getWidth(), x));
		obj.offsetTopAndBottom(this.getPositionCenter(obj.getHeight(), y));
		
		//récupération de la position de l'objet (le centre)
		this.x = obj.getLeft() + obj.getWidth() / 2;
		this.y = obj.getTop() + obj.getHeight() / 2;
		
		//calcul de la position en pourcentage
		this.fx = (float)this.x / (float)screenWidth;
		this.fy = (float)this.y / (float)screenHeight;
	};
	/**
	 * fonction pour envoyer la position de l'objet avec OSC
	 * @param addr Adresse du serveur
	 * @param port Port de l'application
	 */
	public void sendPosition(String addr, int port) {
		//création des informations à envoyer
		Vector send = new Vector(2);
		send.add(this.fx);
		send.add(this.fy);
		
		//création du paquet OSC
		OSCMessage msg = new OSCMessage("/mallarme/"+ this.obj.getResources().getResourceEntryName(this.obj.getId()) +"/position", send);
		GestionOSC.OSCSend(addr, port, msg);
	};
}