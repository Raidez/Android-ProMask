package org.bts_sio.promask;

import com.illposed.osc.*;
import java.io.IOException;
import java.net.*;

public class GestionOSC {
	public static void OSCSend(final String addr, final int port, final OSCMessage msg) {
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
}