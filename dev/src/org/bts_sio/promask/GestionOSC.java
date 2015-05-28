package org.bts_sio.promask;

import com.illposed.osc.*;
import java.io.IOException;
import java.net.*;

/**
 * Classe qui permet de gérer plus simplement l'envoi de paquet OSC
 * @author alexis
 */
public class GestionOSC {
	/**
	 * 
	 * @param addr Adresse du serveur au format decimal pointée (=string)
	 * @param port Port du serveur au format entier
	 * @param msg Le paquet a envoyé au serveur (construit avec OSCMessage)
	 * @see OSCMessage pour savoir comment construire un paquet OSC
	 */
	public static void OSCSend(final String addr, final int port, final OSCMessage msg) {
		Thread temp = new Thread(new Runnable() {
			//créer un processus afin de pouvoir envoyer de façon asynchrone les paquets
			@Override
			public void run() {
				try {
					OSCPortOut sender = new OSCPortOut(InetAddress.getByName(addr), port);
					sender.send(msg);
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		temp.start(); //démarre le processus
	}
}