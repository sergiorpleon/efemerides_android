package android.dte.listefemerides;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.dte.listefemerides.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MostrarActivityList extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verlayout);

		//--obtencion del dia y mes a mostrar por la actividad
		Intent i = getIntent();
		String dia = i.getStringExtra("dia");
		String mes = i.getStringExtra("mes");
		
		//---Abrir la base datos y acceder a las efemerides de un dia y mes dado
		DBHelper entry = new DBHelper(this);
		entry.open();
		EfemerideItem[] datos = entry.getEfemerides(dia, mes);

		//---Inflar la lista con mi adaptador
		AdaptadorEfemerides adaptador = new AdaptadorEfemerides(this, datos);
		ListView lstOpciones = (ListView) findViewById(R.id.LstOpciones);
		lstOpciones.setAdapter(adaptador);
	}

}
