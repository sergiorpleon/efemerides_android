package android.dte.listefemerides;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.dte.listefemerides.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class VerActivityList extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verlayout);

		// Obtenemos el Intent que ha lanzado esta ventana
		Intent intentOrigen = getIntent();
		//Bundle params = intentOrigen.getExtras();

		//Obteniendo una efemeride determinada
		DBHelper entry = new DBHelper(this);
		entry.open();
		EfemerideItem[] datos = new EfemerideItem[1];
		datos[0] = entry.getEfemerides()[Global.ahora];

		//Llenando la lista con la efemeride
		AdaptadorEfemerides adaptador = new AdaptadorEfemerides(this, datos);
		ListView lstOpciones = (ListView) findViewById(R.id.LstOpciones);
		lstOpciones.setAdapter(adaptador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sobre:
			// hacer
			Dialog dialogo = crearDialogoAlerta();
			dialogo.show();
			//miToast();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private Dialog crearDialogoAlerta() {
		//Creacion del Dialogo
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("INFORMACIÓN");
		//Inflando el contenido con un layout mio
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.layout_sobre,(ViewGroup) findViewById(R.id.lytLayout));
		builder.setView(layout);
		
		builder.setPositiveButton("REGRESAR", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
}
