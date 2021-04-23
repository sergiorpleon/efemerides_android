package android.dte.listefemerides;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class BuscarActivityApp extends Activity implements OnNavigationListener {
	//int miMes;
	Context context;
	GridView grdDias;
	GridView grdOpciones;
	TextView labMes;
	//String[] datosDias = { "D", "L", "M", "Mi", "J", "V", "S" };
	String[] datos;
	String[] mes = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
			"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre" };
	int[] extra = { 1, -2, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1 };
	int indMes;
	int diaUno;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.layout_mes);

		Intent intentOrigen = getIntent();
		
		Global.ancho=getWindowManager().getDefaultDisplay().getWidth();
		Global.alto=getWindowManager().getDefaultDisplay().getHeight();

		
		Global.relevancia = new int[12][39];
		// Enero
		Global.relevancia[0][1] = 1;
		Global.relevancia[0][5] = 1;
		Global.relevancia[0][28] = 1;
		// Febrero
		Global.relevancia[1][18] = 1;
		Global.relevancia[1][24] = 1;
		Global.relevancia[1][25] = 1;
		// Marzo
		Global.relevancia[2][4] = 1;
		Global.relevancia[2][13] = 1;
		Global.relevancia[2][15] = 1;
		Global.relevancia[2][25] = 1;
		// Abril
		Global.relevancia[3][4] = 1;
		Global.relevancia[3][16] = 1;
		Global.relevancia[3][19] = 1;
		Global.relevancia[3][22] = 1;
		// Mayo
		Global.relevancia[4][1] = 1;
		Global.relevancia[4][5] = 1;
		Global.relevancia[4][17] = 1;
		Global.relevancia[4][19] = 1;
		// Junio
		Global.relevancia[5][4] = 1;
		Global.relevancia[5][5] = 1;
		Global.relevancia[5][6] = 1;
		Global.relevancia[5][18] = 1;
		// Julio
		Global.relevancia[6][26] = 1;
		Global.relevancia[6][29] = 1;
		Global.relevancia[6][30] = 1;
		// Agosto
		// Septiembre
		Global.relevancia[8][2] = 1;
		Global.relevancia[8][11] = 1;
		Global.relevancia[8][12] = 1;
		Global.relevancia[8][26] = 1;
		Global.relevancia[8][28] = 1;
		// Octubre
		Global.relevancia[9][6] = 1;
		Global.relevancia[9][8] = 1;
		Global.relevancia[9][10] = 1;
		Global.relevancia[9][16] = 1;
		// Noviembre
		Global.relevancia[10][7] = 1;
		Global.relevancia[10][17] = 1;
		Global.relevancia[10][26] = 1;
		Global.relevancia[10][27] = 1;
		// Diciembre
		Global.relevancia[11][2] = 1;
		Global.relevancia[11][7] = 1;
		Global.relevancia[11][20] = 1;
		Global.relevancia[11][22] = 1;
		Global.relevancia[11][31] = 1;
		
		// Obtenecion el action bar y adicion del spiner con los meses
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, 
                R.array.lista, android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);

        Calendar calAct = new GregorianCalendar();
		actionBar.setSelectedNavigationItem(calAct.get(Calendar.MONTH)); //asignacion del mes actual al spiner
		
		context = this;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
	
	private void ventana_mes(int indmes){
		indMes = indmes;
		// mostrar los dia mes
		Calendar calAct = new GregorianCalendar();
		Calendar calendario = new GregorianCalendar(calAct.get(Calendar.YEAR),(indMes), 1);
		diaUno = calendario.get(Calendar.DAY_OF_WEEK);//obteniendo el dia de la semana que comienza el mes

		int b = (calAct.get(Calendar.YEAR) % 4 == 0 && indMes == 1) ? 1 : 0;
		datos = new String[29 + extra[indMes] + b + diaUno];
		//---llenando datos los numeros segun mes y dia de la semana...
		for (int i = 1; i <= (29 + extra[indMes] + b + diaUno); i++) {
			if (i < diaUno) {
				datos[i - 1] = "";
			} else {
				datos[i - 1] = "" + (i - diaUno + 1);
			}
		}
		
		//---Asiganacion de los dias de la semana
		String[] datosDias = { "D", "L", "M", "Mi", "J", "V", "S" };
		ArrayAdapter<String> adaptadordias = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, datosDias);
		grdDias = (GridView) findViewById(R.id.GridDias);
		grdDias.setAdapter(adaptadordias);

		//---Creacion del grid con los numeros de los dias
		// ArrayAdapter<String> adaptador = null;
		// adaptador = new ArrayAdapter<String>(context,
		// android.R.layout.simple_list_item_1, datos);
		AdaptadorEfemeridesDia adaptador = null;
		adaptador = new AdaptadorEfemeridesDia(context, datos);//llamo a mi adaptador
		adaptador.setMes(indMes);
		//View v = (View) findViewById(android.R.layout.simple_list_item_1);
		grdOpciones = (GridView) findViewById(R.id.GridMes);
		grdOpciones.setAdapter(adaptador);
		grdOpciones
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							android.view.View v, int position, long id) {
						if (((TextView) v).getText() != "") {
							Intent i = new Intent(context,
									MostrarActivityList.class);
							i.putExtra("dia", ((TextView) v).getText());
							i.putExtra("mes", (indMes + 1) + "");
							startActivity(i);
							//((Activity) context).overridePendingTransition(
							//		R.anim.slide_in_child_bottom,
							//		R.anim.zoom_forward_out);
						}
					}
				});
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		ventana_mes(arg0);
		return false;
	}
}
