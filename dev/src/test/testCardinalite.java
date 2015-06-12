package test;

import java.util.Vector;

import org.bts_sio.promask.Cardinalite;
import org.bts_sio.promask.GestionOSC;
import org.bts_sio.promask.R;

import com.illposed.osc.OSCMessage;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class testCardinalite {
	public static void main(Activity context) {
		View anna = new ImageView(context);
		anna.setId(R.id.annabelle);
		anna.offsetLeftAndRight(0);
		anna.offsetTopAndBottom(0);
		
		Cardinalite cardAnnabelle = new Cardinalite(anna);
		cardAnnabelle.sendPosition("172.16.100.94", 12345); //test effectué avec mon adresse IP
	}
}