package android.dte.listefemerides;

import android.app.Activity;
import android.content.Context;
import android.dte.listefemerides.R;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//Adaptador personalizado que se encarga de rellenar el grid con los dias
public class AdaptadorEfemeridesDia extends ArrayAdapter {
	Context context;
	String[] datos;
	int mes;

	AdaptadorEfemeridesDia(Context context, String[] datos) {
		super(context, R.layout.layout_item_dia, datos);
		this.datos = datos;
		this.context = context;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		
		//---obtencion de la vista (TExtView)
		View item = inflater.inflate(R.layout.layout_item_dia, null);
		TextView lblFecha = (TextView) item.findViewById(R.id.LblCuadro);
		lblFecha.setText(datos[position]);//asiganandole el numero al label del grid
		lblFecha.setHeight(Global.alto / 12);//asignandole el alto
		if (!datos[position].equalsIgnoreCase("")) {
			//---pintar label de azul si corresponde con un dia relevante
			if (Global.relevancia[mes][Integer.parseInt(datos[position])] == 1) {
				lblFecha.setBackgroundColor(Color.parseColor("#FF5BA6E3"));
			}
		}
		return (item);
	}
}